package com.aaron.android.codelibrary.http.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 15/6/8.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public class BaseResult implements ResultInterface {
    @SerializedName("code")
    private String mCode;

    @SerializedName("message")
    private String message;

    /**
     *
     * @return 请求状态码
     */
    public String getCode() {
        return mCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean isSuccess() {
        return mCode.equals("200");
    }
}
