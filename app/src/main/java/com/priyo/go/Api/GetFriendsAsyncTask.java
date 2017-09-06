package com.priyo.go.Api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.priyo.go.Model.Friend.FriendNode;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetFriendsAsyncTask extends HttpRequestGetAsyncTask implements HttpResponseListener {

    public GetFriendsAsyncTask(Context context, String mobileNumber) {
        super("COMMAND_GET_FRIENDS", ApiConstants.Url.SPIDER_API+ApiConstants.Module.GRAPH_MODULE + Constants.URL_ADD_FRIENDS+"?phoneNumber="+ URLEncoder.encode(mobileNumber), context);

        SharedPreferences pref = context.getSharedPreferences(Constants.ApplicationTag, context.MODE_PRIVATE);
        String accessToken = pref.getString(Constants.ACCESS_TOKEN_KEY, "");
        System.out.println(" Token " + accessToken );
        addHeader(ApiConstants.Header.TOKEN, accessToken);
        mHttpResponseListener = this;

    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {
        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            if (getContext() != null) {
                return;
            }
        }

        try {
            if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                List<FriendNode> mGetAllContactsResponse= new ArrayList<>();

                if(!result.getJsonString().equals("[]")) {
                    Gson gson = new Gson();
                    FriendNode[] friendNodeArray = gson.fromJson(result.getJsonString(), FriendNode[].class);
                    mGetAllContactsResponse = Arrays.asList(friendNodeArray);
                }
                SyncContactsAsyncTask syncContactsAsyncTask = new SyncContactsAsyncTask(getContext(), mGetAllContactsResponse);
                syncContactsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
