package com.priyo.go.PriyoContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class NewsProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NewsDbHelper mOpenHelper;

    private static final int NEWS_C = 100;
    private static final int NEWS_WITH_ID = 101;
//    private static final int NEWS_WITH_CAT = 102;
//    private static final int NEWS_WITH_ID_AND_CAT = 105;

    private static final int CAT_C = 103;
    private static final int CAT_WITH_ID = 104;

    private static final int FAV_NEWS_C = 106;
    private static final int FAV_NEWS_WITH_ID = 107;
//    private static final int FAV_NEWS_WITH_CAT = 108;
//    private static final int FAV_NEWS_WITH_ID_AND_CAT = 109;


    private static final SQLiteQueryBuilder sNewsQueryBuilder;
    private static final SQLiteQueryBuilder sFavNewsQueryBuilder;
    private static final SQLiteQueryBuilder sCatQueryBuilder;

    static{
        sNewsQueryBuilder = new SQLiteQueryBuilder();
        sNewsQueryBuilder.setTables(
                NewsContract.NewsEntry.TABLE_NAME);

        sFavNewsQueryBuilder = new SQLiteQueryBuilder();
        sFavNewsQueryBuilder.setTables(
                NewsContract.FavEntry.TABLE_NAME);

        sCatQueryBuilder = new SQLiteQueryBuilder();
        sCatQueryBuilder.setTables(
                NewsContract.CategoryEntry.TABLE_NAME);
    }

    private static final String sNewsIdSelection =
            NewsContract.NewsEntry.TABLE_NAME+
                    "." + NewsContract.NewsEntry.COLUMN_NEWS_ID + " = ? ";

    private static final String sFavNewsIdSelection =
            NewsContract.FavEntry.TABLE_NAME+
                    "." + NewsContract.FavEntry.COLUMN_NEWS_ID + " = ? ";

    private static final String sCatIdSelection =
            NewsContract.CategoryEntry.TABLE_NAME+
                    "." + NewsContract.CategoryEntry.COLUMN_CAT_ID + " = ? ";


    private Cursor getNewsId(Uri uri, String[] projection, String sortOrder) {
        String id = NewsContract.NewsEntry.getIdFromUri(uri);

        String[] selectionArgs;
        String selection;
        selection = sNewsIdSelection;
        selectionArgs = new String[]{id};


        return sNewsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }




    private Cursor getFavNewsId(Uri uri, String[] projection, String sortOrder) {
        String id = NewsContract.FavEntry.getIdFromUri(uri);

        String[] selectionArgs;
        String selection;
        selection = sFavNewsIdSelection;
        selectionArgs = new String[]{id};


        return sFavNewsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    private Cursor getCatId(Uri uri, String[] projection, String sortOrder) {
        String id = NewsContract.CategoryEntry.getIdFromUri(uri);

        String[] selectionArgs;
        String selection;
        selection = sCatIdSelection;
        selectionArgs = new String[]{id};


        return sCatQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, NewsContract.PATH_NEWS, NEWS_C);
        matcher.addURI(authority, NewsContract.PATH_NEWS + "/*", NEWS_WITH_ID);

        matcher.addURI(authority, NewsContract.PATH_FAV, FAV_NEWS_C);
        matcher.addURI(authority, NewsContract.PATH_FAV + "/*", FAV_NEWS_WITH_ID);

        matcher.addURI(authority, NewsContract.PATH_CAT, CAT_C);
        matcher.addURI(authority, NewsContract.PATH_CAT + "/*", CAT_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case NEWS_WITH_ID: {
                retCursor = getNewsId(uri, projection, sortOrder);
                break;
            }


            case NEWS_C: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            case FAV_NEWS_WITH_ID: {
                retCursor = getFavNewsId(uri, projection, sortOrder);
                break;
            }


            case FAV_NEWS_C: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.FavEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case CAT_WITH_ID: {
                retCursor = getCatId(uri, projection, sortOrder);
                break;
            }

            case CAT_C: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case NEWS_WITH_ID:
                return NewsContract.NewsEntry.CONTENT_TYPE;
            case NEWS_C:
                return NewsContract.NewsEntry.CONTENT_TYPE;

            case FAV_NEWS_WITH_ID:
                return NewsContract.FavEntry.CONTENT_TYPE;
            case FAV_NEWS_C:
                return NewsContract.FavEntry.CONTENT_TYPE;

            case CAT_WITH_ID:
                return NewsContract.CategoryEntry.CONTENT_TYPE;

            case CAT_C:
                return NewsContract.CategoryEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        System.out.println("MKLMKL  *( "+match);
        Uri returnUri;

        switch (match) {
            case NEWS_C: {
                long _id =-1;

                try {
                    _id = db.insertWithOnConflict(NewsContract.NewsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    Log.e("xyz", "Error inserting " + _id);
                    //return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
                } catch (SQLException e) {
                    Log.e("xyz", "Error inserting " + e.toString());
                    _id=-1;
                }

                //long _id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = NewsContract.NewsEntry.buildNewsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case FAV_NEWS_C: {
                long _id = db.insert(NewsContract.FavEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = NewsContract.FavEntry.buildNewsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case CAT_C: {
                long _id = db.insert(NewsContract.CategoryEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = NewsContract.CategoryEntry.buildCatUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case NEWS_C:
                rowsDeleted = db.delete(
                        NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case FAV_NEWS_C:
                rowsDeleted = db.delete(
                        NewsContract.FavEntry.TABLE_NAME, selection, selectionArgs);
                break;


            case CAT_C:
                rowsDeleted = db.delete(
                        NewsContract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case NEWS_C:
                rowsUpdated = db.update(NewsContract.NewsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case FAV_NEWS_C:
                rowsUpdated = db.update(NewsContract.FavEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case CAT_C:
                rowsUpdated = db.update(NewsContract.CategoryEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS_C:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;


            case FAV_NEWS_C:
                db.beginTransaction();
                int returnCount2 = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(NewsContract.FavEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount2++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount2;

            case CAT_C:
                db.beginTransaction();
                int returnCount1 = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(NewsContract.CategoryEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount1++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount1;

            default:
                return super.bulkInsert(uri, values);
        }
    }

}