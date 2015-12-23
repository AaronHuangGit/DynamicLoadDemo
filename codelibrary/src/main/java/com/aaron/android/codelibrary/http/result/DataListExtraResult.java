package com.aaron.android.codelibrary.http.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 15/7/9.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class DataListExtraResult<T extends BaseData> extends DataListResult<T> {
    @SerializedName("extras")
    private ExtraData mExtraData;

    public ExtraData getExtraData() {
        return mExtraData;
    }
}
