package com.priyo.go.view.activity.profile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpRequestPostAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.CustomView.ResourceSelectorDialog;
import com.priyo.go.Model.Friend.District;
import com.priyo.go.Model.Friend.DistrictRequestResponse;
import com.priyo.go.Model.Friend.Gender;
import com.priyo.go.Model.Friend.Profession;
import com.priyo.go.Model.Friend.ProfessionRequestResponse;
import com.priyo.go.Model.Friend.Thana;
import com.priyo.go.Model.Friend.ThanaRequestResponse;
import com.priyo.go.Model.MetaData;
import com.priyo.go.Model.ProfileInfo;
import com.priyo.go.Model.UserLocation;
import com.priyo.go.Model.api.profile.request.ProfileInfoUpdateRequest;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by sajid.shahriar on 4/25/17.
 */

public class UpdateProfileActivity extends AppCompatActivity implements HttpResponseListener, View.OnClickListener {

    private static final String TAG = UpdateProfileActivity.class.getSimpleName();
    private final String[] GENDER_ARRAY = {"Male", "Female", "Other"};
    private final Gson gson = new GsonBuilder().create();
    ProgressDialog progressDialog;
    int count = 0;
    private int mSelectedGenderId = -1;
    private int mSelectedDistrictId = -1;
    private int mSelectedThanaId = -1;
    private int mSelectedProfessionId = -1;
    private List<Gender> mGenderList;
    private List<District> mDistrictList;
    private List<Thana> mThanaList;
    private List<Profession> mProfessionList;
    private HttpRequestGetAsyncTask mGetThanaListAsyncTask = null;
    private HttpRequestGetAsyncTask mGetProfessionListAsyncTask = null;
    private HttpRequestGetAsyncTask mGetDistrictListAsyncTask = null;
    private DistrictRequestResponse mDistrictRequestResponse;
    private ThanaRequestResponse mThanaRequestResponse;
    private ProfessionRequestResponse mProfessionRequestResponse;
    private EditText userFullNameEditText;
    private EditText userDateOfBirthEditText;
    private EditText userGenderEditText;
    private EditText userDescriptionEditText;
    private EditText districtEditText;
    private EditText thanaEditText;
    private EditText professionEditText;
    private EditText organizationEditText;
    private Button updateProfileButton;
    private ResourceSelectorDialog<Gender> genderSelectorDialog;
    private ResourceSelectorDialog<District> districtSelectorDialog;
    private ResourceSelectorDialog<Thana> thanaSelectorDialog;
    private ResourceSelectorDialog<Profession> professionSelectorDialog;
    private Date dateOfBirth;
    private String mMobileNumber;
    private String accessToken;
    private ProfileInfo profileInfo;
    private SharedPreferences sharedPreferences;
    private String deviceToken;
    private Set<String> publicAccessList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCancelable(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(Constants.ApplicationTag, MODE_PRIVATE);
        loadProfileInfo();
        initializeView();
        progressDialog.show();
        fetchProfessionList();
        fetchDistrictList();
        setGenderAdapter();

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

    private void loadProfileInfo() {
        profileInfo = getIntent().getParcelableExtra("profileInfo");
        mMobileNumber = sharedPreferences.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");
        deviceToken = sharedPreferences.getString(Constants.DEVICE_TOKEN_KEY, "");
        publicAccessList = sharedPreferences.getStringSet(Constants.USER_PUBLIC_LIST_KEY, Constants.PUBLIC_ACCESS_LIST);
    }

    private void initializeView() {
        userFullNameEditText = (EditText) findViewById(R.id.user_full_name_edit_text);
        userDateOfBirthEditText = (EditText) findViewById(R.id.user_date_of_birth_edit_text);
        userGenderEditText = (EditText) findViewById(R.id.user_gender_edit_text);
        userDescriptionEditText = (EditText) findViewById(R.id.user_description_edit_text);
        districtEditText = (EditText) findViewById(R.id.district_edit_text);
        thanaEditText = (EditText) findViewById(R.id.thana_edit_text);
        professionEditText = (EditText) findViewById(R.id.profession_edit_text);
        organizationEditText = (EditText) findViewById(R.id.organization_edit_text);
        updateProfileButton = (Button) findViewById(R.id.update_profile_button);

        updateProfileButton.setOnClickListener(this);
        userDateOfBirthEditText.setOnClickListener(this);
        if (profileInfo != null) {
            if (!TextUtils.isEmpty(profileInfo.getName())) {
                userFullNameEditText.setText(profileInfo.getName());
            }
            if (profileInfo.getDateOfBirthDate() != null) {
                if (profileInfo.getDateOfBirthDate().getDate() != 0) {
                    SimpleDateFormat birthDateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                    Date birthDate = new Date(profileInfo.getDateOfBirthDate().getDate());
                    userDateOfBirthEditText.setText(birthDateFormatter.format(birthDate));
                }
            }
            if (!TextUtils.isEmpty(profileInfo.getGender())) {
                userGenderEditText.setText(profileInfo.getGender());
            }
            if (!TextUtils.isEmpty(profileInfo.getDistrict())) {
                districtEditText.setText(profileInfo.getDistrict());
            }
            if (!TextUtils.isEmpty(profileInfo.getThana())) {
                thanaEditText.setText(profileInfo.getThana());
            }
            if (!TextUtils.isEmpty(profileInfo.getProfession())) {
                professionEditText.setText(profileInfo.getProfession());
            }
            if (!TextUtils.isEmpty(profileInfo.getOrganizationName())) {
                organizationEditText.setText(profileInfo.getOrganizationName());
            }

            if (!TextUtils.isEmpty(profileInfo.getDescription())) {
                userDescriptionEditText.setText(profileInfo.getDescription());
            }
        }
    }

    private void fetchThanaList(int districtId) {
        if (mGetThanaListAsyncTask != null) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.Parameter.LABEL_PARAMETER, "Thana");
        params.put(ApiConstants.Parameter.RELATIONSHIP_PARAMETER, "UNDER_DISTRICT");
        params.put(ApiConstants.Parameter.RELATED_TO_ID_PARAMETER, Integer.toString(districtId));
        params.put(ApiConstants.Parameter.SORTING_PARAMETER, "name");
        params.put(ApiConstants.Parameter.SORTING_ORDER_PARAMETER, "asc");
        params.put(ApiConstants.Parameter.PAGE_SIZE_PARAMETER, "all");

        mGetThanaListAsyncTask = new HttpRequestGetAsyncTask(ApiConstants.Command.THANA_LIST_FETCH,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + ApiConstants.EndPoint.THANA_LIST_FETCH_ENDPOINT,
                this, params, this);
        mGetThanaListAsyncTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        mGetThanaListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void fetchDistrictList() {
        if (mGetDistrictListAsyncTask != null) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.Parameter.SORTING_PARAMETER, "name");
        params.put(ApiConstants.Parameter.SORTING_ORDER_PARAMETER, "asc");
        params.put(ApiConstants.Parameter.PAGE_SIZE_PARAMETER, "all");


        mGetDistrictListAsyncTask = new HttpRequestGetAsyncTask(ApiConstants.Command.DISTRICT_LIST_FETCH,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + ApiConstants.EndPoint.DISTRICT_LIST_FETCH_ENDPOINT,
                this, params, this);
        mGetDistrictListAsyncTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        mGetDistrictListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void fetchProfessionList() {
        if (mGetProfessionListAsyncTask != null) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.Parameter.LABEL_PARAMETER, "Profession");
        params.put(ApiConstants.Parameter.SORTING_PARAMETER, "name");
        params.put(ApiConstants.Parameter.SORTING_ORDER_PARAMETER, "asc");
        params.put(ApiConstants.Parameter.PAGE_SIZE_PARAMETER, "all");
        mGetProfessionListAsyncTask = new HttpRequestGetAsyncTask(ApiConstants.Command.PROFESSION_LIST_FETCH,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + ApiConstants.EndPoint.PROFESSION_LIST_FETCH_ENDPOINT,
                this, params, this);
        mGetProfessionListAsyncTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        mGetProfessionListAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void setGenderAdapter() {
        mGenderList = new ArrayList<>();
        for (int i = 0; i < GENDER_ARRAY.length; i++) {
            Gender g = new Gender("", i, "", GENDER_ARRAY[i]);
            mGenderList.add(g);
        }
        genderSelectorDialog = new ResourceSelectorDialog<>(this, getString(R.string.select_gender), mGenderList, mSelectedGenderId);
        genderSelectorDialog.setOnResourceSelectedListener(new ResourceSelectorDialog.OnResourceSelectedListener() {
            @Override
            public void onResourceSelected(int id, String name) {
                userGenderEditText.setError(null);
                userGenderEditText.setText(name);
                mSelectedGenderId = id;
            }
        });
        userGenderEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderSelectorDialog.show();
            }
        });
    }

    private void setThanaAdapter(List<Thana> thanaList) {
        thanaSelectorDialog = new ResourceSelectorDialog<>(this, getString(R.string.select_a_thana), thanaList, mSelectedThanaId);
        thanaSelectorDialog.setOnResourceSelectedListener(new ResourceSelectorDialog.OnResourceSelectedListener() {
            @Override
            public void onResourceSelected(int id, String name) {
                thanaEditText.setError(null);
                thanaEditText.setText(name);
                mSelectedThanaId = id;
            }
        });
        thanaEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thanaSelectorDialog.show();
            }
        });
    }

    private void setProfessionAdapter(List<Profession> professionList) {
        professionSelectorDialog = new ResourceSelectorDialog<>(this, getString(R.string.select_a_profession), professionList, mSelectedProfessionId);
        professionSelectorDialog.setOnResourceSelectedListener(new ResourceSelectorDialog.OnResourceSelectedListener() {
            @Override
            public void onResourceSelected(int id, String name) {
                professionEditText.setError(null);
                professionEditText.setText(name);
                mSelectedProfessionId = id;
            }
        });
        professionEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                professionSelectorDialog.show();
            }
        });
    }

    private void setDistrictAdapter(List<District> districtList) {
        districtSelectorDialog = new ResourceSelectorDialog<>(this, getString(R.string.select_a_district), districtList, mSelectedDistrictId);
        districtSelectorDialog.setOnResourceSelectedListener(new ResourceSelectorDialog.OnResourceSelectedListener() {
            @Override
            public void onResourceSelected(int id, String name) {
                districtEditText.setError(null);
                districtEditText.setText(name);
                mSelectedDistrictId = id;

                mThanaList = null;
                mSelectedThanaId = -1;
                thanaEditText.setText(getString(R.string.loading));
                mThanaRequestResponse = null;
                fetchThanaList(mSelectedDistrictId);
            }
        });

        districtEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                districtSelectorDialog.show();
            }
        });
    }

    @Override
    public void httpResponseReceiver(@Nullable HttpResponseObject result) {

        if (result == null) {
            mGetThanaListAsyncTask = null;
            mGetDistrictListAsyncTask = null;
            progressDialog.cancel();
            Toast.makeText(this, R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (count >= 2) {
            progressDialog.cancel();
        }
        switch (result.getStatus()) {
            case ApiConstants.StatusCode.S_OK:
            case ApiConstants.StatusCode.S_ACCEPTED:
                switch (result.getApiCommand()) {
                    case ApiConstants.Command.DISTRICT_LIST_FETCH:
                        count++;
                        mDistrictRequestResponse = gson.fromJson(result.getJsonString(), DistrictRequestResponse.class);
                        mDistrictList = mDistrictRequestResponse.getData();
                        setDistrictAdapter(mDistrictList);
                        setDistrictField(profileInfo.getDistrict());
                        if (mSelectedDistrictId >= 0) {
                            fetchThanaList(mSelectedDistrictId);
                        }
                        mGetDistrictListAsyncTask = null;
                        break;
                    case ApiConstants.Command.THANA_LIST_FETCH:
                        count++;
                        mThanaRequestResponse = gson.fromJson(result.getJsonString(), ThanaRequestResponse.class);
                        mThanaList = mThanaRequestResponse.getData();
                        setThanaAdapter(mThanaList);
                        setThanaField(profileInfo.getThana());
                        mGetThanaListAsyncTask = null;
                        break;
                    case ApiConstants.Command.PROFESSION_LIST_FETCH:
                        count++;
                        mProfessionRequestResponse = gson.fromJson(result.getJsonString(), ProfessionRequestResponse.class);
                        mProfessionList = mProfessionRequestResponse.getData();
                        setProfessionAdapter(mProfessionList);
                        setProfessionField(profileInfo.getProfession());
                        mGetProfessionListAsyncTask = null;
                        break;
                    case ApiConstants.Command.PROFILE_UPDATE:
                        ProfileInfo profileInfoResponse = gson.fromJson(result.getJsonString(), ProfileInfo.class);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("profileInfo", (Parcelable) profileInfoResponse);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                        break;
                }
                break;
            case ApiConstants.StatusCode.S_FORBIDDEN:
                progressDialog.cancel();
                break;
            case ApiConstants.StatusCode.S_BAD_REQUEST:
            case ApiConstants.StatusCode.S_NOT_ACCEPTED:
                progressDialog.cancel();
                Toast.makeText(this, R.string.failed_loading_district_list, Toast.LENGTH_LONG).show();
                break;
            case ApiConstants.StatusCode.S_NOT_FOUND:
                progressDialog.cancel();
                Toast.makeText(this, R.string.server_down, Toast.LENGTH_LONG).show();
                break;

        }
    }

    private void setProfessionField(String professionName) {
        if (mProfessionList != null) {
            if (!TextUtils.isEmpty(professionName)) {
                for (int i = 0; i < mProfessionList.size(); i++) {
                    if (professionName.equals(mProfessionList.get(i).getNodeTitle())) {
                        mSelectedProfessionId = mProfessionList.get(i).getNodeID();
                        professionEditText.setText(mProfessionList.get(i).getNodeTitle());
                        return;
                    }
                }
            }
            if (mProfessionList.size() > 0) {
                mSelectedProfessionId = mProfessionList.get(0).getNodeID();
                professionEditText.setText(mProfessionList.get(0).getNodeTitle());
            }
        }
    }

    private void setDistrictField(String districtName) {
        if (mDistrictList != null && !TextUtils.isEmpty(districtName)) {
            for (int i = 0; i < mDistrictList.size(); i++) {
                if (districtName.equals(mDistrictList.get(i).getNodeTitle())) {
                    mSelectedDistrictId = mDistrictList.get(i).getNodeID();
                    districtEditText.setText(mDistrictList.get(i).getNodeTitle());
                    return;
                }
            }
            if (mDistrictList.size() > 0) {
                mSelectedDistrictId = mDistrictList.get(0).getNodeID();
                districtEditText.setText(mDistrictList.get(0).getNodeTitle());
            }
        }
    }

    private void setThanaField(String thanaField) {
        if (mThanaList != null && !TextUtils.isEmpty(thanaField)) {
            for (int i = 0; i < mThanaList.size(); i++) {
                if (thanaField.equals(mThanaList.get(i).getNodeTitle())) {
                    mSelectedThanaId = mThanaList.get(i).getNodeID();
                    thanaEditText.setText(mThanaList.get(i).getNodeTitle());
                    return;
                }
            }
            if (mThanaList.size() > 0) {
                mSelectedThanaId = mThanaList.get(0).getNodeID();
                thanaEditText.setText(mThanaList.get(0).getNodeTitle());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_profile_button:
                if (isValidProfileInfo()) {
                    updateProfileInfo();
                }
                break;
            case R.id.user_date_of_birth_edit_text:
                SimpleDateFormat birthDateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                Date date = new Date(System.currentTimeMillis());
                if (!TextUtils.isEmpty(userDateOfBirthEditText.getText())) {
                    try {
                        date = birthDateFormatter.parse(userDateOfBirthEditText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                final DatePickerDialog dialog = new DatePickerDialog(
                        this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.d(TAG, dayOfMonth + " " + month + " " + year);
                        dateOfBirth = new Date(year - 1900, month, dayOfMonth);
                        SimpleDateFormat birthDateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                        userDateOfBirthEditText.setText(birthDateFormatter.format(dateOfBirth));
                    }
                }, 1900 + date.getYear(), date.getMonth(), date.getDay());
                dialog.setCancelable(false);
                dialog.show();
                break;
        }
    }

    private boolean isValidProfileInfo() {

        if (TextUtils.isEmpty(userFullNameEditText.getText())) {
            //Log.w(TAG, "Full Name not Entered");
            userFullNameEditText.requestFocus();
            userFullNameEditText.setError(getString(R.string.empty_user_full_name));
            return false;
        }
        return true;
    }

    private void updateProfileInfo() {
        ProfileInfoUpdateRequest tempProfileInfo = new ProfileInfoUpdateRequest();
        tempProfileInfo.setName(userFullNameEditText.getText().toString());
        if (dateOfBirth != null) {
            SimpleDateFormat dateOfBirthFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat dateOfBirthDisplayFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
            tempProfileInfo.setDateOfBirthDate(dateOfBirthFormat.format(dateOfBirth));
            tempProfileInfo.setDateOfBirthDisplay(dateOfBirthDisplayFormat.format(dateOfBirth));
        }
        if (!TextUtils.isEmpty(userGenderEditText.getText())) {
            tempProfileInfo.setGender(userGenderEditText.getText().toString());
        }
        if (!TextUtils.isEmpty(userDescriptionEditText.getText())) {
            tempProfileInfo.setDescription(userDescriptionEditText.getText().toString());
        }

        if (!TextUtils.isEmpty(professionEditText.getText()) && mSelectedProfessionId != -1) {
            tempProfileInfo.setProfession(new Profession());
            tempProfileInfo.getProfession().setNodeID(mSelectedProfessionId);
            tempProfileInfo.getProfession().setNodeTitle(professionEditText.getText().toString());
        }

        if (!TextUtils.isEmpty(districtEditText.getText()) && mSelectedDistrictId != -1) {
            tempProfileInfo.setDistrict(new District());
            tempProfileInfo.getDistrict().setNodeID(mSelectedDistrictId);
            tempProfileInfo.getDistrict().setNodeTitle(districtEditText.getText().toString());
        }

        if (!TextUtils.isEmpty(thanaEditText.getText()) && mSelectedThanaId != -1) {
            tempProfileInfo.setThana(new Thana());
            tempProfileInfo.getThana().setNodeID(mSelectedThanaId);
            tempProfileInfo.getThana().setNodeTitle(thanaEditText.getText().toString());
        }


        if (!TextUtils.isEmpty(organizationEditText.getText())) {
            tempProfileInfo.setOrganizationName(organizationEditText.getText().toString());
        }

        tempProfileInfo.setPublicAccessList(publicAccessList);

        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(tempProfileInfo);

        double latitude = sharedPreferences.getFloat(Constants.LATITUDE_KEY, 0.0f);
        double longitude = sharedPreferences.getFloat(Constants.LONGITUDE_KEY, 0.0f);

        HttpRequestPostAsyncTask updateProfileAsyncTask = new HttpRequestPostAsyncTask(ApiConstants.Command.PROFILE_UPDATE,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.PROFILE_MODULE + String.format(ApiConstants.EndPoint.PROFILE_DETAILS_ENDPOINT, mMobileNumber), jsonBody, this, this);
        updateProfileAsyncTask.addHeader(ApiConstants.Header.ACCESS_TOKEN, accessToken);
        updateProfileAsyncTask.addHeader(ApiConstants.Header.DEVICE_TOKEN, deviceToken);
        updateProfileAsyncTask.addHeader(ApiConstants.Header.META_DATA, Base64.encodeToString(gson.toJson(new MetaData(0, new UserLocation(latitude, longitude))).getBytes(), Base64.DEFAULT).replaceAll("\n", ""));


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Profile");
        progressDialog.setCancelable(false);
        progressDialog.show();
        updateProfileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }
}
