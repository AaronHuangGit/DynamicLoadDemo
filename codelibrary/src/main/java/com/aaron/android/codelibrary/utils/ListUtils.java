package com.aaron.android.codelibrary.utils;

import java.util.List;

/**
 * Created on 2014/11/17 19:44.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class ListUtils {
    /**
     * 判断List是否为null或者size为0
     * @param list List
     * @return true：empty
     */
    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }
}
