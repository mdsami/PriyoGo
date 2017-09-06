package com.priyo.go.Fragments.Contact;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.priyo.go.Adapters.StrangerAllAdapter;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.Friend.StrangerNode;
import com.priyo.go.Model.api.graph.response.StrangerListResponse;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactStrangerFragment extends Fragment implements HttpResponseListener {

    public static List<StrangerNode> mStrangerList = new ArrayList<StrangerNode>();
    private static String mobileNumber;
    private SharedPreferences pref;
    private RecyclerView recyclerView;
    private TextView whoElse, notFound;
    private ProgressDialog mProgressDialog;
    private HttpRequestGetAsyncTask mStrangerTask = null;
    private StrangerListResponse mStrangerResponse;
    private String accessToken;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stranger_all, container, false);
        initViews(v);

        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Fetching People You May Know...");

        pref = getContext().getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        mobileNumber = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        accessToken = pref.getString(Constants.ACCESS_TOKEN_KEY, "");

        mProgressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.Parameter.PHONE_NUMBER_PARAMETER, mobileNumber);
        mStrangerTask = new HttpRequestGetAsyncTask(ApiConstants.Command.STRANGER_LIST_FETCH,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE +
                        ApiConstants.EndPoint.URL_STRANGERS_CONTACT_ENDPOINT, getContext(), params, this);
        mStrangerTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        mStrangerTask.mHttpResponseListener = this;
        mStrangerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return v;
    }

    private void initViews(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view);
        whoElse = (TextView) v.findViewById(R.id.who_else);
        notFound = (TextView) v.findViewById(R.id.no_contact);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void httpResponseReceiver(HttpResponseObject result) {

        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            mProgressDialog.dismiss();
            mStrangerTask = null;
            if (getContext() != null)
                Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();

        switch (result.getApiCommand()) {

            case ApiConstants.Command.STRANGER_LIST_FETCH:
                try {
                    String message = result.getJsonString();

                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK && !message.equals("this person knows everyone")) {
                        mStrangerResponse = gson.fromJson(result.getJsonString(), StrangerListResponse.class);
                        mStrangerList = mStrangerResponse.getStrangers();
                        StrangerAllAdapter adapter = new StrangerAllAdapter(getContext(), mStrangerList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        whoElse.setVisibility(View.INVISIBLE);
                        notFound.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (getContext() != null)
                        Toast.makeText(getContext(), R.string.failed_msg_common, Toast.LENGTH_LONG).show();
                }

                mProgressDialog.dismiss();
                mStrangerTask = null;

                break;
        }


    }

}

