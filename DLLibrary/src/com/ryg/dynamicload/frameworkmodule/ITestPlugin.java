package com.ryg.dynamicload.frameworkmodule;

import android.content.Context;

/**
 * Created on 15/12/18.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public abstract class ITestPlugin {
    public abstract void showToast(Context context, String content);

    public abstract void showDialog(Context context, String title, String message);
}
