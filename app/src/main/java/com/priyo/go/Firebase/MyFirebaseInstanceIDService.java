package com.priyo.go.Firebase;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.priyo.go.Utilities.Constants;

/**
 * Created by MUKUL on 1/22/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.ApplicationTag, MODE_PRIVATE);
        sharedPreferences.edit().putString(Constants.FCM_TOKEN_KEY, refreshedToken).apply();
        sharedPreferences.edit().putBoolean(Constants.IS_FCM_TOKEN_UPDATED_KEY, true).apply();

    }
    // [END refresh_token]
}