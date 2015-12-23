package com.aaron.android.codelibrary.http.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 15/7/9.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class ExtraData extends BaseData {
    @SerializedName("count")
    private int mPageTotal;

    public int getPageTotal() {
        return mPageTotal;
    }

    public void setPageTotal(int pageTotal) {
        mPageTotal = pageTotal;
    }
}
