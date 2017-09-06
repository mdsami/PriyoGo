package com.priyo.go.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.priyo.go.Api.HttpRequestPostAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.api.profile.request.FcmTokenUpdateRequest;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;

/**
 * Created by sajid.shahriar on 4/20/17.
 */

public class FCMUpdateService extends Service implements HttpResponseListener {

    private SharedPreferences sharedPreferences;
    private HttpRequestPostAsyncTask fcmTokenUpdateProfileTask;
    private HttpRequestPostAsyncTask fcmTokenUpdateUtilityTask;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (sharedPreferences == null)
            sharedPreferences = getApplicationContext().getSharedPreferences(Constants.ApplicationTag, MODE_PRIVATE);
        final String deviceToken = sharedPreferences.getString(Constants.DEVICE_TOKEN_KEY, "");
        final String fcmToken = sharedPreferences.getString(Constants.FCM_TOKEN_KEY, "");
        final String accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");
        final String mobileNumber = sharedPreferences.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        FcmTokenUpdateRequest fcmTokenUpdateRequest = new FcmTokenUpdateRequest();
        fcmTokenUpdateRequest.setFcmToken(fcmToken);
        Gson gson = new Gson();
        String jsonBodyProfile = gson.toJson(fcmTokenUpdateRequest);
        if (!TextUtils.isEmpty(deviceToken)) {
            fcmTokenUpdateProfileTask = new HttpRequestPostAsyncTask(ApiConstants.Command.FCM_TOKEN_UPDATE, ApiConstants.Url.SPIDER_API + ApiConstants.Module.PROFILE_MODULE + String.format(ApiConstants.EndPoint.FCM_TOKEN_UPDATE_ENDPOINT, mobileNumber), jsonBodyProfile, this);
            fcmTokenUpdateProfileTask.addHeader("Device-Token", deviceToken);
            fcmTokenUpdateProfileTask.addHeader("Access-Token", accessToken);
            fcmTokenUpdateProfileTask.mHttpResponseListener = this;

            String jsonBodyUtility = "{\n" +
                    " \"registrationToken\": \"" + fcmToken + "\"" +
                    "}";
            fcmTokenUpdateUtilityTask = new HttpRequestPostAsyncTask(ApiConstants.Command.NOTIFICATION_REGISTER,
                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.UTILITY_MODULE + ApiConstants.EndPoint.NOTIFICATION_REGISTER_ENDPOINT, jsonBodyUtility, getApplicationContext());
            fcmTokenUpdateUtilityTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
            fcmTokenUpdateUtilityTask.mHttpResponseListener = this;

            if (Utilities.isConnectionAvailable(this)) {
                fcmTokenUpdateProfileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                fcmTokenUpdateUtilityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else
                stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void httpResponseReceiver(@Nullable HttpResponseObject result) {
        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_BAD_REQUEST || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_UNAUTHORIZED) {
        } else if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
            switch (result.getApiCommand()) {
                case ApiConstants.Command.FCM_TOKEN_UPDATE:
                    sharedPreferences.edit().putBoolean(Constants.IS_FCM_TOKEN_UPDATED_KEY, false).apply();
                    stopSelf();
                    break;
            }
        }
    }
}
