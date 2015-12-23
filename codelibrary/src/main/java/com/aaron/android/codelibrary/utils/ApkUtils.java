package com.aaron.android.codelibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;


import java.io.File;

/**
 * 1. 安装apk
 * 2. 获取本机安装的应用程序信息-包名、版本
 *
 * @author guo.chen
 * @version 4.1.0
 */
public final class ApkUtils {

    /**
     * 安装应用程序
     *
     * @param context context
     * @param apkPath 安装程序路径
     * @return 成功返回true。失败返回fals
     */
    public static boolean install(Context context, String apkPath) {
        if (FileUtils.fileExists(apkPath)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 检查某个应用是否安装
     *
     * @param context     上下文资源
     * @param packagename 应用包名
     * @return 是否已安装
     */
    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }


}
