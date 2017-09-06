package com.priyo.go.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.priyo.go.Adapters.HoroscopeListAdapter;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.HoroscopeNode;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.JSONPerser;
import com.priyo.go.Utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoroscopeActivity extends AppCompatActivity implements HttpResponseListener {

    private RecyclerView mRecyclerView;
    private LayoutManager mLayoutManager;
    private TextView mEmptyContactsTextView;
    private HoroscopeListAdapter mAdapter;
    private HttpRequestGetAsyncTask mHoroscopeTask = null;
    private ProgressDialog mProgressDialog;
    private List<HoroscopeNode> mHoroscopeList = new ArrayList<>();
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharedPreferences sharedPreferences = getSharedPreferences(Constants.ApplicationTag, MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");

        initViews();
        fetchHoroscope();
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
    public void httpResponseReceiver(HttpResponseObject result) {
        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            mProgressDialog.dismiss();
            mHoroscopeTask = null;
            if (getApplicationContext() != null)
                Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (result.getApiCommand()) {
            case Constants.COMMAND_GET_HOROSCOPE:
                try {
                    String message = result.getJsonString();
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        mHoroscopeList = JSONPerser.pasrseHorscope(result.getJsonString());
                        if (mHoroscopeList.size() > 0) {
                            mAdapter = new HoroscopeListAdapter(this, mHoroscopeList);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mEmptyContactsTextView.setText("No data found!");
                        }
                    } else {
                        if (getApplicationContext() != null)
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (getApplicationContext() != null)
                        Toast.makeText(getApplicationContext(), e.toString() + R.string.failed_msg_common, Toast.LENGTH_LONG).show();
                }

                mProgressDialog.dismiss();
                mHoroscopeTask = null;

                break;
        }
    }

    private void initViews() {
        mEmptyContactsTextView = (TextView) findViewById(R.id.contact_list_empty);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.contact_list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching Today's Horoscope....");
    }

    private void fetchHoroscope() {
        mProgressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.Parameter.DATE_PARAMETER, Utilities.getTodaysdate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        mHoroscopeTask = new HttpRequestGetAsyncTask(Constants.COMMAND_GET_HOROSCOPE,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.UTILITY_MODULE + ApiConstants.EndPoint.ALL_HOROSCOPE_ENDPOINT
                , this, params, this);
        mHoroscopeTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        mHoroscopeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
