package com.priyo.go.DatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DataBaseOpenHelper extends SQLiteOpenHelper {

    private final int newVersion;
    private final String name;

    public DataBaseOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
        this.newVersion = version;
        this.name = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createFriendsTable(db);
        createPushNotificationTable(db);
    }

    private void createFriendsTable(SQLiteDatabase db) {
        db.execSQL("create table if not exists " +
                DBConstants.DB_TABLE_FRIENDS +
                "(_id integer primary key autoincrement, " +
                DBConstants.KEY_MOBILE_NUMBER + " text unique not null, " +
                DBConstants.KEY_NAME + " text, " +
                DBConstants.KEY_PEOPLE_NAME + " text, " +
                DBConstants.KEY_UPDATE_TIME + " long, " +
                DBConstants.KEY_IS_MEMBER + " integer default 0)");
    }

    private void createPushNotificationTable(SQLiteDatabase db) {
        db.execSQL("create table if not exists "
                + DBConstants.DB_TABLE_PUSH_EVENTS
                + "(_id integer primary key autoincrement, " +
                DBConstants.KEY_TITLE + " text, " +
                DBConstants.KEY_MSG + " text, " +
                DBConstants.KEY_URL + " text, " +
                DBConstants.KEY_TYPE + " text, " +
                DBConstants.KEY_PUSH_UPDATE_TIME + " long, " +
                DBConstants.KEY_IS_SEEN + " integer default 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion <= 8) {
            db.execSQL("drop table if exists " + DBConstants.DB_TABLE_FRIENDS);
            createFriendsTable(db);
            db.execSQL("drop table if exists " + DBConstants.DB_TABLE_PUSH_EVENTS);
            createPushNotificationTable(db);
//        }
    }
}