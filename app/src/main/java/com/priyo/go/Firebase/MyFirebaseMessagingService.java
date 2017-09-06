package com.priyo.go.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.priyo.go.Activities.News.NotificationActivity;
import com.priyo.go.DatabaseHelper.DataHelper;
import com.priyo.go.Model.Friend.PushNode;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.MyApplication;
import com.priyo.people.database.model.DaoMaster;
import com.priyo.people.database.model.DaoSession;
import com.priyo.go.database.model.NearbyBusiness;
import com.priyo.people.database.model.NearbyBusinessDao;
import com.priyo.go.database.model.NearbyPeople;
import com.priyo.people.database.model.NearbyPeopleDao;

import org.greenrobot.greendao.database.Database;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private final Gson gson = new GsonBuilder().create();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        if (data != null) {
            Log.d(TAG, remoteMessage.getData().toString());
            final String type = data.get("type");

            try {
                if (type != null) {
                    switch (type) {
                        case Constants.NOTIFICATION_TYPE_NEARBY_BUSINESS:
                            addNearByBusiness(data);
                            break;
                        case Constants.NOTIFICATION_TYPE_NEARBY_PEOPLE:
                            addNearByPeople(data);
                            break;
                        case Constants.NOTIFICATION_TYPE_INTERNAL:
                        case Constants.NOTIFICATION_TYPE_EXTERNAL:
                        default:
                            showNewNewsNotification(type, data);
                            break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void showNewNewsNotification(final String type, Map<String, String> data) {
        final String url = data.get("url");
        final String title = data.get("title");
        final String message = data.get("message");
        final long updateTime = System.currentTimeMillis();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)) {
            PushNode pushNode = new PushNode(title, message, url, type, updateTime, false);
            DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
            dataHelper.createPushEvent(pushNode);
            sendNotification(title, message);
        }

    }

    private void addNearByPeople(Map<String, String> data) {
        Type listType = new TypeToken<List<NearbyPeople>>() {
        }.getType();
        final String nearbyPeopleJsonArray = data.get("nearbyPeopleList");
        if (!TextUtils.isEmpty(nearbyPeopleJsonArray)) {
            List<NearbyPeople> nearbyPeoples = gson.fromJson(nearbyPeopleJsonArray, listType);
            NearbyPeopleDao nearbyPeopleDao = getNearbyPeopleDao();
            nearbyPeopleDao.deleteAll();
            nearbyPeopleDao.saveInTx(nearbyPeoples);
        }
    }

    private void addNearByBusiness(Map<String, String> data) {
        Type listType = new TypeToken<List<NearbyBusiness>>() {
        }.getType();
        final String nearbyBusinessJsonArray = data.get("nearbyBusinessList");
        if (!TextUtils.isEmpty(nearbyBusinessJsonArray)) {
            List<NearbyBusiness> nearbyBusinesses = gson.fromJson(nearbyBusinessJsonArray, listType);
            NearbyBusinessDao nearbyBusinessDao = getNearbyBusinessDao();
            nearbyBusinessDao.deleteAll();
            nearbyBusinessDao.saveInTx(nearbyBusinesses);
        }

    }

    private NearbyPeopleDao getNearbyPeopleDao() {
        NearbyPeopleDao nearbyPeopleDao;
        if (getApplication() != null && ((MyApplication) getApplication()).getDaoSession() != null) {
            nearbyPeopleDao = ((MyApplication) getApplication()).getDaoSession().getNearbyPeopleDao();
        } else {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constants.DATABASE_NAME);
            Database database = helper.getWritableDb();
            DaoSession daoSession = new DaoMaster(database).newSession();
            nearbyPeopleDao = daoSession.getNearbyPeopleDao();
        }
        return nearbyPeopleDao;
    }

    private NearbyBusinessDao getNearbyBusinessDao() {
        NearbyBusinessDao nearbyBusinessDao;
        if (getApplication() != null && ((MyApplication) getApplication()).getDaoSession() != null) {
            nearbyBusinessDao = ((MyApplication) getApplication()).getDaoSession().getNearbyBusinessDao();
        } else {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constants.DATABASE_NAME);
            Database database = helper.getWritableDb();
            DaoSession daoSession = new DaoMaster(database).newSession();
            nearbyBusinessDao = daoSession.getNearbyBusinessDao();
        }
        return nearbyBusinessDao;
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String titleBody, String messageBody) {

        System.out.println(TAG + " " + messageBody + " " + titleBody);
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titleBody)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}