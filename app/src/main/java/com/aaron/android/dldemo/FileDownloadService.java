package com.aaron.android.dldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created on 15/7/14.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class FileDownloadService extends Service {
    public static final String ACTION_DOWNLOADING = "download_progress";
    public static final String ACTION_START_DOWNLOAD = "download_start";
    public static final String ACTION_DOWNLOAD_COMPLETE = "downloadcomplete";
    public static final String EXTRA_INSTALL_APK_PATH = "apkpath";
    public static final String EXTRA_DOWNLOAD_URL = "downloadUrl";
    public static final String EXTRA_DOWNLOAD_PATH = "downloadPath";
    public static final String EXTRA_PROGRESS = "downloadprogress";
    public static final String EXTRA_APK_LENGTH = "apkLength";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        String url = intent.getStringExtra(EXTRA_DOWNLOAD_URL);
        String path = intent.getStringExtra(EXTRA_DOWNLOAD_PATH);
        if (StringUtils.isEmpty(url)) {
            return super.onStartCommand(intent, flags, startId);
        }
        new DownloadThread(url, path).start();
        return super.onStartCommand(intent, flags, startId);
    }

    class DownloadThread extends Thread {
        private String mUrl;
        private String mDirectoryPath;
        private String mTempFilePath;
        private String mFilePath;

        public DownloadThread(String url, String directoryPath) {
            mUrl = url;
            mDirectoryPath = directoryPath;
            createDownloadFilePath();
        }

        @Override
        public void run() {
            downloadFile();
        }

        private String getDiskCacheDir() {
            String cachePath;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = getExternalCacheDir().getPath();
            } else {
                cachePath = getCacheDir().getPath();
            }
            return cachePath;
        }

        private void createDownloadFilePath() {
            mFilePath = getDownloadFileName();
            mTempFilePath = mFilePath + ".tmp";
            Log.e("DownloadService", "download apk path = " + mFilePath);
        }

        private String getDownloadFileName() {
            if (StringUtils.isEmpty(mDirectoryPath)) {
                mDirectoryPath = FileUtils.createFolder(getDiskCacheDir()).getPath();
            }
            File file = new File(mDirectoryPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            return mDirectoryPath + FileUtils.getFileName(mUrl);
        }

        private void downloadFile() {
            FileOutputStream outputStream = null;
            InputStream inputStream = null;
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(mUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(1000 * 60);
                httpURLConnection.setReadTimeout(1000 * 60);
                inputStream = httpURLConnection.getInputStream();
                int responseCode = httpURLConnection.getResponseCode();
                Log.e("DownloadService", "responseCode = " + responseCode);
                long contentLength = httpURLConnection.getContentLength();
                Intent intent = new Intent(ACTION_START_DOWNLOAD);
                intent.putExtra(EXTRA_APK_LENGTH, (int)contentLength);
                sendBroadcast(intent);
                Log.e("DownloadService", "contentLength = " + contentLength);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    outputStream = new FileOutputStream(mTempFilePath);
                    byte[] buffer = new byte[1024 * 8];
                    int temp;
                    int totalLength = 0;
                    intent = new Intent(ACTION_DOWNLOADING);
                    while ((temp = inputStream.read(buffer)) != -1) {
                        totalLength += temp;
                        Log.e("DownloadService", "download.length = " + (totalLength / (float)contentLength) * 100);
                        outputStream.write(buffer, 0, temp);
                        intent.putExtra(EXTRA_PROGRESS, totalLength);
                        sendBroadcast(intent);
                    }
                    FileUtils.rename(mTempFilePath, mFilePath);
                    intent = new Intent(ACTION_DOWNLOAD_COMPLETE);
                    intent.putExtra(EXTRA_INSTALL_APK_PATH, mFilePath);
                    sendBroadcast(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    stopSelf();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FileDownloadService", "FileDownloadService stop");
    }
}
