package com.priyo.go.PriyoContentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NewsDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 210;
    public static final String DATABASE_NAME = "priyo_go.db";

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_NEWS_TABLE = "CREATE TABLE " + NewsContract.NewsEntry.TABLE_NAME + " (" +
                NewsContract.NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NewsContract.NewsEntry.COLUMN_NEWS_ID + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_ANSWER + " TEXT NOT NULL, " +

                NewsContract.NewsEntry.COLUMN_SUMMERY + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_UPDATE_AT + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_CREATE_AT + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_PRIORITY + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_PUBLISHED_AT + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_SLUG + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_IMG + " TEXT NOT NULL, " +

                NewsContract.NewsEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_TAGS + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_BUSINESS + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_LOCATIONS + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_PERSONS + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_TOPICS + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_INSERT_DATE_TIME + " TEXT NOT NULL " + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_TABLE);


        final String SQL_CREATE_NEWSFAV_TABLE = "CREATE TABLE " + NewsContract.FavEntry.TABLE_NAME + " (" +
                NewsContract.FavEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NewsContract.FavEntry.COLUMN_NEWS_ID + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_ANSWER + " TEXT NOT NULL, " +

                NewsContract.FavEntry.COLUMN_SUMMERY + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_UPDATE_AT + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_CREATE_AT + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_PRIORITY + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_PUBLISHED_AT + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_SLUG + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_IMG + " TEXT NOT NULL, " +

                NewsContract.FavEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_TAGS + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_BUSINESS + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_LOCATIONS + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_PERSONS + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_TOPICS + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                NewsContract.FavEntry.COLUMN_INSERT_DATE_TIME + " TEXT NOT NULL " + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_NEWSFAV_TABLE);


        final String SQL_CREATE_NEWS_CAT_TABLE = "CREATE TABLE " + NewsContract.CategoryEntry.TABLE_NAME + " (" +
                NewsContract.CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NewsContract.CategoryEntry.COLUMN_CAT_ID + " TEXT NOT NULL, " +
                NewsContract.CategoryEntry.COLUMN_CAT_TITLE + " TEXT NOT NULL, " +
                NewsContract.CategoryEntry.COLUMN_CAT_DESC + " TEXT NOT NULL, " +
                NewsContract.CategoryEntry.COLUMN_CAT_WEIGHT + " TEXT NOT NULL, " +
                NewsContract.CategoryEntry.COLUMN_CAT_SLUG + " TEXT NOT NULL, " +
                NewsContract.CategoryEntry.COLUMN_CAT_TITLEINENGLISH + " TEXT NOT NULL, " +
                NewsContract.CategoryEntry.COLUMN_CAT_PARENT_ID + " TEXT NOT NULL, " +
                NewsContract.CategoryEntry.COLUMN_CAT_CHILD + " TEXT NOT NULL " +");";

        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_CAT_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.FavEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.CategoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
