package com.priyo.go.Api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PostNewsAsyncTask extends AsyncTask<Void, Void, HttpResponseObject> {

    private final Context mContext;
    private final String imagePath;
    private final String body;
    private final String API_COMMAND;
    public HttpResponseListener mHttpResponseListener;

    public PostNewsAsyncTask(String API_COMMAND, String imagePath, String body, Context mContext) {
        this.mContext = mContext;
        this.imagePath = imagePath;
        this.body = body;
        this.API_COMMAND = API_COMMAND;
    }

    @Override
    protected HttpResponseObject doInBackground(Void... params) {

        HttpResponseObject mHttpResponseObject;
        if (Utilities.isConnectionAvailable(mContext))
            mHttpResponseObject = uploadImageImage(imagePath, body);
        else
            return null;

        return mHttpResponseObject;
    }

    @Override
    protected void onCancelled() {
        mHttpResponseListener.httpResponseReceiver(null);
    }

    @Override
    protected void onPostExecute(final HttpResponseObject result) {

        if (result != null) {
            mHttpResponseListener.httpResponseReceiver(result);

        } else {
            mHttpResponseListener.httpResponseReceiver(null);
        }

    }

    private HttpResponseObject uploadImageImage(String filePath, String body) {

        try {
            File sourceFile = new File(filePath);

            SharedPreferences pref = mContext.getSharedPreferences(Constants.ApplicationTag, mContext.getApplicationContext().MODE_PRIVATE);
            String nodeId = pref.getString(Constants.USER_NODE_ID_KEY, "");
            String phoneNo = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
            String name = pref.getString(Constants.USER_FULL_NAME_KEY, "");

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//dd/MM/yyyy
            sdfDate.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date now = new Date();
            String strDate = sdfDate.format(now);

            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
            String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("mobileNumber", phoneNo)
                    .addFormDataPart("name", name)
                    .addFormDataPart("description", body)
                    .addFormDataPart("createdAt", strDate)
                    .addFormDataPart("photo", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .build();

            Request request = new Request.Builder()
                    .url(ApiConstants.Url.SPIDER_API + ApiConstants.Module.UTILITY_MODULE + "news/add")
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String jsonResponse = "" + response.body().string();

            HttpResponseObject mHttpResponseObject = new HttpResponseObject();
            mHttpResponseObject.setStatus(response.code());
            mHttpResponseObject.setApiCommand(API_COMMAND);
            mHttpResponseObject.setJsonString(jsonResponse);
            return mHttpResponseObject;

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}