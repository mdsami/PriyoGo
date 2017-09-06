package com.priyo.go.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.priyo.go.Activities.News.PriyoNewsTabActivity;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.service.FCMUpdateService;
import com.priyo.go.service.LoginService;

public class LauncherActivity extends AppCompatActivity {

    private static final long time = 1000 * 60 * 60 * 6;
    private SharedPreferences pref;
    private String accessToken = "";
    private String deviceToken = "";
    private boolean isFcmTokenUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Constants.options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnLoading(R.mipmap.no_image)
                .showImageOnFail(R.mipmap.no_image)
                //.displayer(new RoundedBitmapDisplayer(20))
                .build();

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Intent i;
        pref = getSharedPreferences(Constants.ApplicationTag, MODE_PRIVATE);
        accessToken = pref.getString(Constants.ACCESS_TOKEN_KEY, "");
        deviceToken = pref.getString(Constants.DEVICE_TOKEN_KEY, "");
        isFcmTokenUpdate = pref.getBoolean(Constants.IS_FCM_TOKEN_UPDATED_KEY, false);

        long lastUpdateTime = pref.getLong(Constants.LAST_UPDATE_TIME_KEY, 0);
        if (lastUpdateTime != 0 && !TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(deviceToken)) {
            if (System.currentTimeMillis() - lastUpdateTime > time) {
                setContentView(R.layout.layout_splash);
                startService(new Intent(getApplication(), LoginService.class));
            } else {
                i = new Intent(LauncherActivity.this, PriyoNewsTabActivity.class);
                startActivity(i);
                if (isFcmTokenUpdate) {
                    startService(new Intent(this, FCMUpdateService.class));
                }
                finish();
            }

        } else {
            i = new Intent(LauncherActivity.this, TourActivity.class);
            i.putExtra(Constants.TARGET_FRAGMENT, Constants.SIGN_IN);
            startActivity(i);
            finish();
        }


    }
}
