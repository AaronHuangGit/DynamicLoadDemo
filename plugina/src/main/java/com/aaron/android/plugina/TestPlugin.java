package com.aaron.android.plugina;

import android.content.Context;
import android.widget.Toast;

import com.ryg.dynamicload.frameworkmodule.ITestPlugin;

/**
 * Created on 15/12/18.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class TestPlugin extends ITestPlugin {
    @Override
    public void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
