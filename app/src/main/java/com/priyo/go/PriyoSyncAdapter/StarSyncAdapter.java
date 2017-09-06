package com.priyo.go.PriyoSyncAdapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.priyo.go.Api.GetFriendsAsyncTask;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpRequestPostAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.PriyoNews.Categories.Cat;
import com.priyo.go.Model.PriyoNews.Categories.Child;
import com.priyo.go.Model.PriyoNews.PriyoNews;
import com.priyo.go.Model.api.location.response.UserLocationUpdateRequest;
import com.priyo.go.PriyoContentProvider.NewsContract;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.JSONPerser;
import com.priyo.go.Utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;


public class StarSyncAdapter extends AbstractThreadedSyncAdapter implements HttpResponseListener {
    public static final int SYNC_INTERVAL = 15 * 60;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    static final String KEY_ITEM = "item";
    static final String KEY_TITLE = "title";
    static final String KEY_DESC = "description";
    static final String KEY_COLOR = "color";
    static final String KEY_LINK = "link";
    static final String KEY_CATEGORY = "category";
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int NEWS_NOTIFICATION_ID = 3004;
    private static final String[] NOTIFY_NEWS_PROJECTION = new String[]{
            NewsContract.NewsEntry.COLUMN_NEWS_ID,
            NewsContract.NewsEntry.COLUMN_TITLE
    };
    private static final int INDEX_NEWS_ID = 0;
    private static final int INDEX_TITLE = 1;
    public final String LOG_TAG = StarSyncAdapter.class.getSimpleName();
    Context c;
    String mobileNumber = "", nodeId = "";
    SharedPreferences pref;
    private HttpRequestPostAsyncTask mRequestLocationTask = null;
    private HttpRequestGetAsyncTask mPriyoNewsTask = null;
    private HttpRequestGetAsyncTask mPriyoCatTask = null;
    private HttpRequestGetAsyncTask mBusinessCategoryTask = null;


    public StarSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        c = context;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
//        try {
//                ContentResolver.addPeriodicSync(account,
//                        authority, new Bundle(), syncInterval);
//        }catch (Exception e){
//
//
//        }
        System.out.println(Build.VERSION_CODES.KITKAT + "TESTSUNC.....  ");
        try {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        } catch (Exception e) {
            try {
                SyncRequest request = new SyncRequest.Builder().
                        syncPeriodic(syncInterval, flexTime).
                        setSyncAdapter(account, authority).build();
                ContentResolver.requestSync(request);

            } catch (Exception ew) {


            }

        }


//        SyncRequest.Builder builder =
//                (new SyncRequest.Builder()).syncPeriodic(1 * HOUR_IN_SECS, 1 * HOUR_IN_SECS);

    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if (null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        StarSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        pref = c.getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        mobileNumber = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        nodeId = pref.getString(Constants.USER_NODE_ID_KEY, "");
        String accessToken = pref.getString(Constants.ACCESS_TOKEN_KEY, "");

        try {

            String latlon = Utilities.getLongLatWithoutGPS(c);
            System.out.println("FUCV LOC " + latlon);
            String[] a = latlon.split(",");
            UserLocationUpdateRequest mLocationRequest = new UserLocationUpdateRequest(mobileNumber, pref.getString(Constants.DEVICE_TOKEN_KEY, ""), a[1], a[0]);
            Gson gson = new Gson();
            String json = gson.toJson(mLocationRequest);
            mRequestLocationTask = new HttpRequestPostAsyncTask(Constants.COMMAND_LOCATION_SEND,
                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.LOCATION_MODULE + ApiConstants.EndPoint.USER_LOCATION_UPDATE_ENDPOINT
                    , json, c);
            mRequestLocationTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
            mRequestLocationTask.mHttpResponseListener = this;
            mRequestLocationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            System.out.println("FUCV LOC " + e.toString());
            e.printStackTrace();
        }

//        try {
//            mPriyoCatTask = new HttpRequestGetAsyncTask("PRIYO_CATS", "https://api.priyo.com/api/categories/?filter[where][parentId]=null&filter[include]=children", getContext());
//            mPriyoCatTask.mHttpResponseListener = this;
//            mPriyoCatTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } catch (Exception e) {
//            Log.e(LOG_TAG, e.getMessage(), e);
//            e.printStackTrace();
//        }


        try {

            if (!mobileNumber.equals("") && mobileNumber != null && !mobileNumber.equals("null")) {
                new GetFriendsAsyncTask(c, mobileNumber).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return;
        }


    }

    private void prepareCat(String jsonData) {

        ArrayList<Cat> priyo_news = new ArrayList<>();

        String id = "", title = "", description = "", titleInEnglish = "", parentId = "", weight = "", slug = "", child = "";

        ArrayList<Child> children = new ArrayList<Child>();


        JSONObject objs;

        try {
            JSONArray arr = new JSONArray(jsonData);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(arr.length());
            for (int i = 0; i < arr.length(); i++) {

                JSONObject obj = arr.getJSONObject(i);

                try {
                    id = obj.getString("id");
                } catch (Exception e) {
                }
                try {
                    title = obj.getString("title");
                } catch (Exception e) {
                }
                try {
                    description = obj.getString("description");
                } catch (Exception e) {
                }
                try {
                    weight = obj.getString("weight");
                } catch (Exception e) {
                }
                try {
                    slug = obj.getString("slug");
                } catch (Exception e) {
                }
                try {
                    titleInEnglish = obj.getString("titleInEnglish");
                } catch (Exception e) {
                }
                try {
                    parentId = obj.getString("parentId");
                } catch (Exception e) {
                }
                try {
                    child = obj.getString("children");
                } catch (Exception e) {
                }


                ContentValues catValues = new ContentValues();
                catValues.put(NewsContract.CategoryEntry.COLUMN_CAT_ID, id);
                catValues.put(NewsContract.CategoryEntry.COLUMN_CAT_TITLE, title);
                catValues.put(NewsContract.CategoryEntry.COLUMN_CAT_DESC, description);
                catValues.put(NewsContract.CategoryEntry.COLUMN_CAT_WEIGHT, weight);
                catValues.put(NewsContract.CategoryEntry.COLUMN_CAT_SLUG, slug);
                catValues.put(NewsContract.CategoryEntry.COLUMN_CAT_TITLEINENGLISH, titleInEnglish);
                catValues.put(NewsContract.CategoryEntry.COLUMN_CAT_PARENT_ID, parentId);
                catValues.put(NewsContract.CategoryEntry.COLUMN_CAT_CHILD, child);


                cVVector.add(catValues);


            }

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);


                Cursor cursor = getContext().getContentResolver().query(
                        NewsContract.CategoryEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );


                System.out.println("SUCKSSSS        " + cursor.getCount());
                if (cursor.getCount() > 1) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToPosition(i);
                        String ids = cursor.getString(cursor.getColumnIndex(NewsContract.CategoryEntry.COLUMN_CAT_ID));
                        System.out.println("SUCKSSSS        " + ids);
                        getContext().getContentResolver().delete(NewsContract.CategoryEntry.CONTENT_URI,
                                NewsContract.CategoryEntry.COLUMN_CAT_ID + " <= ?",
                                new String[]{ids});

                    }
                }

                getContext().getContentResolver().bulkInsert(NewsContract.CategoryEntry.CONTENT_URI,
                        cvArray);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {

        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            mPriyoNewsTask = null;
            mPriyoCatTask = null;
//            if (getContext() != null)
//                Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }


        Gson gson = new Gson();

        switch (result.getApiCommand()) {


            case "PRIYO_CATS":
                try {
                    prepareCat(result.getJsonString());
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {

                    } else {
                        if (getContext() != null) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    if (getContext() != null)
//                        Toast.makeText(getContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
                }
                mPriyoCatTask = null;

                break;

            case "PRIYO_NEWS":
                try {
                    ArrayList<PriyoNews> priyoNewses = new ArrayList<>();
                    priyoNewses = JSONPerser.prepareNews(result.getJsonString());
                    JSONPerser.updateNewsDatabase(getContext(), priyoNewses);

                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {

                    } else {
                        if (getContext() != null) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    if (getContext() != null)
//                        Toast.makeText(getContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
                }
                mPriyoNewsTask = null;

                break;

            case Constants.COMMAND_LOCATION_SEND:
                try {
                    String message = result.getJsonString();
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        System.out.println("FUCV LOC " + message);

                    } else {
                    }
                } catch (Exception e) {
                }
                mBusinessCategoryTask = null;

                break;
        }


    }

}
