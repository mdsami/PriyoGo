package com.priyo.go.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpRequestAsyncTask extends AsyncTask<Void, Void, HttpResponseObject> {

    final String mUri;
    private final Context mContext;
    private final String API_COMMAND;
    public HttpResponseListener mHttpResponseListener;
    private HttpResponse mHttpResponse;
    private HashMap<String, String> headers;

    private boolean error = false;

    HttpRequestAsyncTask(String API_COMMAND, String mUri, Context mContext, HttpResponseListener listener) {
        this.API_COMMAND = API_COMMAND;
        this.mUri = mUri;
        this.mContext = mContext;
        this.mHttpResponseListener = listener;
        headers = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }


    @Override
    protected HttpResponseObject doInBackground(Void... params) {

        if (Utilities.isConnectionAvailable(mContext))
            mHttpResponse = makeRequest();
        else {
            if (Constants.DEBUG) Log.d(Constants.ERROR, API_COMMAND);
            error = true;
            return null;
        }

        InputStream inputStream = null;
        HttpResponseObject mHttpResponseObject = null;

        try {
            HttpEntity entity = mHttpResponse.getEntity();
            int status = mHttpResponse.getStatusLine().getStatusCode();
            Header[] headers = mHttpResponse.getAllHeaders();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            mHttpResponseObject = new HttpResponseObject();
            mHttpResponseObject.setStatus(status);
            mHttpResponseObject.setApiCommand(API_COMMAND);
            mHttpResponseObject.setJsonString(sb.toString());
            mHttpResponseObject.setHeaders(Arrays.asList(mHttpResponse.getAllHeaders()));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (Exception squish) {
                squish.printStackTrace();
            }
        }
        return mHttpResponseObject;
    }

    @Override
    protected void onPostExecute(final HttpResponseObject result) {
        if (error) {
            if (mContext != null)
                Toast.makeText(mContext, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            return;
        }

        if (Constants.DEBUG) {
            if (result == null)
                Log.e(Constants.RESULT, API_COMMAND + " NULL");
            else
                Log.w(Constants.RESULT, Constants.GET_REQUEST + result.toString());
        }

        if (result != null) {

            if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_UNAUTHORIZED) {
                String message = mContext.getString(R.string.please_log_in_again);

                try {
                    Gson gson = new Gson();
                    //message = gson.fromJson(result.getJsonString(), LoginResponse.class).getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    // Stop the Token Timer here
//                    CountDownTimer tokenTimer = TokenManager.getTokenTimer();
//                    if (tokenTimer != null) {
//                        tokenTimer.cancel();
//                        TokenManager.setTokenTimer(null);
//                    }

                    if (mHttpResponseListener != null)
                        mHttpResponseListener.httpResponseReceiver(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                if (mHttpResponseListener != null)
                    mHttpResponseListener.httpResponseReceiver(result);
            }

        } else {
            if (mHttpResponseListener != null)
                mHttpResponseListener.httpResponseReceiver(null);
        }

    }

    @Override
    protected void onCancelled() {
        mHttpResponseListener.httpResponseReceiver(null);
    }

    private HttpResponse makeRequest() {
        try {
            HttpRequestBase httpRequest = getRequest();

//            if (TokenManager.getToken().length() > 0)
//                httpRequest.setHeader(Constants.TOKEN, TokenManager.getToken());
//            if (TokenManager.isEmployerAccountActive())
//                httpRequest.setHeader(Constants.OPERATING_ON_ACCOUNT_ID, TokenManager.getOperatingOnAccountId());
            //httpRequest.setHeader(Constants.USER_AGENT, Constants.USER_AGENT_MOBILE_ANDROID);
            httpRequest.setHeader("Accept", "application/json");
            httpRequest.setHeader("Content-type", "application/json");
            httpRequest.setHeader("secret", "UGVvcGxlQ2xpZW50MjAxNg==");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpRequest.setHeader(entry.getKey(), entry.getValue());
            }


            HttpParams httpParams = new BasicHttpParams();
            HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
            HttpProtocolParams.setHttpElementCharset(httpParams, HTTP.UTF_8);
            HttpClient client = new DefaultHttpClient(httpParams);

            return client.execute(httpRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    Context getContext() {
        return mContext;
    }

    abstract protected HttpRequestBase getRequest();
}