package com.priyo.go.Api;

import android.content.Context;
import android.util.Log;

import com.priyo.go.Utilities.Constants;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpRequestGetAsyncTask extends HttpRequestAsyncTask {

    private Map<String, String> params;

    public HttpRequestGetAsyncTask(String API_COMMAND, String mUri, Context mContext) {
        this(API_COMMAND, mUri, mContext, null);
    }

    public HttpRequestGetAsyncTask(String API_COMMAND, String mUri, Context mContext, HttpResponseListener listener) {
        this(API_COMMAND, mUri, mContext, null, listener);

    }

    public HttpRequestGetAsyncTask(String API_COMMAND, String mUri, Context mContext, Map<String, String> params, HttpResponseListener listener) {
        super(API_COMMAND, mUri, mContext, listener);
        this.params = params;
    }


    @Override
    protected HttpRequestBase getRequest() {
        if (Constants.DEBUG) {
            Log.w(Constants.GET_URL, mUri);
        }
        final String url = mUri + buildParams();
        return new HttpGet(url);
    }

    private String buildParams() {
        if (params == null)
            return "";
        else {
            StringBuilder paramBuilder = new StringBuilder("?");
            for (Map.Entry<String, String> param : params.entrySet()) {
                try {
                    paramBuilder
                            .append(URLEncoder.encode(param.getKey(), "UTF-8"))
                            .append("=").
                            append(URLEncoder.encode(param.getValue(), "UTF-8"))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return paramBuilder.toString();
        }
    }


}