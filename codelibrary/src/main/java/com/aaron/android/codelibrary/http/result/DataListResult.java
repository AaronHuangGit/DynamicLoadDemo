package com.aaron.android.codelibrary.http.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 15/6/30.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class DataListResult<T extends BaseData> extends BaseResult {
    @SerializedName("data")
    private List<T> mDataList;

    public List<T> getDataList() {
        return mDataList;
    }
}
