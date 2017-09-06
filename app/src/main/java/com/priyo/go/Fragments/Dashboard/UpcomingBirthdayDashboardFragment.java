package com.priyo.go.Fragments.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.priyo.go.Api.ApiForbiddenResponse;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.Friend.BirthdayNode;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.controller.adapter.dashboard.DashboardBirthdayListController;
import com.priyo.go.view.activity.BirthdaysActivity;
import com.priyo.go.view.activity.people.PeopleDetailsActivity;
import com.priyo.go.view.adapter.RecyclerViewAdapter;
import com.priyo.go.view.widget.RecyclerView;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.priyo.go.view.widget.RecyclerView.HORIZONTAL_LAYOUT;

/**
 * Created by sajid.shahriar on 5/1/17.
 */

public class UpcomingBirthdayDashboardFragment extends Fragment implements View.OnClickListener, RecyclerView.OnItemClickListener, HttpResponseListener {

    private static final String TAG = NewBusinessDashboardFragment.class.getSimpleName();

    private final Gson gson = new GsonBuilder().setDateFormat("MMM dd").create();

    private final Date dateToday = new Date(System.currentTimeMillis());
    private final SimpleDateFormat todayDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private HttpRequestGetAsyncTask getUpcomingBirthdayTask = null;
    private List<BirthdayNode> birthdayNodeList;

    private View rootView;
    private Button showMoreButton;
    private ProgressBar progressBar;
    private RecyclerView upcomingBirthdaysRecyclerView;

    private DashboardBirthdayListController dashboardBirthdayListController;
    private RecyclerViewAdapter recyclerViewAdapter;
    private SharedPreferences preferences;
    private String userMobileNumber;
    private String accessToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(@Nullable Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        userMobileNumber = preferences.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        accessToken = preferences.getString(Constants.ACCESS_TOKEN_KEY, "");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)");
        // Inflate the layout for this fragment
        if (rootView == null)
            rootView = inflater.inflate(R.layout.fragment_upcoming_birthday_dashboard, container, false);

        showMoreButton = (Button) rootView.findViewById(R.id.show_more_button);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        upcomingBirthdaysRecyclerView = (RecyclerView) rootView.findViewById(R.id.upcoming_birthdays_recycler_view);
        upcomingBirthdaysRecyclerView.setOnItemClickListener(this);
        upcomingBirthdaysRecyclerView.setAsListType(HORIZONTAL_LAYOUT);

        birthdayNodeList = new ArrayList<>();
        dashboardBirthdayListController = new DashboardBirthdayListController(getActivity(), birthdayNodeList);
        recyclerViewAdapter = new RecyclerViewAdapter(dashboardBirthdayListController);
        upcomingBirthdaysRecyclerView.setAdapter(recyclerViewAdapter);
        progressBar.setVisibility(View.GONE);

        showMoreButton.setOnClickListener(this);


        fetchUpcomingBirthdayList();

        return rootView;
    }

    private void fetchUpcomingBirthdayList() {
        Log.d(TAG, "fetchUpcomingBirthdayList()");
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.Parameter.PHONE_NUMBER_PARAMETER, userMobileNumber);
        params.put(ApiConstants.Parameter.COUNT_PARAMETER, "10");
        params.put(ApiConstants.Parameter.DATE_PARAMETER, todayDateFormat.format(dateToday));

        progressBar.setVisibility(View.VISIBLE);
        getUpcomingBirthdayTask = new HttpRequestGetAsyncTask(ApiConstants.Command.UPCOMING_BIRTHDAY_LIST_FETCH,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + ApiConstants.EndPoint.BIRTHDAYS_ENDPOINT,
                getActivity(), params, this);
        getUpcomingBirthdayTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        getUpcomingBirthdayTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public boolean onItemClick(View clickedItemView, int position, long id) {
        Log.d(TAG, "onItemClick(View clickedItemView, int position, long id)");
        if (position >= 0) {
            BirthdayNode birthdayNode = dashboardBirthdayListController.getItem(position);
            Intent intent = new Intent(getActivity(), PeopleDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("nodeID", birthdayNode.getNodeID());
            intent.putExtra("hashedPhoneNumber", birthdayNode.getPhoneNumber());
            intent.putExtra("isCelebrity", birthdayNode.isCelebrity());
            startActivity(intent);
            return true;
        } else
            return false;
    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {
        Log.d(TAG, "httpResponseReceiver(HttpResponseObject result)");
        getUpcomingBirthdayTask = null;
        if (result == null) {
            Log.d(TAG, "Null Response");
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (result.getStatus()) {
            case ApiConstants.StatusCode.S_OK:
            case ApiConstants.StatusCode.S_ACCEPTED:
                progressBar.setVisibility(View.GONE);
                rootView.setVisibility(View.VISIBLE);
                Type listType = new TypeToken<List<BirthdayNode>>() {
                }.getType();
                try {
                    birthdayNodeList = gson.fromJson(result.getJsonString(), listType);
                } catch (Exception e) {
                    rootView.setVisibility(View.GONE);
                    return;
                }
                dashboardBirthdayListController.updateItemList(birthdayNodeList);
                recyclerViewAdapter.notifyDataSetChanged();
                break;
            case ApiConstants.StatusCode.S_UNAUTHORIZED:
            case ApiConstants.StatusCode.S_FORBIDDEN:
                progressBar.setVisibility(View.GONE);
                rootView.setVisibility(View.GONE);
                ApiForbiddenResponse apiForbiddenResponse = gson.fromJson(result.getJsonString(), ApiForbiddenResponse.class);
                if (!TextUtils.isEmpty(apiForbiddenResponse.getDetail())) {
                    Toast.makeText(getContext(), apiForbiddenResponse.getDetail(), Toast.LENGTH_LONG).show();
                } else if (!TextUtils.isEmpty(apiForbiddenResponse.getMessage())) {
                    Toast.makeText(getContext(), apiForbiddenResponse.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), R.string.unauthorized, Toast.LENGTH_LONG).show();
                }
                break;
            case ApiConstants.StatusCode.S_NOT_FOUND:
            case ApiConstants.StatusCode.S_SERVER_ERROR:
                progressBar.setVisibility(View.GONE);
                rootView.setVisibility(View.GONE);
                Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick(View view)");
        switch (view.getId()) {
            case R.id.show_more_button:
                Intent i = new Intent(getContext(), BirthdaysActivity.class);
                startActivity(i);
                break;
        }
    }

}
