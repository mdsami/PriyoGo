package com.priyo.go.Api;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.priyo.go.DatabaseHelper.DataHelper;
import com.priyo.go.Model.Friend.AddFriendRequest;
import com.priyo.go.Model.Friend.AddFriendResponse;
import com.priyo.go.Model.Friend.FriendNode;
import com.priyo.go.Model.Friend.InfoAddFriend;
import com.priyo.go.Model.Friend.UpdateFriendResponse;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.ContactEngine;

import java.util.ArrayList;
import java.util.List;

public class SyncContactsAsyncTask extends AsyncTask<String, Void, ContactEngine.ContactDiff> implements HttpResponseListener {

    private static boolean contactsSyncedOnce;
    private final Context context;
    private final List<FriendNode> serverContacts;
    private HttpRequestPostAsyncTask mAddFriendAsyncTask;
    private AddFriendResponse mAddFriendResponse;
    private HttpRequestPostAsyncTask mUpdateFriendAsyncTask;
    private UpdateFriendResponse mUpdateFriendResponse;

    public SyncContactsAsyncTask(Context context, List<FriendNode> serverContacts) {
        this.context = context;
        this.serverContacts = serverContacts;
    }


    @Override
    protected ContactEngine.ContactDiff doInBackground(String... params) {

        // Save the friend list fetched from the server into the database
        DataHelper dataHelper = DataHelper.getInstance(context);
        dataHelper.createFriends(serverContacts);
        // IMPORTANT: Perform this check only after saving all server contacts into the database!
        if (contactsSyncedOnce)
            return null;
        else
            contactsSyncedOnce = true;

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // Read phone contacts
            List<FriendNode> phoneContacts = ContactEngine.getAllContacts(context);
            // Calculate the difference between phone contacts and server contacts
            ContactEngine.ContactDiff contactDiff = ContactEngine.getContactDiff(phoneContacts, serverContacts);
            return contactDiff;
        } else {
            return null;
        }

    }

    @Override
    protected void onPostExecute(ContactEngine.ContactDiff contactDiff) {
        if (contactDiff != null) {
            addFriends(contactDiff.newFriends);
            updateFriends(contactDiff.updatedFriends);
        }

    }

    private void addFriends(List<FriendNode> friends) {
        if (mAddFriendAsyncTask != null) {
            return;
        }

        if (friends.isEmpty())
            return;

        List<InfoAddFriend> newFriends = new ArrayList<>();
        for (FriendNode friendNode : friends) {
            newFriends.add(new InfoAddFriend(friendNode.getMobileNumber(), friendNode.getContactName()));
        }

        AddFriendRequest addFriendRequest = new AddFriendRequest(newFriends);
        Gson gson = new Gson();
        String json = gson.toJson(addFriendRequest);


        SharedPreferences pref = context.getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        String mobileNumber = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");

        json = json.replace("{\"contacts\":", "{\"phoneNumber\" : \"" + mobileNumber + "\",\"contacts\" :");
        mAddFriendAsyncTask = new HttpRequestPostAsyncTask(Constants.COMMAND_ADD_FRIENDS,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + Constants.URL_ADD_FRIENDS, json, context, this);
        String accessToken = pref.getString(Constants.ACCESS_TOKEN_KEY, "");
        mAddFriendAsyncTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        mAddFriendAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void updateFriends(List<FriendNode> friends) {
        if (mUpdateFriendResponse != null) {
            return;
        }

        if (friends.isEmpty())
            return;

        List<InfoAddFriend> updateFriends = new ArrayList<>();
        for (FriendNode friend : friends) {
            InfoAddFriend infoUpdateFriend = new InfoAddFriend(
                    friend.getMobileNumber(), friend.getContactName());
            updateFriends.add(infoUpdateFriend);
        }

        AddFriendRequest updateFriendRequest = new AddFriendRequest(updateFriends);
        Gson gson = new Gson();
        String json = gson.toJson(updateFriendRequest);


        SharedPreferences pref = context.getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        String mobileNumber = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        String accessToken = pref.getString(Constants.ACCESS_TOKEN_KEY, "");

        json = json.replace("{\"contacts\":", "{\"mobileNumber\" : \"" + mobileNumber + "\",\"contacts\" :");
        mAddFriendAsyncTask = new HttpRequestPostAsyncTask(Constants.COMMAND_ADD_FRIENDS,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + Constants.URL_ADD_FRIENDS, json, context, this);
        mAddFriendAsyncTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        mAddFriendAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {
        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            mAddFriendAsyncTask = null;
            mUpdateFriendAsyncTask = null;
            return;
        }

        Gson gson = new Gson();

        if (result.getApiCommand().equals(Constants.COMMAND_ADD_FRIENDS)) {
            try {
                String msg = result.getJsonString();
                if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                    // Server contacts updated, download contacts again
                    SharedPreferences pref = context.getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
                    String mobileNumber = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
                    new GetFriendsAsyncTask(context, mobileNumber).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mAddFriendAsyncTask = null;

        }
    }
}