package com.aaron.android.dldemo;

/**
 * Created on 15/12/23.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class PluginItem {
    private String mFilePath;
    private PluginType mPluginType;
    private String mPluginDescription;


    public PluginItem(String filePath, PluginType pluginType, String pluginDescription) {
        mFilePath = filePath;
        mPluginType = pluginType;
        mPluginDescription = pluginDescription;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public PluginType getPluginType() {
        return mPluginType;
    }

    public void setPluginType(PluginType pluginType) {
        mPluginType = pluginType;
    }

    public String getPluginDescription() {
        return mPluginDescription;
    }

    public void setPluginDescription(String pluginDescription) {
        mPluginDescription = pluginDescription;
    }

    enum PluginType {
        TYPE_JAR,
        TYPE_APK
    }
}
