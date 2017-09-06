package com.priyo.go.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.priyo.go.Model.Friend.FriendNode;
import com.priyo.go.Model.Friend.PushNode;

import java.util.ArrayList;
import java.util.List;


public class DataHelper {

    private static final int DATABASE_VERSION = 211;

    private final Context context;
    private static DataHelper instance = null;
    private static DataBaseOpenHelper dOpenHelper;

    private DataHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized DataHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataHelper(context);
            dOpenHelper = new DataBaseOpenHelper(context, DBConstants.DB_IPAY,
                    DATABASE_VERSION);
        }
        return instance;
    }

    public void closeDbOpenHelper() {
        if (dOpenHelper != null) dOpenHelper.close();
        instance = null;
    }

    public void createFriends(List<FriendNode> friendNodes) {

        System.out.println("contact "+"0"+friendNodes.size());
        if (friendNodes != null && !friendNodes.isEmpty()) {

            SQLiteDatabase db = dOpenHelper.getWritableDatabase();
            db.beginTransaction();

            try {
                for (FriendNode friendNode : friendNodes) {
                    ContentValues values = new ContentValues();
                    values.put(DBConstants.KEY_MOBILE_NUMBER, friendNode.getMobileNumber());
                    values.put(DBConstants.KEY_NAME, friendNode.getContactName());
                    values.put(DBConstants.KEY_PEOPLE_NAME, friendNode.getPeopleName());
                    values.put(DBConstants.KEY_IS_MEMBER, friendNode.isPeople() ?
                            DBConstants.IPAY_MEMBER : DBConstants.NOT_IPAY_MEMBER);

                    db.insertWithOnConflict(DBConstants.DB_TABLE_FRIENDS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            context.getContentResolver().notifyChange(DBConstants.DB_TABLE_FRIENDS_URI, null);

            Log.i("Friends", "Inserted into the database");
        }
    }

    public Cursor searchFriends(String query) {
        return searchFriends(query, false, false, false);
    }

    public Cursor searchFriends(String query, boolean memberOnly, boolean businessMemberOnly, boolean verifiedOnly) {
        return searchFriends(query, memberOnly, businessMemberOnly, false, verifiedOnly, false, false, null);
    }

    public Cursor searchFriends(String query, boolean memberOnly, boolean businessMemberOnly, boolean nonMemberOnly,
                                boolean verifiedOnly, boolean invitedOnly, boolean nonInvitedOnly, List<String> invitees) {
        Cursor cursor = null;

        try {
            SQLiteDatabase db = dOpenHelper.getReadableDatabase();

            String queryString = "SELECT * FROM " + DBConstants.DB_TABLE_FRIENDS
                    + " WHERE (" + DBConstants.KEY_NAME + " LIKE '%" + query + "%'"
                    + " OR " + DBConstants.KEY_MOBILE_NUMBER + " LIKE '%" + query + "%'"
                    + " OR " + DBConstants.KEY_PEOPLE_NAME + " LIKE '%" + query + "%')";


            // Get iPay Users
            if (memberOnly)
                queryString += " AND " + DBConstants.KEY_IS_MEMBER + " = " + DBConstants.IPAY_MEMBER;


            // Get Non-iPay Users
            if (nonMemberOnly)
                queryString += " AND " + DBConstants.KEY_IS_MEMBER + " != " + DBConstants.IPAY_MEMBER;

            if (invitees != null) {
                List<String> quotedInvitees = new ArrayList<>();
                for (String invitee : invitees) {
                    quotedInvitees.add("'" + invitee + "'");
                }

                String inviteeListStr = "(" + TextUtils.join(", ", quotedInvitees) + ")";

                if (invitedOnly) {
                    // Get invited users
                    queryString += " AND " + DBConstants.KEY_MOBILE_NUMBER + " IN " + inviteeListStr;
                }

                if (nonInvitedOnly) {
                    // Get invited users
                    queryString += " AND " + DBConstants.KEY_MOBILE_NUMBER + " NOT IN " + inviteeListStr;
                }
            }

            // If original name exists, then user original name as the sorting parameter.
            // Otherwise use normal name as the sorting parameter.
            queryString += " ORDER BY CASE "
                    + "WHEN (" + DBConstants.KEY_NAME + " IS NULL OR " + DBConstants.KEY_PEOPLE_NAME + " = '')"
                    + " THEN " + DBConstants.KEY_NAME
                    + " ELSE "
                    + DBConstants.KEY_NAME + " END COLLATE NOCASE";

            Log.w("Query", queryString);

            cursor = db.rawQuery(queryString, null);

            if (cursor != null) {
                cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursor;
    }

    private List<FriendNode> getFriendList(String query, boolean memberOnly, boolean businessMemberOnly, boolean verifiedOnly) {
        Cursor cursor = searchFriends(query, memberOnly, businessMemberOnly, verifiedOnly);
        List<FriendNode> friends = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBConstants.KEY_NAME);
            int originalNameIndex = cursor.getColumnIndex(DBConstants.KEY_PEOPLE_NAME);
            int mobileNumberIndex = cursor.getColumnIndex(DBConstants.KEY_MOBILE_NUMBER);
            int updateTimeIndex = cursor.getColumnIndex(DBConstants.KEY_UPDATE_TIME);
            int isMemberIndex = cursor.getColumnIndex(DBConstants.KEY_IS_MEMBER);

            do {
                String name = cursor.getString(nameIndex);
                String originalName = cursor.getString(originalNameIndex);
                String mobileNumber = cursor.getString(mobileNumberIndex);
                long updateTime = cursor.getLong(updateTimeIndex);
                int isMember = cursor.getInt(isMemberIndex);
                boolean te;
                if(isMember==1)
                    te=true;
                else
                    te=false;

                FriendNode friend = new FriendNode(name, mobileNumber, te,originalName);
                friends.add(friend);
            } while (cursor.moveToNext());
        }

        return friends;
    }

    public List<FriendNode> getFriendList() {
        return getFriendList("", false, false, false);
    }


    public void createPushEvent(PushNode friendNodes) {

        if (friendNodes != null && !friendNodes.getTitle().equals("") && !friendNodes.getMsg().equals("")) {

            SQLiteDatabase db = dOpenHelper.getWritableDatabase();
            db.beginTransaction();

            try {
                    ContentValues values = new ContentValues();
                    values.put(DBConstants.KEY_TITLE, friendNodes.getTitle());
                    values.put(DBConstants.KEY_MSG, friendNodes.getMsg());
                    values.put(DBConstants.KEY_URL, friendNodes.getUrl());
                    values.put(DBConstants.KEY_TYPE, friendNodes.getType());
                    values.put(DBConstants.KEY_PUSH_UPDATE_TIME, friendNodes.getUpdatedAt());
                    values.put(DBConstants.KEY_IS_SEEN, friendNodes.isOpen() ?
                            DBConstants.OPEN_PUSH : DBConstants.NOT_OPEN_PUSH);

                    db.insert(DBConstants.DB_TABLE_PUSH_EVENTS, null, values);
            } catch (Exception e) {
                Log.i("Push", "Inserted into the database"+ e.toString());
                e.printStackTrace();
            }

            db.setTransactionSuccessful();
            db.endTransaction();

            context.getContentResolver().notifyChange(DBConstants.DB_TABLE_PUSH_URI, null);

            Log.i("Push", "Inserted into the database");
        }
    }

    public void updatePushEvents(PushNode pp) {

        try {
            SQLiteDatabase db = dOpenHelper.getReadableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.KEY_TITLE, pp.getTitle());

            contentValues.put(DBConstants.KEY_MSG, pp.getMsg());
            contentValues.put(DBConstants.KEY_URL, pp.getUrl());
            contentValues.put(DBConstants.KEY_TYPE, pp.getType());
            contentValues.put(DBConstants.KEY_IS_SEEN, pp.isOpen() ?
                    DBConstants.OPEN_PUSH : DBConstants.NOT_OPEN_PUSH);

            db.update(DBConstants.DB_TABLE_PUSH_EVENTS, contentValues, " _id = ?",
                    new String[] { String.valueOf(pp.getId()) });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePushEvents(String tagName, String msg, String jsonString,String type,int isOpen) {

        try {
            SQLiteDatabase db = dOpenHelper.getReadableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.KEY_TITLE, tagName);

            contentValues.put(DBConstants.KEY_MSG, msg);
            contentValues.put(DBConstants.KEY_URL, jsonString);
            contentValues.put(DBConstants.KEY_TYPE, type);
            contentValues.put(DBConstants.KEY_IS_SEEN, isOpen);

            db.replace(DBConstants.DB_TABLE_PUSH_EVENTS, null, contentValues);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PushNode getPushEvent(String tag) {
        Cursor cursor;

        try {
            SQLiteDatabase db = dOpenHelper.getReadableDatabase();

            String queryString = "SELECT * FROM " + DBConstants.DB_TABLE_PUSH_EVENTS
                    + " WHERE " + DBConstants.KEY_TITLE + " = '" + tag + "'";
            cursor = db.rawQuery(queryString, null);

            if (cursor != null && cursor.moveToFirst()) {
                String title = cursor.getString(cursor.getColumnIndex(DBConstants.KEY_TITLE));
                String msg = cursor.getString(cursor.getColumnIndex(DBConstants.KEY_MSG));
                String pushEvent = cursor.getString(cursor.getColumnIndex(DBConstants.KEY_URL));
                String type = cursor.getString(cursor.getColumnIndex(DBConstants.KEY_TYPE));
                long updatedTime = cursor.getLong(cursor.getColumnIndex(DBConstants.KEY_PUSH_UPDATE_TIME));
                int isOpen = cursor.getInt(cursor.getColumnIndex(DBConstants.KEY_IS_SEEN));



                boolean te;
                if(isOpen==1)
                    te=true;
                else
                    te=false;
                cursor.close();
                return new PushNode(title,msg,pushEvent,type,updatedTime,te);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<PushNode> getPushList() {
        Cursor cursor;
        List<PushNode> friends = new ArrayList<>();

        try {
            SQLiteDatabase db = dOpenHelper.getReadableDatabase();

            String queryString = "SELECT * FROM " + DBConstants.DB_TABLE_PUSH_EVENTS + " ORDER BY "+DBConstants.KEY_PUSH_UPDATE_TIME+" DESC";
            cursor = db.rawQuery(queryString, null);


            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("_id");
                int titleIndex = cursor.getColumnIndex(DBConstants.KEY_TITLE);
                int msgIndex = cursor.getColumnIndex(DBConstants.KEY_MSG);
                int jsonIndex = cursor.getColumnIndex(DBConstants.KEY_URL);
                int typeIndex = cursor.getColumnIndex(DBConstants.KEY_TYPE);
                int updateAtIndex = cursor.getColumnIndex(DBConstants.KEY_PUSH_UPDATE_TIME);
                int isOpenIndex = cursor.getColumnIndex(DBConstants.KEY_IS_SEEN);



                do {
                    String id = String.valueOf(cursor.getInt(idIndex));
                    String title = cursor.getString(titleIndex);
                    String msg = cursor.getString(msgIndex);
                    String json = cursor.getString(jsonIndex);
                    String type = cursor.getString(typeIndex);
                    long updateTime = cursor.getLong(updateAtIndex);
                    int isOpen = cursor.getInt(isOpenIndex);

                    System.out.println("IS OPEN "+isOpen);
                    boolean te;
                    if(isOpen==1)
                        te=true;
                    else
                        te=false;

                    PushNode friend = new PushNode(id,title,msg, json,type, updateTime,te);
                    friends.add(friend);
                } while (cursor.moveToNext());
            }

            return friends;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public int getNotOpenedPush() {
        Cursor cursor;
        try {
            SQLiteDatabase db = dOpenHelper.getReadableDatabase();

            String queryString = "SELECT * FROM " + DBConstants.DB_TABLE_PUSH_EVENTS + " WHERE "+DBConstants.KEY_IS_SEEN+" = 0";
            cursor = db.rawQuery(queryString, null);
            return cursor.getCount();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }




}