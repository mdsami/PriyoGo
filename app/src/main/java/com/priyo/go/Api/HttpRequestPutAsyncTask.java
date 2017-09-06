package com.priyo.go.Api;

import android.content.Context;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

public class HttpRequestPutAsyncTask extends HttpRequestAsyncTask {

    private final String mJsonString;

    public HttpRequestPutAsyncTask(String API_COMMAND, String mUri, String mJsonString,
                                   Context mContext, HttpResponseListener listener) {
        super(API_COMMAND, mUri, mContext, listener);
        this.mJsonString = mJsonString;
    }

    public HttpRequestPutAsyncTask(String API_COMMAND, String mUri, String mJsonString, Context mContext) {
        this(API_COMMAND, mUri, mJsonString, mContext, null);
    }

    @Override
    protected HttpRequestBase getRequest() {
        HttpPut httpPut = new HttpPut(mUri);
        try {
            if (mJsonString != null)
                httpPut.setEntity(new StringEntity(mJsonString, HTTP.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpPut;
    }
}