package com.aaron.android.dldemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;


/**
 * Created on 15/7/31.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class FileDownloaderManager {

    private ProgressDialog mDownloadApkProgressDialog;
//    private ProgressBar mDownloadApkProgressBar;
//    private TextView mDownloadApkProgressTextView;
    private AlertDialog mDownloadApkDialog;
    private DownloadNewApkBroadcast mDownloadNewApkBroadcast;
    private Context mContext;

    public FileDownloaderManager(Context context) {
        mContext = context;
    }

    public void downloadFile(String downloadUrl, String downloadPath) {
//        View downloadApkDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_download_apk, null, false);
//        mDownloadApkProgressBar = (ProgressBar) downloadApkDialogView.findViewById(R.id.progressbar_download_apk);
//        mDownloadApkProgressTextView = (TextView) downloadApkDialogView.findViewById(R.id.textview_download_apk);
//        mDownloadApkDialog = new AlertDialog.Builder(mContext)
//                .setView(downloadApkDialogView)
//                .create();
//        mDownloadApkDialog.setCancelable(false);
//        mDownloadApkDialog.setCanceledOnTouchOutside(false);
//        mDownloadApkDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        mDownloadApkDialog.show();
        showLoadProgressDialog();
        registerDownloadNewApkBroadcast();
        startDownloadApk(downloadUrl, downloadPath);
    }

    private void showLoadProgressDialog() {
        if (mDownloadApkProgressDialog == null) {
            mDownloadApkProgressDialog = new ProgressDialog(mContext);
            mDownloadApkProgressDialog.setMessage("正在下载插件更新包");
        }
        if (!mDownloadApkProgressDialog.isShowing()) {
            mDownloadApkProgressDialog.show();
        }
    }

    private void dismissLoadProgressDialog() {
        if (mDownloadApkProgressDialog != null && mDownloadApkProgressDialog.isShowing()) {
            mDownloadApkProgressDialog.dismiss();
        }
    }

    private void startDownloadApk(String downloadUrl, String downloadPath) {
        Intent intent = new Intent(mContext, FileDownloadService.class);
        intent.putExtra(FileDownloadService.EXTRA_DOWNLOAD_URL, downloadUrl);
        intent.putExtra(FileDownloadService.EXTRA_DOWNLOAD_PATH, downloadPath);
        mContext.startService(intent);
    }

    public class DownloadNewApkBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StringUtils.isEmpty(action)) {
                return;
            }
//            if (mDownloadApkDialog != null && mDownloadApkDialog.isShowing()) {
                if (action.equals(FileDownloadService.ACTION_DOWNLOADING)) {
//                    int progress = intent.getIntExtra(FileDownloadService.EXTRA_PROGRESS, 0);
//                    float percent = (float) progress / mDownloadApkProgressBar.getMax() * 100;
//                    mDownloadApkProgressBar.setProgress(progress);
//                    mDownloadApkProgressTextView.setText(
//                            mContext.getString(R.string.main_download_apk_progress, percent));
                } else if (action.equals(FileDownloadService.ACTION_START_DOWNLOAD)) {
                    showLoadProgressDialog();
//                    mDownloadApkProgressBar.setMax(intent.getIntExtra(FileDownloadService.EXTRA_APK_LENGTH, 0));
                } else if (action.equals(FileDownloadService.ACTION_DOWNLOAD_COMPLETE)) {
//                    ApkUtils.install(mContext, intent.getStringExtra(FileDownloadService.EXTRA_INSTALL_APK_PATH));
                    dismissLoadProgressDialog();
                    Toast.makeText(mContext, "插件下载完成", Toast.LENGTH_LONG).show();
                }
//            }
        }
    }

    private void registerDownloadNewApkBroadcast() {
        if (mDownloadNewApkBroadcast == null) {
            mDownloadNewApkBroadcast = new DownloadNewApkBroadcast();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FileDownloadService.ACTION_DOWNLOADING);
        intentFilter.addAction(FileDownloadService.ACTION_START_DOWNLOAD);
        intentFilter.addAction(FileDownloadService.ACTION_DOWNLOAD_COMPLETE);
        mContext.registerReceiver(mDownloadNewApkBroadcast, intentFilter);
    }

    private void unregisterDownloadNewApkBroadcast() {
        if (mDownloadNewApkBroadcast != null) {
            mContext.unregisterReceiver(mDownloadNewApkBroadcast);
            mDownloadNewApkBroadcast = null;
        }
    }

    public void onDestory() {
        unregisterDownloadNewApkBroadcast();
    }

}
