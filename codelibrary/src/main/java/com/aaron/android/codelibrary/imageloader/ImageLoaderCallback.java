package com.aaron.android.codelibrary.imageloader;

import android.graphics.Bitmap;

/**
 * Created on 15/6/15.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public interface ImageLoaderCallback {
    /**
     * 图片加载完成后处理回调
     * @param bitmap 解码后的Bitmap
     */
    void finish(Bitmap bitmap);
}
