package com.aaron.android.codelibrary.http;


/**
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public class RequestError extends Exception {

    public RequestError(String detailMessage) {
        super(detailMessage);
    }

    public RequestError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RequestError(Throwable throwable) {
        super(throwable);
    }
}
