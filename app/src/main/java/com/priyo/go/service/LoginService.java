package com.priyo.go.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Activities.News.PriyoNewsTabActivity;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.api.profile.response.LoginResponse;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;
import com.priyo.go.view.activity.signup.SignupActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by sajid.shahriar on 4/20/17.
 */

public class LoginService extends Service implements HttpResponseListener {

    private SharedPreferences sharedPreferences;
    private HttpRequestGetAsyncTask loginTask;


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

        if (!TextUtils.isEmpty(deviceToken)) {
            loginTask = new HttpRequestGetAsyncTask(ApiConstants.Command.LOGIN, ApiConstants.Url.SPIDER_API + ApiConstants.Module.PROFILE_MODULE + ApiConstants.EndPoint.LOG_IN_ENDPOINT, this, this);
            loginTask.addHeader("Device-Token", deviceToken);
            if (Utilities.isConnectionAvailable(this))
                loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            logout();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void httpResponseReceiver(@Nullable HttpResponseObject result) {
        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_BAD_REQUEST || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_UNAUTHORIZED) {
            logout();
        } else if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
            Gson gson = new GsonBuilder().create();
            switch (result.getApiCommand()) {
                case ApiConstants.Command.LOGIN:
                    LoginResponse loginResponse = gson.fromJson(result.getJsonString(), LoginResponse.class);
                    sharedPreferences.edit().putString(Constants.ACCESS_TOKEN_KEY, loginResponse.getToken()).apply();
                    sharedPreferences.edit().putLong(Constants.LAST_UPDATE_TIME_KEY, System.currentTimeMillis()).apply();
                    Intent i = new Intent(this, PriyoNewsTabActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    stopSelf();
                    break;
            }
        }
    }

    private void logout() {
        if (getApplicationContext() != null)
            Toast.makeText(getApplicationContext(), "Please login", Toast.LENGTH_SHORT).show();
        sharedPreferences.edit().clear().apply();
        Intent loginIntent = new Intent(getApplication(), SignupActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        stopSelf();
    }
}
