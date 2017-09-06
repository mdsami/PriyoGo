package com.priyo.go.view.fragment.signup;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Activities.News.PriyoNewsTabActivity;
import com.priyo.go.Api.ApiForbiddenResponse;
import com.priyo.go.Api.HttpRequestPostAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.BroadcastReceiverClass.EnableDisableSMSBroadcastReceiver;
import com.priyo.go.BroadcastReceiverClass.SMSReaderBroadcastReceiver;
import com.priyo.go.Model.DeviceInformation;
import com.priyo.go.Model.MetaData;
import com.priyo.go.Model.ProfileInfo;
import com.priyo.go.Model.UserLocation;
import com.priyo.go.Model.api.graph.response.AddPhoneBookResponse;
import com.priyo.go.Model.api.profile.request.SignupInitRequest;
import com.priyo.go.Model.api.profile.request.SignupRequest;
import com.priyo.go.Model.api.profile.response.SignupInitResponse;
import com.priyo.go.Model.api.profile.response.SignupResponse;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;
import com.priyo.go.service.FCMUpdateService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("MissingPermission")
public class SignupFragment extends Fragment implements LocationListener, HttpResponseListener, View.OnClickListener {

    private static final String TAG = SignupFragment.class.getSimpleName();

    private static final long DEFAULT_COUNTDOWN_TIME = 120000;
    private static final Gson gson = new GsonBuilder().create();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int OPEN_LOCATION_SETTINGS_REQUEST_CODE = 1002;
    private static final int OPEN_APP_PERMISSION_SETTINGS_REQUEST_CODE = 1003;

    SharedPreferences pref;

    private HttpRequestPostAsyncTask mSignUpTask = null;
    private HttpRequestPostAsyncTask mSignUpInitTask = null;

    private View rootView;
    private Button mOTPVerificationButton;
    private Button mOTPResendButton;
    private EditText mOTPEditText;
    private TextView mTimerTextView;
    private ProgressDialog mProgressDialog;

    private String otp;
    private String mUserMobileNumber;
    private String mUserFullName;
    private String mDeviceToken;
    private String mAccessToken;

    private ProfileInfo profileInfo;

    private int triesLeft;
    private SimpleDateFormat counterTimeFormat = new SimpleDateFormat("mm:ss", Locale.US);
    private EnableDisableSMSBroadcastReceiver mEnableDisableSMSBroadcastReceiver;
    private LocationManager locationManager;

    public SignupFragment() {
        Log.d(TAG, "SignupFragment()");
    }

    public static SignupFragment newInstance(Bundle bundle) {
        Log.d(TAG, "newInstance(Bundle bundle)");
        SignupFragment newFragmentInstance = new SignupFragment();
        newFragmentInstance.setArguments(bundle);
        return newFragmentInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(@Nullable Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);

        //initializing Location Manager
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        //Reading Values for SignUp Complete Request.
        if (getArguments() != null) {
            mUserMobileNumber = getArguments().getString(Constants.USER_MOBILE_NUMBER_KEY);
            mUserFullName = getArguments().getString(Constants.USER_FULL_NAME_KEY);
            triesLeft = getArguments().getInt(Constants.OTP_TRIES_LEFT_KEY);
        }
        pref = getActivity().getSharedPreferences(Constants.ApplicationTag, Activity.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");

        // Inflate the layout for this fragment
        if (rootView == null)
            rootView = inflater.inflate(R.layout.fragment_otp_verification, container, false);

        //Initialing The View Elements
        mOTPVerificationButton = (Button) rootView.findViewById(R.id.otp_verification_button);
        mOTPResendButton = (Button) rootView.findViewById(R.id.otp_resend_button);
        mTimerTextView = (TextView) rootView.findViewById(R.id.timer_text_view);
        mOTPEditText = (EditText) rootView.findViewById(R.id.otp_edit_text);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);

        mOTPResendButton.setOnClickListener(this);
        mOTPVerificationButton.setOnClickListener(this);


        Utilities.showKeyboard(getActivity(), mOTPEditText);


        //enable broadcast receiver to get the text message to get the OTP
        mEnableDisableSMSBroadcastReceiver = new EnableDisableSMSBroadcastReceiver();
        mEnableDisableSMSBroadcastReceiver.enableBroadcastReceiver(getContext(), new SMSReaderBroadcastReceiver.OnTextMessageReceivedListener() {
            @Override
            public void onTextMessageReceive(String otp) {
                mOTPEditText.setText(otp);
                mOTPVerificationButton.performClick();
            }
        });
        startCounter(DEFAULT_COUNTDOWN_TIME);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        mEnableDisableSMSBroadcastReceiver.disableBroadcastReceiver(getContext());
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_LOCATION_SETTINGS_REQUEST_CODE:
                if (locationManager != null) {
                    checkIsLocationServiceEnabled();
                }
                break;
            case OPEN_APP_PERMISSION_SETTINGS_REQUEST_CODE:
                if (canAttemptSignUp())
                    attemptSignUp();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case LOCATION_PERMISSION_REQUEST_CODE:
                    attemptSignUp();
                    break;
            }
        } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            switch (requestCode) {
                case LOCATION_PERMISSION_REQUEST_CODE:
                    if (canAttemptSignUp()) {
                        doSignUpRequest(new UserLocation());
                    }
                    break;
            }
        }
    }

    @Override
    public void httpResponseReceiver(@Nullable HttpResponseObject result) {

        if (result == null) {
            mProgressDialog.dismiss();
            mSignUpTask = null;
            mSignUpInitTask = null;
            if (getActivity() != null)
                Toast.makeText(getActivity(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        //For showing message received from the server
        String message = "";
        try {
            //Switch case to check Response Status and perform necessary actions for those status.
            switch (result.getStatus()) {
                case ApiConstants.StatusCode.S_OK:
                case ApiConstants.StatusCode.S_ACCEPTED:
                    mProgressDialog.dismiss();

                    switch (result.getApiCommand()) {
                        case ApiConstants.Command.SIGN_UP_INIT:

                            SignupInitResponse signupInitResponse = gson.fromJson(result.getJsonString(), SignupInitResponse.class);
                            triesLeft = signupInitResponse.getTriesLeft();
                            message = getString(R.string.otp_going_to_send);
                            startCounter(DEFAULT_COUNTDOWN_TIME);
                            mProgressDialog.cancel();
                            nullAllRequestTask();
                            break;
                        case ApiConstants.Command.SIGN_UP:

                            SignupResponse mSignupResponse = gson.fromJson(result.getJsonString(), SignupResponse.class);
                            mDeviceToken = mSignupResponse.getDeviceToken();
                            mAccessToken = mSignupResponse.getToken();
                            profileInfo = mSignupResponse.getProfileInfo();
                            saveUserInfo("");
                            //attemptAddToPhoneBook();

                            break;
                        case ApiConstants.Command.PHONEBOOK_ADD:

                            AddPhoneBookResponse mAddPhoneBookResponse = gson.fromJson(result.getJsonString(), AddPhoneBookResponse.class);
                            saveUserInfo(Integer.toString(mAddPhoneBookResponse.getNodeID()));


                            mProgressDialog.cancel();
                            message = getString(R.string.login_success);
                            break;
                    }

                    break;
                case ApiConstants.StatusCode.S_BAD_REQUEST:
                    mProgressDialog.cancel();
                    message = result.getJsonString();
                    break;
                case ApiConstants.StatusCode.S_FORBIDDEN:
                    mProgressDialog.cancel();
                    nullAllRequestTask();
                    ApiForbiddenResponse apiForbiddenResponse = gson.fromJson(result.getJsonString(), ApiForbiddenResponse.class);
                    message = apiForbiddenResponse.getDetail();

                    break;
                case ApiConstants.StatusCode.S_NOT_ACCEPTED:
                case ApiConstants.StatusCode.S_SERVER_ERROR:
                    nullAllRequestTask();
                    mProgressDialog.cancel();
                    message = getString(R.string.can_not_request);

                    break;
                case ApiConstants.StatusCode.S_NOT_FOUND:
                default:
                    mProgressDialog.cancel();
                    nullAllRequestTask();
                    message = getString(R.string.server_down);

                    break;
            }
        } catch (Exception e) {
            message = getString(R.string.server_down);
        } finally {
            if (!TextUtils.isEmpty(message))
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void switchToNewsTabActivity() {
        Intent intent = new Intent(getActivity(), PriyoNewsTabActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void saveUserInfo(String nodeId) {
        pref.edit().putString(Constants.USER_MOBILE_NUMBER_KEY, mUserMobileNumber).apply();
        pref.edit().putString(Constants.USER_FULL_NAME_KEY, mUserFullName).apply();
        pref.edit().putString(Constants.DEVICE_TOKEN_KEY, mDeviceToken).apply();
        pref.edit().putString(Constants.ACCESS_TOKEN_KEY, mAccessToken).apply();
        pref.edit().putString(Constants.USER_NODE_ID_KEY, nodeId).apply();
        pref.edit().putLong("lastUpdate", System.currentTimeMillis()).apply();
        if (profileInfo != null) {
            if (!TextUtils.isEmpty(profileInfo.getName()))
                pref.edit().putString(Constants.USER_FULL_NAME_KEY, profileInfo.getName()).apply();
            if (!TextUtils.isEmpty(profileInfo.getPhotoUri()))
                pref.edit().putString(Constants.USER_PHOTO_URL_KEY, ApiConstants.Url.IMAGE_BASE_URI + profileInfo.getPhotoUri()).apply();
            if (profileInfo.getDateOfBirthDate() != null)
                pref.edit().putLong(Constants.USER_DATE_OF_BIRTH_KEY, profileInfo.getDateOfBirthDate().getDate()).apply();
            if (!TextUtils.isEmpty(profileInfo.getDistrict()))
                pref.edit().putString(Constants.USER_DISTRICT_KEY, profileInfo.getDistrict()).apply();
            if (!TextUtils.isEmpty(profileInfo.getThana()))
                pref.edit().putString(Constants.USER_THANA_KEY, profileInfo.getThana()).apply();
            if (!TextUtils.isEmpty(profileInfo.getOrganizationName()))
                pref.edit().putString(Constants.USER_ORGANIZATION_KEY, profileInfo.getOrganizationName()).apply();
            if (!TextUtils.isEmpty(profileInfo.getProfession()))
                pref.edit().putString(Constants.USER_PROFESSION_KEY, profileInfo.getProfession()).apply();
            if (!TextUtils.isEmpty(profileInfo.getGender()))
                pref.edit().putString(Constants.USER_GENDER_KEY, profileInfo.getGender()).apply();
            if (!TextUtils.isEmpty(profileInfo.getDescription()))
                pref.edit().putString(Constants.USER_DESCRIPTION_KEY, profileInfo.getDistrict()).apply();
        }
        boolean isFcmTokenUpdate = pref.getBoolean(Constants.IS_FCM_TOKEN_UPDATED_KEY, false);
        if (isFcmTokenUpdate) {
            getActivity().startService(new Intent(getActivity(), FCMUpdateService.class));
        }
        switchToNewsTabActivity();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.otp_resend_button:
                if (Utilities.isConnectionAvailable(getActivity())) resendOTP();
                else if (getActivity() != null)
                    Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                break;
            case R.id.otp_verification_button:
                Utilities.hideKeyboard(getActivity());
                if (canAttemptSignUp()) {
                    if (Utilities.hasPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                        attemptSignUp();
                    } else {
                        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                    }
                }

                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);
        pref.edit().putFloat(Constants.LONGITUDE_KEY, (float) location.getLongitude()).apply();
        pref.edit().putFloat(Constants.LATITUDE_KEY, (float) location.getLatitude()).apply();

        if (mSignUpTask != null) {
            UserLocation userLocation = new UserLocation(location.getLatitude(), location.getLongitude());
            doSignUpRequest(userLocation);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void checkIsLocationServiceEnabled() {
        if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            doSignUpRequest(new UserLocation(0.0, 0.0));
        } else {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                getUserLocation(LocationManager.NETWORK_PROVIDER);
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getUserLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }

    private void nullAllRequestTask() {
        mSignUpInitTask = null;
        mSignUpTask = null;
    }

    private void startCounter(long timer) {
        mTimerTextView.setVisibility(View.VISIBLE);
        mOTPResendButton.setEnabled(false);
        new CountDownTimer(timer, 1000) {

            public void onTick(long millisUntilFinished) {
                mTimerTextView.setText(counterTimeFormat.format(new Date(millisUntilFinished)));
            }

            public void onFinish() {
                mTimerTextView.setVisibility(View.INVISIBLE);
                mOTPResendButton.setEnabled(true);
            }
        }.start();
    }

    private void resendOTP() {

        if (mSignUpInitTask != null) {
            return;
        }

        mProgressDialog.setMessage(getString(R.string.progress_dialog_text_sending_sms));
        mProgressDialog.show();

        SignupInitRequest mSignupInitRequest = new SignupInitRequest(mUserMobileNumber);

        final String jsonBody = gson.toJson(mSignupInitRequest);

        mSignUpInitTask = new HttpRequestPostAsyncTask(ApiConstants.Command.SIGN_UP_INIT,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.PROFILE_MODULE + ApiConstants.EndPoint.SIGN_UP_INIT_ENDPOINT, jsonBody, getActivity());

        mSignUpInitTask.mHttpResponseListener = this;
        mSignUpInitTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean canAttemptSignUp() {
        if (!Utilities.isConnectionAvailable(getContext())) {
            Log.w(TAG, "No Internet Connectivity");

            if (getActivity() != null)
                Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();

            return false;
        } else if (mSignUpTask != null) {
            Log.w(TAG, "Already another OTP Verification request is in Queue.");
            return false;
        } else {
            Log.d(TAG, "Gathering User Inputs");
            this.otp = mOTPEditText.getText().toString().trim();
            if (TextUtils.isEmpty(otp)) {
                mOTPEditText.setError(getActivity().getString(R.string.error_invalid_otp));
                mOTPEditText.requestFocus();
                return false;
            } else {
                return true;
            }
        }
    }


    private void attemptSignUp() {
        if (locationManager != null) {
            if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doSignUpRequest(new UserLocation(0.0, 0.0));
                    }
                }).setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), OPEN_LOCATION_SETTINGS_REQUEST_CODE);
                    }
                }).setMessage("For better service, please turn on Location service.").show();
            } else {
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    getUserLocation(LocationManager.NETWORK_PROVIDER);
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getUserLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
    }

    private void getUserLocation(String provider) {
        mProgressDialog.setMessage(getString(R.string.progress_dialog_text_logging_in));
        mProgressDialog.show();
        locationManager.requestLocationUpdates(provider, 1000, 100, this);
        new CountDownTimer(5 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                locationManager.removeUpdates(SignupFragment.this);
                Location location = Utilities.getLastKnownLocation(locationManager);
                UserLocation userLocation = new UserLocation();
                if (location != null)
                    userLocation = new UserLocation(location.getLatitude(), location.getLongitude());
                doSignUpRequest(userLocation);
            }
        }.start();
    }

    private void doSignUpRequest(UserLocation userLocation) {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(getString(R.string.progress_dialog_text_logging_in));
            mProgressDialog.show();
        }
        DeviceInformation deviceInformation = Utilities.getDeviceInformation(getContext());

        pref.edit().putFloat(Constants.LATITUDE_KEY, (float) userLocation.getLatitude()).apply();
        pref.edit().putFloat(Constants.LONGITUDE_KEY, (float) userLocation.getLongitude()).apply();

        final SignupRequest mSignupRequest = new SignupRequest(mUserMobileNumber, mUserFullName, this.otp, deviceInformation);

        final Gson gson = new GsonBuilder().create();
        try {
            final String signUpBodyJson = gson.toJson(mSignupRequest);
            final String metaDataJson = gson.toJson(new MetaData(1, userLocation));
            final String metaDataEncode = new String(Base64.encode(metaDataJson.getBytes(), Base64.DEFAULT)).replaceAll("\n", "").trim();
            mSignUpTask = new HttpRequestPostAsyncTask(ApiConstants.Command.SIGN_UP,
                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.PROFILE_MODULE + ApiConstants.EndPoint.SIGN_UP_ENDPOINT, signUpBodyJson, getActivity());
            mSignUpTask.addHeader(ApiConstants.Header.META_DATA, metaDataEncode);
            mSignUpTask.mHttpResponseListener = this;
            mSignUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
        }
    }
}

