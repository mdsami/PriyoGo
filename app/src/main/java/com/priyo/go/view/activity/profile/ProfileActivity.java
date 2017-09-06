package com.priyo.go.view.activity.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Api.ApiForbiddenResponse;
import com.priyo.go.Api.HttpMultiPartAsyncTask;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.Model.ProfileInfo;
import com.priyo.go.Model.api.profile.response.ProfilePictureUploadResponse;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.DocumentPicker;
import com.priyo.go.Utilities.ProfilePictureFaceDetector;

import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author RaggedyCoder
 */

public class ProfileActivity extends AppCompatActivity implements HttpResponseListener, View.OnClickListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSION = 1001;
    private static final int REQUEST_CODE_PROFILE_UPDATE = 1002;
    private static final int ACTION_PICK_PROFILE_PICTURE = 100;

    private final Gson gson = new GsonBuilder().create();

    private AlertDialog mAlertDialog;

    private HttpRequestGetAsyncTask mProfileDetailsTask = null;
    private HttpMultiPartAsyncTask mUploadProfilePictureAsyncTask = null;
    private ProfileInfo profileInfo;
    private ProfilePictureUploadResponse mProfilePictureUploadResponse;
    private ProgressDialog mProgressDialog;
    private LinearLayout mRootView;
    private TextView mNameTextView;
    private TextView mProfessionTextView;
    private TextView mOrganizationTextView;
    private Button mProfileEditButton;
    private ProfileImageView mProfilePictureImageView;
    private SharedPreferences sharedPreferences;
    private String mMobileNumber = "";
    private String mAccessToken = "";
    private String mDeviceToken = "";
    private String mSelectedImagePath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        mMobileNumber = sharedPreferences.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        mDeviceToken = sharedPreferences.getString(Constants.DEVICE_TOKEN_KEY, "");
        mAccessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");
        initView();
        fetchProfileInfo();
    }

    private void initView() {
        mRootView = (LinearLayout) findViewById(R.id.root);
        mNameTextView = (TextView) findViewById(R.id.name_text_view);
        mProfessionTextView = (TextView) findViewById(R.id.profession_text_view);
        mOrganizationTextView = (TextView) findViewById(R.id.organization_text_view);
        mProfilePictureImageView = (ProfileImageView) findViewById(R.id.profile_picture_image_view);
        mProfileEditButton = (Button) findViewById(R.id.profile_edit_button);

        mProfilePictureImageView.setOnClickListener(this);
        mProfileEditButton.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching profile info...");
        mProgressDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Accept permission", Toast.LENGTH_LONG).show();
                } else {
                    Intent imageChooserIntent = DocumentPicker.getPickImageIntent(getApplicationContext(), "Select an image.");
                    startActivityForResult(imageChooserIntent, ACTION_PICK_PROFILE_PICTURE);
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case ACTION_PICK_PROFILE_PICTURE:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri uri = DocumentPicker.getDocumentFromResult(getApplicationContext(), resultCode, data);
                        if (uri == null) {
                            if (getApplicationContext() != null)
                                Toast.makeText(getApplicationContext(),
                                        "Could not load image",
                                        Toast.LENGTH_SHORT).show();
                        } else {

                            ProfilePictureFaceDetector profilePictureFaceDetector = new ProfilePictureFaceDetector(getApplicationContext(), uri.getPath());
                            int totalFace = profilePictureFaceDetector.countFaces();

                            if (totalFace == 1) {
                                mProfilePictureImageView.setProfilePicture(uri.getPath(), true);
                                updateProfilePicture(uri);
                            } else if (totalFace == 0) {
                                showChangeLangDialog("Please upload a proper profile picture! ");
                                //Toast.makeText(ProfileActivity.this,"Your face is not clearly visible! Please try again.", Toast.LENGTH_LONG).show();
                            } else {
                                showChangeLangDialog("Please upload a clear picture of your face only! ");
                                //Toast.makeText(ProfileActivity.this,"There are more than one person in this picture! Please try again.", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                    break;
                case REQUEST_CODE_PROFILE_UPDATE:
                    if (resultCode == RESULT_OK) {
                        this.profileInfo = data.getParcelableExtra("profileInfo");
                        setProfileInfo(this.profileInfo);
                    }
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
    }

    public void showChangeLangDialog(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setPositiveButton("TRY AGAIN", null);
        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProfileActivity.this.finish();
            }
        });
        dialogBuilder.setMessage(msg);

        mAlertDialog = dialogBuilder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();
                        attemptForSelectPicture();

                    }
                });
            }
        });
        mAlertDialog.show();
    }

    private void attemptForSelectPicture() {
        if (DocumentPicker.ifNecessaryPermissionExists(getApplicationContext(), DocumentPicker.PICTURE_SELECT_PERMISSIONS)) {
            Intent imageChooserIntent = DocumentPicker.getPickImageIntent(getApplicationContext(), "Select an image");
            startActivityForResult(imageChooserIntent, ACTION_PICK_PROFILE_PICTURE);
        } else {
            DocumentPicker.requestRequiredPermissions(ProfileActivity.this, REQUEST_CODE_PERMISSION, DocumentPicker.PICTURE_SELECT_PERMISSIONS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }

    private void fetchProfileInfo() {
        mProgressDialog.show();

        mProfileDetailsTask = new HttpRequestGetAsyncTask(ApiConstants.Command.PROFILE_FETCH,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.PROFILE_MODULE + String.format(ApiConstants.EndPoint.PROFILE_DETAILS_ENDPOINT, mMobileNumber), getApplicationContext());
        mProfileDetailsTask.mHttpResponseListener = this;
        mProfileDetailsTask.addHeader(ApiConstants.Header.ACCESS_TOKEN, mAccessToken);
        System.out.println("Access Token "+mAccessToken);
        mProfileDetailsTask.addHeader(ApiConstants.Header.DEVICE_TOKEN, mDeviceToken);
        System.out.println("Access Token "+mDeviceToken);

        mProfileDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public void httpResponseReceiver(HttpResponseObject result) {
        if (result == null) {
            mProgressDialog.dismiss();
            mProfileDetailsTask = null;
            if (getApplicationContext() != null)
                Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        String message = "";

        try {
            //Switch case to check Response Status and perform necessary actions for those status.
            switch (result.getStatus()) {
                case ApiConstants.StatusCode.S_OK:
                case ApiConstants.StatusCode.S_ACCEPTED:

                    switch (result.getApiCommand()) {
                        case ApiConstants.Command.PROFILE_FETCH:
                            mProfileDetailsTask = null;

                            profileInfo = gson.fromJson(result.getJsonString(), ProfileInfo.class);
                            setProfileInfo(profileInfo);
                            break;
                        case ApiConstants.Command.PROFILE_PHOTO_UPLOAD:
                            mProgressDialog.cancel();
                            mUploadProfilePictureAsyncTask = null;
                            mProfilePictureUploadResponse = gson.fromJson(result.getJsonString(), ProfilePictureUploadResponse.class);

                            //Updates User Profile Picture
                            if (!TextUtils.isEmpty(mProfilePictureUploadResponse.getUrl())) {
                                profileInfo.setPhotoUri(mProfilePictureUploadResponse.getUrl().trim());
                                mProfilePictureImageView.setProfilePicture(ApiConstants.Url.IMAGE_BASE_URI + mProfilePictureUploadResponse.getUrl().trim(), false);
                                sharedPreferences.edit().putString(Constants.USER_PHOTO_URL_KEY, ApiConstants.Url.IMAGE_BASE_URI + mProfilePictureUploadResponse.getUrl().trim()).apply();
                            }
                            break;
                    }

                    break;
                case ApiConstants.StatusCode.S_FORBIDDEN:

                    ApiForbiddenResponse apiForbiddenResponse = gson.fromJson(result.getJsonString(), ApiForbiddenResponse.class);
                    message = apiForbiddenResponse.getDetail();

                    break;
                case ApiConstants.StatusCode.S_NOT_ACCEPTED:
                case ApiConstants.StatusCode.S_SERVER_ERROR:
                    message = getString(R.string.can_not_request);

                    break;
                case ApiConstants.StatusCode.S_NOT_FOUND:
                default:

                    message = getString(R.string.server_down);

                    break;
            }
        } catch (Exception e) {
            message = getString(R.string.can_not_request);
        } finally {
            mProgressDialog.dismiss();

            if (!TextUtils.isEmpty(message))
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        mProgressDialog.cancel();
    }

    private void setProfileInfo(ProfileInfo profileInfo) {

        mRootView.removeAllViews();
        //Adds User Name
        if (!TextUtils.isEmpty(profileInfo.getName())) {
            mNameTextView.setText(profileInfo.getName().trim());
            sharedPreferences.edit().putString(Constants.USER_FULL_NAME_KEY, profileInfo.getName().trim()).apply();
        }
        //Adds User Profession
        if (!TextUtils.isEmpty(profileInfo.getProfession())) {
            mProfessionTextView.setText(profileInfo.getProfession().trim());
            sharedPreferences.edit().putString(Constants.USER_PROFESSION_KEY, profileInfo.getProfession().trim()).apply();
        }
        //Adds User Working Organization
        if (!TextUtils.isEmpty(profileInfo.getOrganizationName())) {
            mOrganizationTextView.setText(profileInfo.getOrganizationName().trim());
            sharedPreferences.edit().putString(Constants.USER_ORGANIZATION_KEY, profileInfo.getOrganizationName().trim()).apply();
        }
        //Updates User Profile Picture
        if (!TextUtils.isEmpty(profileInfo.getPhotoUri())) {
            mProfilePictureImageView.setProfilePicture(ApiConstants.Url.IMAGE_BASE_URI + profileInfo.getPhotoUri(), false);
            sharedPreferences.edit().putString(Constants.USER_PHOTO_URL_KEY, ApiConstants.Url.IMAGE_BASE_URI + profileInfo.getPhotoUri().trim()).apply();
        }
        //Adds User Description
        if (!TextUtils.isEmpty(profileInfo.getDescription())) {
            addDetailsToProfileView(getString(R.string.about_me), profileInfo.getDescription().trim());
            sharedPreferences.edit().putString(Constants.USER_DESCRIPTION_KEY, profileInfo.getDescription().trim()).apply();
        }
        //Adds User Phone Number
        if (!TextUtils.isEmpty(mMobileNumber)) {
            addDetailsToProfileView(getString(R.string.phone_number), mMobileNumber);
        }
        //Adds User Date of Birth
        if (profileInfo.getDateOfBirthDate() != null) {
            String birthDateDisplay = "";
            if (!TextUtils.isEmpty(profileInfo.getDateOfBirthDisplay())) {
                birthDateDisplay = profileInfo.getDateOfBirthDisplay().trim();
            } else if (profileInfo.getDateOfBirthDate().getDate() != 0) {
                SimpleDateFormat birthDateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                Date birthDate = new Date(profileInfo.getDateOfBirthDate().getDate());
                birthDateDisplay = birthDateFormatter.format(birthDate);
            }
            if (!TextUtils.isEmpty(birthDateDisplay)) {
                addDetailsToProfileView(getString(R.string.date_of_birth), birthDateDisplay);
                sharedPreferences.edit().putLong(Constants.USER_DATE_OF_BIRTH_KEY, profileInfo.getDateOfBirthDate().getDate()).apply();
            }
        }

        //Updates User Gender
        if (!TextUtils.isEmpty(profileInfo.getGender())) {
            addDetailsToProfileView(getString(R.string.gender), profileInfo.getGender());
            sharedPreferences.edit().putString(Constants.USER_GENDER_KEY, profileInfo.getGender().trim()).apply();
        }


        //Adds User Location
        if (!TextUtils.isEmpty(profileInfo.getDistrict())) {
            String from = profileInfo.getDistrict();
            sharedPreferences.edit().putString(Constants.USER_DISTRICT_KEY, profileInfo.getDistrict()).apply();
            if (!TextUtils.isEmpty(profileInfo.getThana())) {
                from = profileInfo.getThana().trim() + ", " + from;
                sharedPreferences.edit().putString(Constants.USER_THANA_KEY, profileInfo.getThana()).apply();
            }
            addDetailsToProfileView("From", from);
        }
    }

    private void addDetailsToProfileView(String title, String value) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout detailsLayout = (LinearLayout) inflater.inflate(R.layout.item_details, mRootView, false);
        TextView titleTextView = (TextView) detailsLayout.findViewById(R.id.title_text_view);
        TextView valueTextView = (TextView) detailsLayout.findViewById(R.id.value_text_view);
        titleTextView.setText(title);
        valueTextView.setText(value);
        mRootView.addView(detailsLayout);
    }

    private void updateProfilePicture(Uri selectedImageUri) {


        mUploadProfilePictureAsyncTask = new HttpMultiPartAsyncTask(ApiConstants.Command.PROFILE_PHOTO_UPLOAD,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.PROFILE_MODULE + String.format(ApiConstants.EndPoint.PROFILE_PICTURE_UPLOAD_ENDPOINT, mMobileNumber), this);

        mUploadProfilePictureAsyncTask.addHeader(ApiConstants.Header.ACCESS_TOKEN, mAccessToken).
                addHeader(ApiConstants.Header.DEVICE_TOKEN, mDeviceToken);

        try {
            mSelectedImagePath = selectedImageUri.getPath();
            final File sourceFile = new File(mSelectedImagePath);
            final String filename = mSelectedImagePath.substring(mSelectedImagePath.lastIndexOf("/") + 1);
            mUploadProfilePictureAsyncTask.addFormBody(filename, sourceFile);
        } catch (MalformedURLException e) {
            Toast.makeText(this, "Unable to upload the image", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.setMessage("Uploading picture..");
        mProgressDialog.show();

        mUploadProfilePictureAsyncTask.setHttpResponseListener(this);
        mUploadProfilePictureAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_edit_button:
                Intent intent = new Intent(this, UpdateProfileActivity.class);
                intent.putExtra("profileInfo", (Parcelable) profileInfo);
                startActivityForResult(intent, REQUEST_CODE_PROFILE_UPDATE);
                break;
            case R.id.profile_picture_image_view:
                attemptForSelectPicture();
                break;
        }
    }
}
