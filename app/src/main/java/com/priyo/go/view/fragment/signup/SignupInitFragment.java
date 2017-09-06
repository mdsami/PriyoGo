package com.priyo.go.view.fragment.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;
import com.priyo.go.Api.ApiForbiddenResponse;
import com.priyo.go.Api.HttpRequestPostAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Fragments.OnFragmentInteractionListener;
import com.priyo.go.Model.api.profile.request.SignupInitRequest;
import com.priyo.go.Model.api.profile.response.SignupInitResponse;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.ContactEngine;
import com.priyo.go.Utilities.PhoneUtils;
import com.priyo.go.Utilities.Utilities;
import com.priyo.go.view.activity.signup.SignupActivity;

public class SignupInitFragment extends Fragment implements View.OnClickListener, HttpResponseListener {

    private static final String TAG = SignupInitFragment.class.getSimpleName();

    private final Gson gson = new GsonBuilder().create();
    private HttpRequestPostAsyncTask mRequestOTPTask = null;


    private View rootView;
    private EditText mUserMobileNumberEditText;
    private EditText mUserFullNameEditText;
    private Button mOtpRequestButton;

    private String mUserMobileNumber;
    private String mUserFullName;

    private ProgressDialog mProgressDialog;
    private CountryCodePicker countryCodePicker;

    private OnFragmentInteractionListener onFragmentInteractionListener;

    public SignupInitFragment() {
        Log.d(TAG, "SignupInitFragment()");
    }

    public static SignupInitFragment newInstance(Bundle bundle) {
        Log.d(TAG, "newInstance(Bundle bundle)");
        SignupInitFragment newFragmentInstance = new SignupInitFragment();
        newFragmentInstance.setArguments(bundle);
        return newFragmentInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");

        if (rootView == null) {
            Log.d(TAG, "Initializing view for " + TAG);
            rootView = inflater.inflate(R.layout.fragment_login, container, false);
        }

        Log.d(TAG, "Setting up Progress Dialog");
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.progress_dialog_text_sending_sms));

        Log.d(TAG, "Initializing View objects");
        mOtpRequestButton = (Button) rootView.findViewById(R.id.otp_request_button);
        mUserMobileNumberEditText = (EditText) rootView.findViewById(R.id.user_mobile_number_edit_text);
        mUserFullNameEditText = (EditText) rootView.findViewById(R.id.user_full_name_edit_text);
        countryCodePicker = (CountryCodePicker) rootView.findViewById(R.id.country_code_picker);


        Log.d(TAG, "Initializing field values for the views");
        String countryRegion = PhoneUtils.getCountryRegionFromPhone(getContext());
        countryCodePicker.setCountryForNameCode(countryRegion);
        mOtpRequestButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach(Context context)");
        super.onAttach(context);
        if (context != null && context instanceof OnFragmentInteractionListener) {
            try {
                onFragmentInteractionListener = (OnFragmentInteractionListener) context;
            } catch (ClassCastException e) {
                if (e.getMessage() != null) {
                    Log.wtf(TAG, e.getMessage());
                }
            }
        } else {
            Log.w(TAG, "Fragment is not an instance of " + OnFragmentInteractionListener.class.getSimpleName());
        }
    }

    private boolean canPerformOTPRequest() {
        Log.d(TAG, "canPerformOTPRequest()");
        if (!Utilities.isConnectionAvailable(getActivity())) {
            Log.w(TAG, "No Internet Connectivity");
            return false;
        } else if (mRequestOTPTask != null) {
            Log.w(TAG, "Already another OTP request is in Queue.");
            return false;
        } else {
            Log.d(TAG, "Gathering User Inputs");
            mUserFullName = mUserFullNameEditText.getText().toString().trim();
            mUserMobileNumber = mUserMobileNumberEditText.getText().toString().trim();
            String mSelectedCountryNameCode = countryCodePicker.getSelectedCountryNameCode();


            if (TextUtils.isEmpty(mUserFullName)) {
                Log.w(TAG, "Full Name not Entered");
                mUserFullNameEditText.requestFocus();
                mUserFullNameEditText.setError(getString(R.string.empty_user_full_name));
                return false;
            } else if (TextUtils.isEmpty(mUserMobileNumber)) {
                Log.w(TAG, "Mobile Number not Entered");
                mUserMobileNumberEditText.requestFocus();
                mUserMobileNumberEditText.setError(getString(R.string.empty_user_mobile_number));
                return false;
            } else {
                mUserMobileNumber = ContactEngine.formatMobileNumber(mUserMobileNumber, mSelectedCountryNameCode);
                if (!ContactEngine.isValidNumber(mUserMobileNumber, mSelectedCountryNameCode)) {
                    Log.w(TAG, "Mobile Number not valid");
                    mUserMobileNumberEditText.requestFocus();
                    mUserMobileNumberEditText.setError(getString(R.string.error_invalid_mobile_number));
                    return false;
                } else {
                    Log.d(TAG, "All the field value is valid. Ready for OTP Request.");
                    return true;
                }
            }
        }
    }

    private void requestForOTP() {
        Log.d(TAG, "requestForOTP()");

        mProgressDialog.show();

        SignupInitRequest mSignupInitRequest = new SignupInitRequest(mUserMobileNumber);
        String jsonBody = gson.toJson(mSignupInitRequest);

        mRequestOTPTask = new HttpRequestPostAsyncTask(ApiConstants.Command.SIGN_UP_INIT,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.PROFILE_MODULE + ApiConstants.EndPoint.SIGN_UP_INIT_ENDPOINT, jsonBody, getActivity());
        mRequestOTPTask.mHttpResponseListener = this;

        Log.d(TAG, "OTP Request Executed.");
        mRequestOTPTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {
        Log.d(TAG, "httpResponseReceiver(HttpResponseObject result)");

        if (result == null) {
            Log.w(TAG, "null response");
            mProgressDialog.dismiss();

            mRequestOTPTask = null;

            if (getActivity() != null)
                Toast.makeText(getActivity(), R.string.otp_request_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        //For showing message received from the server
        String message = "";
        try {
            Log.d(TAG, "Request Status Code " + result.getStatus());
            //Switch case to check Response Status and perform necessary actions for those status.
            switch (result.getStatus()) {
                case ApiConstants.StatusCode.S_OK:
                case ApiConstants.StatusCode.S_ACCEPTED:
                    switch (result.getApiCommand()) {
                        case ApiConstants.Command.SIGN_UP_INIT:
                            SignupInitResponse signupInitResponse = gson.fromJson(result.getJsonString(), SignupInitResponse.class);
                            message = getString(R.string.otp_going_to_send);
                            switchToOTPVerificationFragment(signupInitResponse);
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
            mRequestOTPTask = null;
            if (!TextUtils.isEmpty(message))
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void switchToOTPVerificationFragment(SignupInitResponse mSignupInitResponse) {
        Log.d(TAG, "switchToOTPVerificationFragment(SignupInitResponse mSignupInitResponse)");
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_MOBILE_NUMBER_KEY, mUserMobileNumber);
        bundle.putString(Constants.USER_FULL_NAME_KEY, mUserFullName);
        bundle.putInt(Constants.OTP_TRIES_LEFT_KEY, mSignupInitResponse.getTriesLeft());

        if (onFragmentInteractionListener != null)
            onFragmentInteractionListener.addToBackStack(SignupActivity.ID_SIGN_UP_FRAGMENT, bundle);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick(View view)");
        switch (view.getId()) {
            case R.id.otp_request_button:
                Log.d(TAG, "OTP Request Button Pressed");
                if (canPerformOTPRequest()) {
                    requestForOTP();
                } else if (getActivity() != null)
                    Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                break;
        }
    }
}

