package com.aaron.android.codelibrary.http;

import com.aaron.android.codelibrary.http.result.BaseResult;

import java.util.Map;

/**
 * 网络请求接口Interface,由具体的实现
 * Created on 15/6/13.
 *
 * @author HuangRan
 */
public interface AsyncRequest {
    /**
     * 网络请求
     * @param method 请求方式
     * @param url 请求地址
     * @param header 请求头
     * @param params 请求参数
     * @param requestCallback 请求回调
     */
    <T extends BaseResult> void doRequest(int method, String url, Class<T> cls, Map header, Map params, RequestCallback<T> requestCallback);
}
