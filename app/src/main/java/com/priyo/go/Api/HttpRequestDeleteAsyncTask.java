package com.priyo.go.Api;

import android.content.Context;
import android.util.Log;

import com.priyo.go.Utilities.Constants;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;

public class HttpRequestDeleteAsyncTask extends HttpRequestAsyncTask {

    public HttpRequestDeleteAsyncTask(String API_COMMAND, String mUri, Context mContext) {
        this(API_COMMAND, mUri, mContext, null);
    }

    public HttpRequestDeleteAsyncTask(String API_COMMAND, String mUri, Context mContext, HttpResponseListener listener) {
        super(API_COMMAND, mUri, mContext, listener);
    }

    @Override
    protected HttpRequestBase getRequest() {
        if (Constants.DEBUG) {
            Log.w(Constants.DELETE_URL, mUri);
        }
        return new HttpDelete(mUri);
    }
}