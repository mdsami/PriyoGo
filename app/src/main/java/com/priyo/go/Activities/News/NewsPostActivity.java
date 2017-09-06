package com.priyo.go.Activities.News;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Api.PostNewsAsyncTask;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.DocumentPicker;
import com.priyo.go.Utilities.Utilities;

import org.json.JSONObject;

public class NewsPostActivity extends AppCompatActivity implements HttpResponseListener {

    private final int REQUEST_CODE_PERMISSION = 1001;
    private final int ACTION_PICK_PROFILE_PICTURE = 100;
    private ProgressDialog mProgressDialog;
    private Button mPostNewsButton;
    private EditText mNewsTextBox;
    private FloatingActionButton mAttachImage;
    private ImageView mNewsPictureView;
    private Uri picUri;
    private String mSelectedImagePath = "";
    private PostNewsAsyncTask mPostNewsAsyncTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_post_1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        mPostNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isConnectionAvailable(getApplicationContext())) {
                    if (verifyUserInputs())
                        postNews(picUri, mNewsTextBox.getText().toString());
                } else if (getApplicationContext() != null)
                    Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            }
        });

        mAttachImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DocumentPicker.ifNecessaryPermissionExists(getApplicationContext(), DocumentPicker.PICTURE_SELECT_PERMISSIONS)) {
                    Intent imageChooserIntent = DocumentPicker.getPickImageIntent(getApplicationContext(), "Select an image.");
                    startActivityForResult(imageChooserIntent, ACTION_PICK_PROFILE_PICTURE);
                } else {
                    DocumentPicker.requestRequiredPermissions(NewsPostActivity.this, REQUEST_CODE_PERMISSION, DocumentPicker.PICTURE_SELECT_PERMISSIONS);
                }
            }
        });
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.progress_dialog_text_post_news));

        mPostNewsButton = (Button) findViewById(R.id.sign_up_button);
        mAttachImage = (FloatingActionButton) findViewById(R.id.fab);
        mNewsTextBox = (EditText) findViewById(R.id.news_body);
        mNewsPictureView = (ImageView) findViewById(R.id.postImage);
    }


    private void postNews(Uri selectedImageUri, String body) {
        mProgressDialog.setMessage(getString(R.string.progress_dialog_text_post_news));
        mProgressDialog.show();
        mSelectedImagePath = selectedImageUri.getPath();
        mPostNewsAsyncTask = new PostNewsAsyncTask(Constants.COMMAND_POST_NEWS,
                mSelectedImagePath, body, getApplicationContext());
        mPostNewsAsyncTask.mHttpResponseListener = this;
        mPostNewsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (DocumentPicker.ifNecessaryPermissionExists(getApplicationContext(), DocumentPicker.PICTURE_SELECT_PERMISSIONS)) {
                    Intent imageChooserIntent = DocumentPicker.getPickImageIntent(getApplicationContext(), "Select an image.");
                    startActivityForResult(imageChooserIntent, ACTION_PICK_PROFILE_PICTURE);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.accept_permission_for_camera), Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_PICK_PROFILE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = DocumentPicker.getDocumentFromResult(getApplicationContext(), resultCode, data);
                    if (uri == null) {
                        mNewsPictureView.setVisibility(View.GONE);
                        if (getApplicationContext() != null)
                            Toast.makeText(getApplicationContext(), "Could not load image", Toast.LENGTH_SHORT).show();
                    } else {
                        picUri = uri;
                        mNewsPictureView.setVisibility(View.VISIBLE);
                        mNewsPictureView.setImageURI(uri);
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void httpResponseReceiver(HttpResponseObject result) {

        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            mProgressDialog.dismiss();
            mPostNewsAsyncTask = null;
            if (getApplicationContext() != null)
                Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        switch (result.getApiCommand()) {

            case Constants.COMMAND_POST_NEWS:

                try {
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        try {
                            JSONObject ss = new JSONObject(result.getJsonString());
                            Boolean picUrl = ss.getBoolean("success");
                            if (picUrl) {
                                Toast.makeText(getApplicationContext(), "News submitted successfully!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "News could not be submitted! Try again later!", Toast.LENGTH_LONG).show();
                            }
                            finish();
                        } catch (Exception e) {
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                }

                mProgressDialog.dismiss();
                mPostNewsAsyncTask = null;
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

    public boolean verifyUserInputs() {
        boolean cancel = false;
        View focusedView = null;
        mNewsTextBox.setError(null);

        if (mNewsTextBox.getText().toString().trim().length() == 0) {
            mNewsTextBox.setError(this.getString(R.string.invalid_district));
            focusedView = mNewsTextBox;
            cancel = true;
        }

        if (picUri == null) {
            Toast.makeText(this, "Photo attachment required!", Toast.LENGTH_LONG).show();
            cancel = true;
        }
        if (cancel) {
            focusedView.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}
