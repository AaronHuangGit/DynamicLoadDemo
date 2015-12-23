package com.aaron.android.codelibrary.http;

import com.aaron.android.codelibrary.http.result.BaseResult;

/**
 * Created on 15/6/9.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public abstract class RequestCallback<T extends BaseResult> {

    public void onStart() {

    }
    /**
     * 请求成功回调
     */
    public abstract void onSuccess(T result);

    /**
     * 请求失败回调
     */
    public abstract void onFailure(RequestError error);
}
