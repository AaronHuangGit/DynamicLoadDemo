package com.aaron.android.codelibrary.imageloader;

import android.graphics.drawable.Drawable;

/**
 * Created on 15/6/16.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public interface ImageConfig {
    /**
     * @return 获取默认图片
     */
    public Drawable getDefaultImage();

    /**
     * @return 获取加载图片失败时显示的图片
     */
    public Drawable getFailureImage();
}
