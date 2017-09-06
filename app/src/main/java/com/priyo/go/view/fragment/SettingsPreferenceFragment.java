package com.priyo.go.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.priyo.go.Api.HttpRequestPostAsyncTask;
import com.priyo.go.Api.HttpRequestPutAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.api.location.request.LocationSearchableUpdateRequest;
import com.priyo.go.Model.api.profile.request.ProfileInfoUpdateRequest;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class SettingsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, HttpResponseListener {

    private static final Set<String> dobAccessList = new HashSet<>(Arrays.asList("dob_display", "dob_date"));
    private final Gson gson = new Gson();
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private HttpRequestPostAsyncTask userProfileSettingsTask = null;
    private HttpRequestPutAsyncTask userLocationSettingsTask = null;
    private Preference versionPreference;
    private CheckBoxPreference shareDateOfBirthPreference;
    private CheckBoxPreference shareLocationPreference;

    private Set<String> publicAccessList;
    private boolean shareUserLocation;
    private String mobileNumber;
    private String accessToken;
    private String deviceToken;

    private PackageInfo packageInfo;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Updating your settings...");

        sharedPreferences = getActivity().getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);


        publicAccessList = sharedPreferences.getStringSet(Constants.USER_PUBLIC_LIST_KEY, Constants.PUBLIC_ACCESS_LIST);
        shareUserLocation = sharedPreferences.getBoolean(Constants.SHARE_LOCATION_KEY, true);
        mobileNumber = sharedPreferences.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");
        deviceToken = sharedPreferences.getString(Constants.DEVICE_TOKEN_KEY, "");

        versionPreference = findPreference(getString(R.string.version_key));
        shareDateOfBirthPreference = (CheckBoxPreference) findPreference(getString(R.string.share_dob_key));
        shareLocationPreference = (CheckBoxPreference) findPreference(getString(R.string.share_location_key));

        shareLocationPreference.setChecked(shareUserLocation);

        if (publicAccessList.containsAll(dobAccessList)) {
            shareDateOfBirthPreference.setChecked(true);
            publicAccessList.removeAll(dobAccessList);
        } else {
            shareDateOfBirthPreference.setChecked(false);
        }

        shareDateOfBirthPreference.setOnPreferenceChangeListener(this);
        shareLocationPreference.setOnPreferenceChangeListener(this);


        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionPreference.setSummary(getString(R.string.app_version, packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (newValue instanceof Boolean) {
            if (Utilities.isConnectionAvailable(getActivity())) {
                switch (preference.getKey()) {
                    case Constants.SHARE_DOB_KEY:
                        if ((Boolean) newValue) {
                            publicAccessList.addAll(dobAccessList);
                        } else {
                            publicAccessList.removeAll(dobAccessList);
                        }
                        ProfileInfoUpdateRequest profileInfoUpdateRequest = new ProfileInfoUpdateRequest();
                        profileInfoUpdateRequest.setPublicAccessList(publicAccessList);
                        if (userProfileSettingsTask == null) {
                            String jsonBody = gson.toJson(profileInfoUpdateRequest);
                            userProfileSettingsTask = new HttpRequestPostAsyncTask(ApiConstants.Command.PROFILE_UPDATE,
                                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.PROFILE_MODULE + String.format(ApiConstants.EndPoint.PROFILE_DETAILS_ENDPOINT, mobileNumber), jsonBody, getActivity(), this);
                            userProfileSettingsTask.addHeader(ApiConstants.Header.ACCESS_TOKEN, accessToken);
                            userProfileSettingsTask.addHeader(ApiConstants.Header.DEVICE_TOKEN, deviceToken);
                            userProfileSettingsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            progressDialog.show();
                        }
                        break;
                    case Constants.SHARE_LOCATION_KEY:
                        shareUserLocation = (Boolean) newValue;
                        LocationSearchableUpdateRequest locationSearchableUpdateRequest = new LocationSearchableUpdateRequest(shareUserLocation);
                        if (userLocationSettingsTask == null) {
                            String jsonBody = gson.toJson(locationSearchableUpdateRequest);
                            userLocationSettingsTask = new HttpRequestPutAsyncTask(ApiConstants.Command.SHARE_LOCATION_SETTINGS_UPDATE,
                                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.LOCATION_MODULE + String.format(ApiConstants.EndPoint.USER_LOCATION_SETTINGS_UPDATE_ENDPOINT, mobileNumber), jsonBody, getActivity(), this);
                            userLocationSettingsTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
                            userLocationSettingsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            progressDialog.show();
                        }
                        break;
                }
            } else {
                if (preference instanceof CheckBoxPreference) {
                    Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    @Override
    public void httpResponseReceiver(@Nullable HttpResponseObject result) {
        progressDialog.cancel();
        if (result == null) {
            userProfileSettingsTask = null;
            progressDialog.cancel();
            Toast.makeText(getActivity(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        switch (result.getStatus()) {
            case ApiConstants.StatusCode.S_OK:
            case ApiConstants.StatusCode.S_ACCEPTED:
            case ApiConstants.StatusCode.S_NO_CONTENT:
                switch (result.getApiCommand()) {
                    case ApiConstants.Command.PROFILE_UPDATE:
                        userProfileSettingsTask = null;
                        if (publicAccessList.containsAll(dobAccessList)) {
                            shareDateOfBirthPreference.setChecked(true);
                        } else {
                            shareDateOfBirthPreference.setChecked(false);
                        }
                        sharedPreferences.edit().putStringSet(Constants.USER_PUBLIC_LIST_KEY, publicAccessList).apply();
                        break;
                    case ApiConstants.Command.SHARE_LOCATION_SETTINGS_UPDATE:
                        userLocationSettingsTask = null;
                        shareLocationPreference.setChecked(shareUserLocation);
                        sharedPreferences.edit().putBoolean(Constants.SHARE_LOCATION_KEY, shareUserLocation).apply();
                        break;
                }
                break;
            case ApiConstants.StatusCode.S_FORBIDDEN:
                userProfileSettingsTask = null;
                userLocationSettingsTask = null;
                progressDialog.cancel();
                break;
            case ApiConstants.StatusCode.S_BAD_REQUEST:
            case ApiConstants.StatusCode.S_NOT_ACCEPTED:
                userProfileSettingsTask = null;
                userLocationSettingsTask = null;
                progressDialog.cancel();
                Toast.makeText(getActivity(), R.string.failed_loading_district_list, Toast.LENGTH_LONG).show();
                break;
            case ApiConstants.StatusCode.S_NOT_FOUND:
            default:
                userProfileSettingsTask = null;
                userLocationSettingsTask = null;
                progressDialog.cancel();
                Toast.makeText(getActivity(), R.string.server_down, Toast.LENGTH_LONG).show();
                break;

        }
    }
}