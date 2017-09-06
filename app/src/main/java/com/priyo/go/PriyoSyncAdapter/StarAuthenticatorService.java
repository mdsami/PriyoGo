package com.priyo.go.PriyoSyncAdapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StarAuthenticatorService extends Service {
    private StarAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new StarAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
