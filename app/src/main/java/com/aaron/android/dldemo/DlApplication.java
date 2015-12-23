package com.aaron.android.dldemo;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.os.Environment;

import com.ryg.dynamicload.frameworkmodule.ITestPlugin;

import java.io.File;
import java.lang.reflect.Constructor;

import dalvik.system.DexClassLoader;

/**
 * Created on 15/12/18.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class DlApplication extends Application {
    public static final String PLUGIN_DIR = "dex";
    private String dexOutPath;
    private ITestPlugin mITestPlugin;
    @Override
    public void onCreate() {
        super.onCreate();
        mITestPlugin = loadLocalPlugin("/sdcard/aaron/plugina.jar");
        if (mITestPlugin != null) {
            mITestPlugin.showToast(this, "成功调用插件");
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private ITestPlugin loadLocalPlugin(String jarPath) {
        ITestPlugin iTestPlugin = null;
        dexOutPath = getDir(PLUGIN_DIR, MODE_PRIVATE).getAbsolutePath();
        DexClassLoader dexClassLoader = new DexClassLoader(jarPath, dexOutPath, null, getApplicationContext().getClassLoader());
        try {
            Class<?> testPluginClass = dexClassLoader.loadClass("com.aaron.android.plugina.TestPlugin");
            Constructor<?> localConstructor = testPluginClass.getConstructor(new Class[]{});
            iTestPlugin = (ITestPlugin)localConstructor.newInstance(new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iTestPlugin;
    }
}
