package com.priyo.go.DatabaseHelper;

import android.net.Uri;

import com.priyo.go.Utilities.Constants;

public class DBConstants {

    public static final String TAG = "DataBaseOpenHelper";
    public static final String DB_PATH = "/data/data/" + Constants.ApplicationPackage + "/databases/";
    public static final String DB_IPAY = "PeopleDatabase";
    public static final String DB_TABLE_FRIENDS = "friends";
    public static final String DB_TABLE_PUSH_EVENTS = "push_events";
    public static final Uri DB_TABLE_FRIENDS_URI = Uri
            .parse("sqlite://" + Constants.ApplicationPackage + "/" + DB_TABLE_FRIENDS);
    public static final Uri DB_TABLE_PUSH_URI = Uri
            .parse("sqlite://" + Constants.ApplicationPackage + "/" + DB_TABLE_PUSH_EVENTS);

    // Subscriber table
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_NAME = "name";
    public static final String KEY_PEOPLE_NAME = "original_name";
    public static final String KEY_IS_MEMBER = "is_member";
    public static final String KEY_UPDATE_TIME = "update_at";

    // Push events table
    public static final String KEY_TITLE = "tag_name";
    public static final String KEY_MSG = "tag_msg";
    public static final String KEY_URL = "tag_url";
    public static final String KEY_TYPE = "tag_type";
    public static final String KEY_PUSH_UPDATE_TIME = "update_at";
    public static final String KEY_IS_SEEN = "is_seen";


    public static final int VERIFIED_USER = 1;
    public static final int NOT_VERIFIED_USER = 0;

    public static final int BUSINESS_USER = 2;

    public static final int IPAY_MEMBER = 1;
    public static final int NOT_IPAY_MEMBER = 0;

    public static final int OPEN_PUSH = 1;
    public static final int NOT_OPEN_PUSH = 0;
}
