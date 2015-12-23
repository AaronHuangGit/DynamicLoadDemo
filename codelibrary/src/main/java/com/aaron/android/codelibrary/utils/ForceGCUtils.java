package com.aaron.android.codelibrary.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created on 2015/3/23 14:00.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class ForceGCUtils {

    private static final String TAG = "ForceGCUtils";

    /**
     * ViewGroup中ImageView中Bitmap资源的回收
     *
     * @param layout     ViewGroup
     */
    public static void recycleAllImageResource(ViewGroup layout) {
        if (layout == null) {
            return;
        }
        synchronized (layout) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                View subView = layout.getChildAt(i);
                if (subView instanceof ViewGroup) {
                    recycleAllImageResource((ViewGroup) subView);
                } else {
                    if (subView instanceof ImageView) {
                        ForceGCUtils.recycleImageViewResource((ImageView)subView);
                        ForceGCUtils.recycleViewBackgroundResource(subView);
                        ForceGCUtils.recycleViewAnimationDrawable((ImageView)subView);
                    }
                }
            }
        }
    }

        /**
         * 回收ImageView中的图片资源
         * @param view ImageView
         */
    public static void recycleImageViewResource(ImageView view) {
        if (view == null) {
            return;
        }
        Drawable drawable = view.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                LogUtils.d(TAG, "imageview recycle resource bitmap!");
                view.setImageDrawable(null);
                bitmap.recycle();
            }
        }
    }

    /**
     * 回收ImageView中的背景图片资源，用于设置图片背景
     * @param view View
     */
    public static void recycleViewBackgroundResource(View view) {
        if (view == null) {
            return;
        }
        Drawable drawable = view.getBackground();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                LogUtils.d(TAG, "imageview recycle background bitmap!");
                if (SDKVersionUtils.hasJellyBean()) {
                    view.setBackground(null);
                } else {
                    view.setBackgroundDrawable(null);
                }
                bitmap.recycle();
            }
        }
    }

    /**
     * 回收做帧动画的ImageView中的图片资源
     * @param view ImageView
     */
    public static void recycleViewAnimationDrawable(ImageView view) {
        if (view == null) {
            return;
        }
        Drawable drawable = view.getBackground();
        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable animationDrawable = (AnimationDrawable)drawable;
            animationDrawable.stop();
            for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                Drawable frame = animationDrawable.getFrame(i);
                if (frame instanceof BitmapDrawable) {
                    LogUtils.d(TAG, "imageview recycle AnimationDrawable bitmap!");
                    if (SDKVersionUtils.hasJellyBean()) {
                        view.setBackground(null);
                    } else {
                        view.setBackgroundDrawable(null);
                    }
                    ((BitmapDrawable) frame).getBitmap().recycle();
                }
                frame.setCallback(null);
            }
            animationDrawable.setCallback(null);
        }
    }
}
