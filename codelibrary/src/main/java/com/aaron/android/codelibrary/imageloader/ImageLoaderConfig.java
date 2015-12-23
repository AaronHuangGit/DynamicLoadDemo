package com.aaron.android.codelibrary.imageloader;

/**
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public interface ImageLoaderConfig {
    /**
     * 最大的磁盘缓存
     */
    public void getDiskCacheMaxSize();

    /**
     * 最大的内存缓存
     */
    public void getMemoryCacheMaxSize();

    /**
     * 图片磁盘缓存目录
     */
    public void getDiskMainCacheDirectory();

}
