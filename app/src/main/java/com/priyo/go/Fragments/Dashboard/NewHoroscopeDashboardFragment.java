package com.priyo.go.Fragments.Dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Api.ApiForbiddenResponse;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.utility.Horoscope;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by sajid.shahriar on 4/30/17.
 */

public class NewHoroscopeDashboardFragment extends Fragment implements HttpResponseListener {

    private static List<String> horoscopeNameEnglish = Arrays.asList("Aquarius", "Aries", "Cancer", "Capricorn", "Gemini", "Leo", "Libra", "Pisces", "Sagittarius", "Scorpio", "Taurus", "Virgo");
    private static List<String> horoscopeNameBengali = Arrays.asList("কুম্ভ", "মেষ", "কর্কট", " মকর", " মিথুন", "সিংহ ", "তুলা", "মীন", "ধনু", "বৃশ্চিক", "বৃষ", "কন্যা");
    private static List<Integer> horoscopeImage = Arrays.asList(R.mipmap.aquarius, R.mipmap.aries, R.mipmap.cancer, R.mipmap.capricorn, R.mipmap.gemini, R.mipmap.leo, R.mipmap.libra, R.mipmap.pisces, R.mipmap.sagittarius, R.mipmap.scorpius, R.mipmap.taurus, R.mipmap.virgo);

    private Gson gson = new GsonBuilder().create();
    private View rootView;
    private TextView horoscopeSignTextView;
    private TextView horoscopeDetailTextView;
    private TextView noBirthdayTextView;
    private ImageView horoscopeSignImageView;
    private ProgressBar progressBar;


    private long userBirthDate;
    private long currentTime = System.currentTimeMillis();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    private SharedPreferences sharedPreferences;

    private HttpRequestGetAsyncTask userHoroscopeRequest = null;

    private String accessToken;
    private SharedPreferences preferences;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Constants.ApplicationTag, Activity.MODE_PRIVATE);
        userBirthDate = sharedPreferences.getLong(Constants.USER_DATE_OF_BIRTH_KEY, -1);
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(R.layout.fragment_new_horoscope_dashboard, container, false);
        horoscopeSignTextView = (TextView) rootView.findViewById(R.id.horoscope_sign_text_view);
        horoscopeSignImageView = (ImageView) rootView.findViewById(R.id.horoscope_sign_image_view);
        horoscopeDetailTextView = (TextView) rootView.findViewById(R.id.horoscope_detail_text_view);
        noBirthdayTextView = (TextView) rootView.findViewById(R.id.no_birthday_text_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        if (userBirthDate == -1) {
            noBirthdayTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            fetchUserHoroscope();
        }

        return rootView;
    }

    private void fetchUserHoroscope() {
        preferences = getActivity().getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        accessToken = preferences.getString(Constants.ACCESS_TOKEN_KEY, "");

        Map<String, String> params = new HashMap<>();
        params.put("date", dateFormat.format(new Date(currentTime)));
        params.put("dob", dateFormat.format(new Date(userBirthDate)));

        userHoroscopeRequest = new HttpRequestGetAsyncTask(ApiConstants.Command.USER_HOROSCOPE_REQUEST,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.UTILITY_MODULE + ApiConstants.EndPoint.HOROSCOPE_FIND_ENDPOINT,
                getContext(), params, this);
        userHoroscopeRequest.addHeader(ApiConstants.Header.TOKEN, accessToken);
        userHoroscopeRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void httpResponseReceiver(@Nullable HttpResponseObject result) {

        if (result == null) {
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.GONE);
            userHoroscopeRequest = null;
            if (getContext() != null)
                Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        String message = "";

        try {
            switch (result.getStatus()) {
                case ApiConstants.StatusCode.S_OK:
                case ApiConstants.StatusCode.S_ACCEPTED:
                    Horoscope horoscope = gson.fromJson(result.getJsonString(), Horoscope.class);
                    if (horoscope != null) {
                        progressBar.setVisibility(View.GONE);
                        rootView.setVisibility(View.VISIBLE);
                        noBirthdayTextView.setVisibility(View.GONE);
                        int position = horoscopeNameEnglish.indexOf(horoscope.getSign());
                        if (position != -1) {
                            horoscopeSignTextView.setText(horoscopeNameBengali.get(position));
                            horoscopeSignImageView.setImageResource(horoscopeImage.get(position));
                            horoscopeDetailTextView.setText(horoscope.getHoroscope());
                        }
                    } else {
                        throw new Exception();
                    }
                    break;
                case ApiConstants.StatusCode.S_FORBIDDEN:
                    ApiForbiddenResponse apiForbiddenResponse = gson.fromJson(result.getJsonString(), ApiForbiddenResponse.class);
                    message = apiForbiddenResponse.getDetail();
                    break;
                case ApiConstants.StatusCode.S_BAD_REQUEST:
                case ApiConstants.StatusCode.S_NOT_ACCEPTED:
                case ApiConstants.StatusCode.S_NOT_FOUND:
                case ApiConstants.StatusCode.S_SERVER_ERROR:
                    message = getString(R.string.service_not_available);
                    break;
            }
        } catch (Exception e) {
            message = getString(R.string.service_not_available);
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.GONE);
        }

        userHoroscopeRequest = null;
        if (!TextUtils.isEmpty(message))
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();


    }
}
