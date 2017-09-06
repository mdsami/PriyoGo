package com.priyo.go.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.priyo.go.Utilities.Utilities;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sajid.shahriar on 4/25/17.
 */

public class HttpMultiPartAsyncTask extends AsyncTask<Void, Void, HttpResponseObject> {

    private final String url;
    private final String apiCommand;
    private final MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
    private HttpResponseListener httpResponseListener;
    private Map<String, String> headers = new HashMap<>();
    private Context context;

    public HttpMultiPartAsyncTask(final String apiCommand, final String url, Context context) {
        this(apiCommand, url, context, null);
    }


    public HttpMultiPartAsyncTask(final String apiCommand, final String url, Context context, HttpResponseListener httpResponseListener) {
        this.apiCommand = apiCommand;
        this.url = url;
        this.context = context;
        this.httpResponseListener = httpResponseListener;
    }

    public MultipartBuilder addFormBody(String name, String value) {
        return multipartBuilder.addFormDataPart(name, value);
    }

    public MultipartBuilder addFormBody(String fileName, File sourceFile) throws MalformedURLException {
        final String fileExtension = MimeTypeMap.getFileExtensionFromUrl(sourceFile.toURI().toURL().toString());
        final MediaType mediaType = MediaType.parse("image/" + fileExtension);

        return multipartBuilder.addFormDataPart("file", fileName, RequestBody.create(mediaType, sourceFile));
    }

    public HttpMultiPartAsyncTask addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public void addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    @Override
    protected HttpResponseObject doInBackground(Void... params) {
        HttpResponseObject mHttpResponseObject;

        try {
            if (Utilities.isConnectionAvailable(context))
                mHttpResponseObject = makeRequest();
            else
                return null;
        } catch (Exception ex) {
            mHttpResponseObject = null;
        }

        return mHttpResponseObject;
    }

    public HttpResponseListener getHttpResponseListener() {
        return httpResponseListener;
    }

    public void setHttpResponseListener(HttpResponseListener httpResponseListener) {
        this.httpResponseListener = httpResponseListener;
    }

    @Override
    protected void onPostExecute(HttpResponseObject httpResponseObject) {
        super.onPostExecute(httpResponseObject);
        this.httpResponseListener.httpResponseReceiver(httpResponseObject);
    }

    private HttpResponseObject makeRequest() throws IOException {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        requestBuilder.post(multipartBuilder.build());

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestBuilder.build()).execute();
        String jsonResponse = "" + response.body().string();

        HttpResponseObject mHttpResponseObject = new HttpResponseObject();
        mHttpResponseObject.setStatus(response.code());
        mHttpResponseObject.setApiCommand(apiCommand);
        mHttpResponseObject.setJsonString(jsonResponse);
        return mHttpResponseObject;
    }
}
