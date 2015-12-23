package com.aaron.android.codelibrary.imageloader;

import android.net.Uri;
import android.view.View;

/**
 * 图片加载器
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public interface ImageLoader {
    /**
     * 初始化操作
     */
    void initialize();

    /**
     * 图片加载
     * @param view 占位图片
     * @param uri 图片资源Uri
     * @param imageConfig 图片加载配置
     * @param imageLoaderCallback 图片加载回调
     */
    void requestImage(View view, Uri uri, ImageConfig imageConfig, ImageLoaderCallback imageLoaderCallback);
}
