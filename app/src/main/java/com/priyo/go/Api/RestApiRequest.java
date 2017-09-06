package com.priyo.go.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sajid.shahriar on 4/14/17.
 */

public class RestApiRequest<T, K> extends AsyncTask<T, Void, ApiResponse<K>> {

    private final Context mContext;
    private final String mApiCommand;
    private final Method requestMethod;

    private String mApiUrl;
    private ApiResponse<K> apiResponse;
    private URLConnection mUrlConnection;
    private Class<K> kClass;

    private T t;

    public RestApiRequest(Context mContext, String mApiUrl, String mApiCommand, Class<K> kClass) {
        this(mContext, Method.GET, mApiUrl, mApiCommand, kClass);
    }

    public RestApiRequest(Context mContext, Method requestMethod, String mApiUrl, String mApiCommand, Class<K> kClass) {
        this(mContext, requestMethod, mApiUrl, null, mApiCommand, kClass);
    }

    public RestApiRequest(Context mContext, Method requestMethod, String mApiUrl, T t, String mApiCommand, Class<K> kClass) {
        this.mContext = mContext;
        this.requestMethod = requestMethod;
        this.mApiUrl = mApiUrl;
        this.t = t;
        this.mApiCommand = mApiCommand;
        this.kClass = kClass;
    }

    public void setBody(T t) {
        this.t = t;
    }

    public T getBody() {
        return this.t;
    }

    public Method getRequestMethod() {
        return requestMethod;
    }

    @Override
    @SafeVarargs
    protected final ApiResponse<K> doInBackground(T... params) {
        if (Utilities.isConnectionAvailable(mContext)) {
            createConnection();
        } else {
            if (Constants.DEBUG) Log.d(Constants.ERROR, mApiCommand);
            return null;
        }
        return null;
    }

    private HttpURLConnection createConnection() {
        try {
            URL url = new URL(URLEncoder.encode(mApiUrl, "UTF-8"));
            mUrlConnection = url.openConnection();
            mUrlConnection.setRequestProperty("", "");
        } catch (IOException e) {

        } finally {
            if (mUrlConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) mUrlConnection).disconnect();
            } else if (mUrlConnection instanceof HttpURLConnection) {
                ((HttpURLConnection) mUrlConnection).disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ApiResponse<K> apiResponse) {
        super.onPostExecute(apiResponse);
    }

    public enum Method {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE"),
        HEAD("HEAD"),
        OPTIONS("OPTIONS"),
        TRACE("TRACE"),
        PATCH("PATCH");


        private String methodName;

        Method(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public String toString() {
            return this.methodName;
        }

    }

    public interface ApiResponseListener {
        void onSuccess(ApiResponse apiResponse);

        void onFailed(ApiFailResponse apiFailResponse);
    }
}
