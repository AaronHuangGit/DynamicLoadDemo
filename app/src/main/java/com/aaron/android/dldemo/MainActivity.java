package com.aaron.android.dldemo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ryg.dynamicload.frameworkmodule.ITestPlugin;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private ListView mPluginListView;
    private List<PluginItem> mPluginItemList = new ArrayList<>();
    public static final String PLUGIN_DIR = "dex";
    private String dexOutPath;
    private ITestPlugin mITestPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
    }

    private void initData() {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "aaron/";
        mPluginItemList.add(new PluginItem(rootPath + "pluginApk.apk", PluginItem.PluginType.TYPE_APK, "打开一个插件窗口"));
        mPluginItemList.add(new PluginItem(rootPath + "plugina.jar", PluginItem.PluginType.TYPE_JAR, "弹出一个插件化Toast实现"));
    }

    private void initViews() {
        mPluginListView = (ListView) findViewById(R.id.listview_plugins);
        mPluginListView.setAdapter(new PluginListAdapter());
        mPluginListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PluginItem pluginItem = mPluginItemList.get(position);
                if (pluginItem.getPluginType() == PluginItem.PluginType.TYPE_APK) {
                    DLPluginManager dlPluginManager = DLPluginManager.getInstance(MainActivity.this);
                    dlPluginManager.loadApk(pluginItem.getFilePath());
                    dlPluginManager.startPluginActivity(MainActivity.this, new DLIntent("com.aaron.android.pluginapk", "com.aaron.android.pluginapk.MainActivity"));
                } else {
                    mITestPlugin = loadLocalPlugin(pluginItem.getFilePath());
                    if (mITestPlugin != null) {
                        mITestPlugin.showDialog(MainActivity.this, "大厨小灶", "成功调用插件模块");
                    }
                }
            }
        });
    }

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
    private class PluginListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mPluginItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return mPluginItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_plugin, null, false);
            }
            PluginItem pluginItem = mPluginItemList.get(position);
            TextView pluginPathTextView = (TextView) convertView.findViewById(R.id.textview_plugin_path);
            TextView pluginDescriptionTextView = (TextView) convertView.findViewById(R.id.textview_plugin_description);
            pluginDescriptionTextView.setText(pluginItem.getPluginDescription());
            pluginPathTextView.setText(pluginItem.getFilePath());
            return convertView;
        }
    }

}
