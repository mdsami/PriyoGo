package com.priyo.go.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Api.HttpRequestPostAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.api.utility.request.FeedbackRequest;
import com.priyo.go.Model.api.utility.response.FeedbackResponse;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;

import java.util.Calendar;
import java.util.TimeZone;

public class FeedbackActivity extends AppCompatActivity implements HttpResponseListener, View.OnClickListener {

    private EditText feedbackEditText;
    private Button feedbackSendButton;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

    private String mobileNumber;
    private String fullName;
    private String accessToken;
    private ProgressDialog progressDialog;
    private HttpRequestPostAsyncTask mFeedbackTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        feedbackEditText = (EditText) findViewById(R.id.feedback_edit_text);
        feedbackSendButton = (Button) findViewById(R.id.feedback_send_button);

        progressDialog = new ProgressDialog(this);

        feedbackSendButton.setOnClickListener(this);
        progressDialog.setMessage("Sending your Feedback...");
        progressDialog.setCancelable(false);

        final SharedPreferences sharedPreferences = getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        mobileNumber = sharedPreferences.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        fullName = sharedPreferences.getString(Constants.USER_FULL_NAME_KEY, "");
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");
    }


    @Override
    public void httpResponseReceiver(HttpResponseObject result) {
        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            progressDialog.dismiss();
            mFeedbackTask = null;
            if (getApplicationContext() != null)
                Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson = new Gson();

        switch (result.getApiCommand()) {
            case ApiConstants.Command.FEEDBACK_ADD:
                try {
                    System.out.println("json mNodeId" + result.getJsonString());

                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        final FeedbackResponse feedbackResponse = gson.fromJson(result.getJsonString(), FeedbackResponse.class);
                        if (feedbackResponse.isSuccess()) {
                            if (getApplicationContext() != null)
                                Toast.makeText(getApplicationContext(), "Your wish submitted successfully.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            if (getApplicationContext() != null)
                                Toast.makeText(getApplicationContext(), "Your wish could not submitted! Please try again.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (getApplicationContext() != null)
                            Toast.makeText(getApplicationContext(), R.string.failed_msg_common, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (getApplicationContext() != null)
                        Toast.makeText(getApplicationContext(), R.string.failed_msg_common, Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
                mFeedbackTask = null;

                break;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback_send_button:
                if (!TextUtils.isEmpty(feedbackEditText.getText()) &&
                        TextUtils.getTrimmedLength(feedbackEditText.getText()) > 3) {
                    FeedbackRequest feedbackRequest = new FeedbackRequest();
                    feedbackRequest.setName(fullName);
                    feedbackRequest.setMobileNumber(mobileNumber);
                    feedbackRequest.setFeedback(feedbackEditText.getText().toString());
                    feedbackRequest.setCreatedAt(Calendar.getInstance(TimeZone.getDefault()).getTime());

                    final String jsonBody = gson.toJson(feedbackRequest);

                    mFeedbackTask = new HttpRequestPostAsyncTask(ApiConstants.Command.FEEDBACK_ADD,
                            ApiConstants.Url.SPIDER_API + ApiConstants.Module.UTILITY_MODULE + ApiConstants.EndPoint.FEEDBACK_ADD_ENDPOINT, jsonBody, getApplicationContext());
                    mFeedbackTask.mHttpResponseListener = FeedbackActivity.this;
                    mFeedbackTask.addHeader(ApiConstants.Header.TOKEN, accessToken);

                    progressDialog.show();
                    mFeedbackTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else {
                    Snackbar.make(view, "Please write more.", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
