package com.priyo.go.view.activity.business;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.Model.Business.ProfileDetailsNode;
import com.priyo.go.Model.Business.PropertiesNode;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;

import java.util.ArrayList;

public class BusinessDetailsActivity extends AppCompatActivity implements HttpResponseListener, OnMapReadyCallback {

    private HttpRequestGetAsyncTask mBusinessDetailsTask = null;
    private ProfileDetailsNode mBusinessDetailsResponseModel;

    private LinearLayout mRootView;
    private ProfileImageView mBusinessIconView;
    private ImageView mCoverPicView;
    private TextView mNameView;
    private ProgressDialog mProgressDialog;
    private SupportMapFragment mMapFragment;

    private GoogleMap mMap;

    private String mNodeId = "";
    private String mNodeTitle = "";
    private double lat = 0.0;
    private double lon = 0.0;
    private String mNameMap = "", mDescMap = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        Intent i = getIntent();
        mNodeId = i.getStringExtra("nodeID");
        mNodeTitle = i.getStringExtra("nodeTitle");

        fetchBusinessDetails(mNodeId);

    }

    private void initViews() {
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        mMapFragment.getView().setVisibility(View.GONE);
        mRootView = (LinearLayout) findViewById(R.id.root);
        mNameView = (TextView) findViewById(R.id.name_text_view);
        mBusinessIconView = (ProfileImageView) findViewById(R.id.profile_picture_image_view);
        mBusinessIconView.setPlaceHolder(R.mipmap.buld);
        mCoverPicView = (ImageView) findViewById(R.id.placeImage);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching Business Details...");

//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

    }

    private void fetchBusinessDetails(String nodeId) {
        mProgressDialog.show();
        mBusinessDetailsTask = new HttpRequestGetAsyncTask(Constants.COMMAND_GET_BUSINESS_DETAILS,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + "business?nodeID=" + nodeId, getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.ApplicationTag, MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");
        mBusinessDetailsTask.addHeader("token", accessToken);
        mBusinessDetailsTask.mHttpResponseListener = this;
        mBusinessDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void createPage(ProfileDetailsNode mBusinessList) {
        try {
            ProfileDetailsNode test = mBusinessList;
            String name = test.getNodeTitle();
            ArrayList<PropertiesNode> properties = test.getProperties();
            for (int i = 0; i < properties.size(); i++) {
                PropertiesNode test2 = properties.get(i);
                String display = test2.getDisplay();
                String value = test2.getValue();

                if (!value.equals("") && value != null && !value.equals("null") && test2.getKey().equals("latitude"))
                    lat = Double.parseDouble(test2.getValue());
                else if (!value.equals("") && value != null && !value.equals("null") && test2.getKey().equals("longitude"))
                    lon = Double.parseDouble(test2.getValue());
                else if (!value.equals("") && value != null && !value.equals("null") && test2.getKey().equals("name")) {
                    mNameMap = test2.getValue();
                    mNameView.setText(mNameMap);
                } else if (!value.equals("") && value != null && !value.equals("null") && test2.getKey().equals("photo")) {
                    String nameMap = test2.getValue();
                    mBusinessIconView.setPlaceHolder(R.mipmap.buld);
                    mBusinessIconView.setOval(false);
                    if (!TextUtils.isEmpty(nameMap))
                        mBusinessIconView.setProfilePicture(nameMap, true);
                    else
                        mBusinessIconView.setProfilePicture("", true);

//                    mBusinessIconView.setProfilePicture(nameMap, false);
                } else if (!value.equals("") && value != null && !value.equals("null") && test2.getKey().equals("coverPhoto")) {
                    String nameMap = test2.getValue();
                    Glide.with(getApplicationContext())
                            .load(nameMap)
                            .placeholder(R.mipmap.business_avater)
                            .error(R.mipmap.business_avater)
                            .crossFade()
                            .dontAnimate()
                            .into(mCoverPicView);
                } else if (!value.equals("") && value != null && !value.equals("null") && test2.getKey().equals("description")) {
                    mDescMap = test2.getValue();
                    LayoutInflater inflater = (LayoutInflater) this.getSystemService
                            (Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout testL = (LinearLayout) inflater.inflate(R.layout.item_details, null);
                    TextView keyText = (TextView) testL.findViewById(R.id.title_text_view);
                    TextView valueText = (TextView) testL.findViewById(R.id.value_text_view);
                    keyText.setText("Description");
                    valueText.setText(mDescMap);
                    mRootView.addView(testL);
                } else if (!value.equals("") && value != null && !value.equals("null") && test2.getType().equals("property") && !test2.getKey().equals("bengaliName") && !test2.getKey().equals("formalName")) {
                    LayoutInflater inflater = (LayoutInflater) this.getSystemService
                            (Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout testL = (LinearLayout) inflater.inflate(R.layout.item_details, null);
                    TextView keyText = (TextView) testL.findViewById(R.id.title_text_view);
                    TextView valueText = (TextView) testL.findViewById(R.id.value_text_view);
                    keyText.setText(display);
                    valueText.setText(value);
                    mRootView.addView(testL);
                }
            }
            if (mNodeTitle.equals("Business") && lat != 0.0 && lon != 0.0) {
                mMapFragment.getView().setVisibility(View.VISIBLE);
                mMapFragment.getMapAsync(this);
            } else
                mMapFragment.getView().setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void httpResponseReceiver(HttpResponseObject result) {

        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            mProgressDialog.dismiss();
            mBusinessDetailsTask = null;
            if (getApplicationContext() != null)
                Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        switch (result.getApiCommand()) {

            case Constants.COMMAND_GET_BUSINESS_DETAILS:
                try {

                    String message = result.getJsonString();
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        mBusinessDetailsResponseModel = gson.fromJson(result.getJsonString(), ProfileDetailsNode.class);
                        createPage(mBusinessDetailsResponseModel);

                    } else {
                        if (getApplicationContext() != null)
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (getApplicationContext() != null)
                        Toast.makeText(getApplicationContext(), R.string.failed_msg_common, Toast.LENGTH_LONG).show();
                }

                mProgressDialog.dismiss();
                mBusinessDetailsTask = null;

                break;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng currentLoc = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(currentLoc).title(mNameMap));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14.0f));
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
