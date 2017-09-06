package com.priyo.go.Utilities;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.priyo.go.PriyoSyncAdapter.StarSyncAdapter;
import com.priyo.people.database.model.DaoMaster;
import com.priyo.people.database.model.DaoSession;

import org.greenrobot.greendao.database.Database;


public class MyApplication extends MultiDexApplication {

    private DaoSession daoSession;
    private DaoMaster.DevOpenHelper helper;

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(100 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().subscribeToTopic("general");
        StarSyncAdapter.initializeSyncAdapter(getApplicationContext());
        StarSyncAdapter.syncImmediately(getApplicationContext());

        initImageLoader(getApplicationContext());

        if (helper == null) {
            helper = new DaoMaster.DevOpenHelper(this, Constants.DATABASE_NAME);
            Database database = helper.getWritableDb();
            daoSession = new DaoMaster(database).newSession();
        }


    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
