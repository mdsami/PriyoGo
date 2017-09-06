package com.priyo.go.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
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
import com.priyo.go.controller.adapter.UpcomingBirthdayListController;
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

public class BirthdaysActivity extends AppCompatActivity implements HttpResponseListener, RecyclerView.OnItemClickListener {

    public static final int CALENDAR_READ_REQUEST_CODE = 1001;
    public static final int CALENDAR_WRITE_REQUEST_CODE = 1002;
    public static final int CREATE_BIRTHDAY_EVENT_REQUEST_CODE = 1003;
    private static final String TAG = BirthdaysActivity.class.getSimpleName();
    private final Gson gson = new GsonBuilder().setDateFormat("MMM dd").create();
    private final Date dateToday = new Date(System.currentTimeMillis());
    private final SimpleDateFormat todayDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private SharedPreferences sharedPreferences;

    private HttpRequestGetAsyncTask getUpcomingBirthdayTask = null;
    private List<BirthdayNode> birthdayNodeList;

    private ProgressDialog progressDialog;
    private RecyclerView upcomingBirthdaysRecyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private UpcomingBirthdayListController upcomingBirthdayListController;

    private String userMobileNumber;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthdays);

        initViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = this.getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        userMobileNumber = sharedPreferences.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");

        fetchBirthdayList();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViews() {
        upcomingBirthdaysRecyclerView = (RecyclerView) findViewById(R.id.upcoming_birthdays_recycler_view);
        upcomingBirthdaysRecyclerView.setAsListType(RecyclerView.VERTICAL_LAYOUT);
        upcomingBirthdaysRecyclerView.setRequestDisallowInterceptTouchEvent(true);
        upcomingBirthdaysRecyclerView.setOnItemClickListener(this);

        birthdayNodeList = new ArrayList<>();
        upcomingBirthdayListController = new UpcomingBirthdayListController(this, birthdayNodeList);

        recyclerViewAdapter = new RecyclerViewAdapter(upcomingBirthdayListController);
        upcomingBirthdaysRecyclerView.setAdapter(recyclerViewAdapter);

        progressDialog = new ProgressDialog(BirthdaysActivity.this);
        progressDialog.setMessage("Fetching Birthday List" + "...");
    }

    private void fetchBirthdayList() {

        HashMap<String, String> params = new HashMap<>();
        params.put(ApiConstants.Parameter.PHONE_NUMBER_PARAMETER, userMobileNumber);
        params.put(ApiConstants.Parameter.COUNT_PARAMETER, "100");
        params.put(ApiConstants.Parameter.DATE_PARAMETER, todayDateFormat.format(dateToday));
        progressDialog.show();


        getUpcomingBirthdayTask = new HttpRequestGetAsyncTask(ApiConstants.Command.UPCOMING_BIRTHDAY_LIST_FETCH,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + ApiConstants.EndPoint.BIRTHDAYS_ENDPOINT,
                this, params, this);
        getUpcomingBirthdayTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        getUpcomingBirthdayTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREATE_BIRTHDAY_EVENT_REQUEST_CODE:
                recyclerViewAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CALENDAR_READ_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    upcomingBirthdayListController.setHasCalendarReadPermission(false);
                    recyclerViewAdapter.notifyDataSetChanged();
                    Toast.makeText(this, R.string.calendar_read_permission_needed, Toast.LENGTH_SHORT).show();
                } else {
                    upcomingBirthdayListController.setHasCalendarReadPermission(true);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                break;
            case CALENDAR_WRITE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.cannot_perform_add_event, Toast.LENGTH_SHORT).show();
                } else {
                    upcomingBirthdayListController.attemptAddBirthdayEvent();
                }
                break;
        }
    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {
        progressDialog.dismiss();
        getUpcomingBirthdayTask = null;
        if (result == null) {
            Toast.makeText(this, R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            switch (result.getStatus()) {
                case ApiConstants.StatusCode.S_OK:
                case ApiConstants.StatusCode.S_ACCEPTED:
                    Type listType = new TypeToken<List<BirthdayNode>>() {
                    }.getType();
                    birthdayNodeList = gson.fromJson(result.getJsonString(), listType);
                    upcomingBirthdayListController.updateItemList(birthdayNodeList);
                    checkForCalenderPermission();
                    recyclerViewAdapter.notifyDataSetChanged();
                    break;
                case ApiConstants.StatusCode.S_UNAUTHORIZED:
                case ApiConstants.StatusCode.S_FORBIDDEN:
                    ApiForbiddenResponse apiForbiddenResponse = gson.fromJson(result.getJsonString(), ApiForbiddenResponse.class);
                    if (!TextUtils.isEmpty(apiForbiddenResponse.getDetail())) {
                        Toast.makeText(getApplicationContext(), apiForbiddenResponse.getDetail(), Toast.LENGTH_LONG).show();
                    } else if (!TextUtils.isEmpty(apiForbiddenResponse.getMessage())) {
                        Toast.makeText(getApplicationContext(), apiForbiddenResponse.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.unauthorized, Toast.LENGTH_LONG).show();
                    }
                    break;
                case ApiConstants.StatusCode.S_NOT_FOUND:
                case ApiConstants.StatusCode.S_SERVER_ERROR:
                    Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkForCalenderPermission() {
        if (upcomingBirthdayListController != null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                upcomingBirthdayListController.setHasCalendarReadPermission(false);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, CALENDAR_READ_REQUEST_CODE);
            } else {
                upcomingBirthdayListController.setHasCalendarReadPermission(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onItemClick(View clickedItemView, int position, long id) {
        BirthdayNode birthdayNode = upcomingBirthdayListController.getItem(position);
        Intent intent = new Intent(this, PeopleDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("nodeID", birthdayNode.getNodeID());
        intent.putExtra("hashedPhoneNumber", birthdayNode.getPhoneNumber());
        intent.putExtra("isCelebrity", birthdayNode.isCelebrity());
        startActivity(intent);
        return false;
    }
}
