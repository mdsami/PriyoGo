package com.priyo.go.PriyoSyncAdapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StarSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static StarSyncAdapter sStarSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sStarSyncAdapter == null) {
                sStarSyncAdapter = new StarSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sStarSyncAdapter.getSyncAdapterBinder();
    }
}
