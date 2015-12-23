package com.aaron.android.codelibrary.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created on 15/7/16.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class DiskStorageUtils {
    public final static int ERROR = -1;
    public static boolean sdcardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     *
     * @return 获取本地Data目录
     */
    public static File getInternalDataStorageFile() {
        return Environment.getDataDirectory();
    }

    /**
     *
     * @return 获取本地Data目录路径
     */
    public static String getInternalDataStoragePath() {
        return getInternalDataStorageFile().getPath();
    }

    /**
     *
     * @return 获取sdcard目录
     */
    public static File getSdcardDirectory() {
        if (!sdcardExist()) {
            return null;
        }
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取sdcard目录路径
     * @return
     */
    public static String getSdcardDirectoryPath() {
        File file = getSdcardDirectory();
        if (file == null) {
            return null;
        }
        return file.getPath();
    }

    /**
     *
     * @return 获取sdcard可用大小
     */
    public static long getSdcardAvailableSize() {
        if (!sdcardExist()) {
            return ERROR;
        }
        File sdcardFile = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(sdcardFile.getPath());
        long blockSize, availableBlock;
        if (SDKVersionUtils.hasJellyBeanMR2()) {
            blockSize = statFs.getBlockSizeLong();
            availableBlock = statFs.getAvailableBlocksLong();
        } else {
            blockSize = statFs.getBlockSize();
            availableBlock = statFs.getAvailableBlocks();
        }
        return blockSize * availableBlock;
    }

    /**
     *
     * @return 获取sdcard总大小
     */
    public static long getSdcardTotalSize() {
        if (!sdcardExist()) {
            return ERROR;
        }
        File sdcardFile = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(sdcardFile.getPath());
        long blockSize, totalBlock;
        if (SDKVersionUtils.hasJellyBeanMR2()) {
            blockSize = statFs.getBlockSizeLong();
            totalBlock = statFs.getBlockCountLong();
        } else {
            blockSize = statFs.getBlockSize();
            totalBlock = statFs.getBlockCount();
        }
        return blockSize * totalBlock;
    }

}
