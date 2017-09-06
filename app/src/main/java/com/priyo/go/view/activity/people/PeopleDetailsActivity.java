package com.priyo.go.view.activity.people;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Api.ApiForbiddenResponse;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.Model.Business.RelationshipNode;
import com.priyo.go.Model.DateInfo;
import com.priyo.go.Model.ProfileInfo;
import com.priyo.go.Model.graph.GraphInfo;
import com.priyo.go.Model.graph.Property;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Md. Sajid Shahriar
 */

public class PeopleDetailsActivity extends AppCompatActivity implements HttpResponseListener {
    private static final String TAG = PeopleDetailsActivity.class.getSimpleName();
    private final Gson gson = new GsonBuilder().create();

    private HttpRequestGetAsyncTask peopleDetailsTask = null;

    private ProgressDialog progressDialog;

    private LinearLayout mRootView;
    private TextView nameTextView;
    private TextView professionTextView;
    private TextView organizationTextView;
    private ProfileImageView profileImageView;

    private int nodeID;
    private String hashedMobileNumber;
    private boolean isCelebrity;

    private String accessToken;
    private String deviceToken;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(@Nullable Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_details);
        initView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("People Profile");
        }

        Intent intent = getIntent();

        hashedMobileNumber = intent.getStringExtra("hashedMobileNumber");
        isCelebrity = intent.getBooleanExtra("isCelebrity", false);
        if (isCelebrity) {
            nodeID = intent.getIntExtra("nodeID", 0);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        deviceToken = sharedPreferences.getString(Constants.DEVICE_TOKEN_KEY, "");
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");

        fetchProfileInfo();
    }

    private void initView() {
        mRootView = (LinearLayout) findViewById(R.id.root);
        nameTextView = (TextView) findViewById(R.id.name_text_view);
        professionTextView = (TextView) findViewById(R.id.profession_text_view);
        organizationTextView = (TextView) findViewById(R.id.organization_text_view);
        profileImageView = (ProfileImageView) findViewById(R.id.profile_picture_image_view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching profile info...");
        progressDialog.setCancelable(false);
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
        progressDialog.show();
        if (isCelebrity) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiConstants.Parameter.NODE_ID_PARAMETER, Integer.toString(nodeID));
            peopleDetailsTask = new HttpRequestGetAsyncTask(ApiConstants.Command.PROFILE_FETCH,
                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + ApiConstants.EndPoint.PEOPLE_ENDPOINT, this, params, this);
            peopleDetailsTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        } else {
            peopleDetailsTask = new HttpRequestGetAsyncTask(ApiConstants.Command.PROFILE_FETCH,
                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.PROFILE_MODULE + String.format(ApiConstants.EndPoint.PROFILE_DETAILS_ENDPOINT, hashedMobileNumber), getApplicationContext());
            peopleDetailsTask.mHttpResponseListener = this;
            peopleDetailsTask.addHeader(ApiConstants.Header.ACCESS_TOKEN, accessToken);
            peopleDetailsTask.addHeader(ApiConstants.Header.DEVICE_TOKEN, deviceToken);
        }
        peopleDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public void httpResponseReceiver(HttpResponseObject result) {
        if (result == null) {
            progressDialog.dismiss();
            peopleDetailsTask = null;
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
                            peopleDetailsTask = null;
                            if (!isCelebrity)
                                setProfileInfo(gson.fromJson(result.getJsonString(), ProfileInfo.class));
                            else
                                setProfileInfo(parseToProfileInfo(gson.fromJson(result.getJsonString(), GraphInfo.class)));
                            break;
                    }

                    break;
                case ApiConstants.StatusCode.S_FORBIDDEN:
                case ApiConstants.StatusCode.S_BAD_REQUEST:
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
            progressDialog.dismiss();

            if (!TextUtils.isEmpty(message))
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
    }

    private ProfileInfo parseToProfileInfo(GraphInfo graphInfo) {
        ProfileInfo profileInfo = new ProfileInfo();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        for (Property property : graphInfo.getProperties()) {
            if (property.getKey().equals("name")) {
                profileInfo.setName(property.getValue());
            } else if (property.getKey().equals("description")) {
                profileInfo.setDescription(property.getValue());
            } else if (property.getKey().equals("dob")) {
                try {
                    profileInfo.setDateOfBirthDate(new DateInfo(simpleDateFormat.parse(property.getValue()).getTime()));
                } catch (ParseException e) {
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();
                }
            } else if (property.getKey().equals("photo")) {
                profileInfo.setPhotoUri(property.getValue());
            }
        }
        for (RelationshipNode relationshipNode : graphInfo.getRelationships()) {
            if (relationshipNode.getRelatedNodeLabel().equals("Profession")) {
                profileInfo.setProfession(relationshipNode.getRelatedNodeTitle());
            }
        }
        return profileInfo;
    }

    private void setProfileInfo(ProfileInfo mProfileInfo) {

        //Adds User Name
        if (!TextUtils.isEmpty(mProfileInfo.getName())) {
            nameTextView.setText(mProfileInfo.getName().trim());
        }
        //Adds User Profession
        if (!TextUtils.isEmpty(mProfileInfo.getProfession())) {
            professionTextView.setText(mProfileInfo.getProfession().trim());
        }
        //Adds User Working Organization
        if (!TextUtils.isEmpty(mProfileInfo.getOrganizationName())) {
            organizationTextView.setText(mProfileInfo.getOrganizationName().trim());
        }
        //Updates User Profile Picture
        if (!TextUtils.isEmpty(mProfileInfo.getPhotoUri())) {
            //TODO : have to fix this issue later.
            if (mProfileInfo.getPhotoUri().contains(ApiConstants.Url.IMAGE_BASE_URI)) {
                profileImageView.setProfilePicture(mProfileInfo.getPhotoUri(), false);
            } else {
                profileImageView.setProfilePicture(ApiConstants.Url.IMAGE_BASE_URI + mProfileInfo.getPhotoUri(), false);
            }

        }
        //Adds User Description
        if (!TextUtils.isEmpty(mProfileInfo.getDescription())) {
            addDetailsToProfileView(getString(R.string.about_me), mProfileInfo.getDescription().trim());
        }

        //Adds User Date of Birth
        if (mProfileInfo.getDateOfBirthDate() != null) {
            String birthDateDisplay = "";
            if (mProfileInfo.getDateOfBirthDate().getDate() != 0) {
                SimpleDateFormat birthDateFormatter = new SimpleDateFormat("dd MMMM", Locale.US);
                Date birthDate = new Date(mProfileInfo.getDateOfBirthDate().getDate());
                birthDateDisplay = birthDateFormatter.format(birthDate);
            } else if (!TextUtils.isEmpty(mProfileInfo.getDateOfBirthDisplay())) {
                birthDateDisplay = mProfileInfo.getDateOfBirthDisplay().trim();
            }
            if (!TextUtils.isEmpty(birthDateDisplay)) {
                addDetailsToProfileView(getString(R.string.date_of_birth), birthDateDisplay);
            }
        }

        //Updates User Gender
        if (!TextUtils.isEmpty(mProfileInfo.getGender())) {
            addDetailsToProfileView(getString(R.string.gender), mProfileInfo.getGender());
        }


        //Adds User Location
        if (!TextUtils.isEmpty(mProfileInfo.getDistrict())) {
            String from = mProfileInfo.getDistrict();
            if (!TextUtils.isEmpty(mProfileInfo.getThana())) {
                from = mProfileInfo.getThana().trim() + ", " + from;
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


}
