package com.priyo.go.view.activity.wish;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Api.ApiForbiddenResponse;
import com.priyo.go.Api.HttpRequestPostAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.api.utility.request.WishAddRequest;
import com.priyo.go.Model.api.utility.response.WishAddResponse;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;
import com.priyo.go.view.fragment.wish.WishAllFragment;
import com.priyo.go.view.fragment.wish.WishFulfilledFragment;
import com.priyo.go.view.fragment.wish.WishPersonalFragment;

import java.util.Date;

public class WishListActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener, DialogInterface.OnClickListener, HttpResponseListener {


    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
    private FloatingActionButton wishListFab;
    private TabLayout wishListTabLayout;
    private ViewPager wishListViewPager;
    private WishListPagerAdapter wishListPagerAdapter;
    private ProgressDialog progressDialog;
    private AlertDialog makeAWishDialog;
    private EditText userWishEditText;
    private String mobileNumber;
    private String name;
    private String accessToken;
    private HttpRequestPostAsyncTask userWishRequestTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        wishListFab = (FloatingActionButton) findViewById(R.id.wish_list_fab);
        wishListTabLayout = (TabLayout) findViewById(R.id.wish_list_tab_layout);
        wishListViewPager = (ViewPager) findViewById(R.id.wish_list_view_pager);
        wishListPagerAdapter = new WishListPagerAdapter(getSupportFragmentManager());

        buildMakeAWishDialog();

        wishListViewPager.setAdapter(wishListPagerAdapter);
        wishListViewPager.setOffscreenPageLimit(3);
        wishListViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(wishListTabLayout));
        wishListViewPager.addOnPageChangeListener(this);

        wishListTabLayout.setupWithViewPager(wishListViewPager, true);
        wishListTabLayout.addOnTabSelectedListener(this);
        wishListFab.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending your wish...");

        final SharedPreferences sharedPreferences = getSharedPreferences(Constants.ApplicationTag, MODE_PRIVATE);
        mobileNumber = sharedPreferences.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        name = sharedPreferences.getString(Constants.USER_FULL_NAME_KEY, "");
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");

    }

    private void buildMakeAWishDialog() {
        AlertDialog.Builder makeAWishDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_make_a_wish, null);
        makeAWishDialogBuilder.setView(dialogView);

        userWishEditText = (EditText) dialogView.findViewById(R.id.user_wish_edit_text);

        makeAWishDialogBuilder.setPositiveButton("MAKE WISH", this);
        makeAWishDialogBuilder.setNegativeButton("CANCEL", this);
        makeAWishDialogBuilder.setTitle("Make a Wish");
        makeAWishDialogBuilder.setCancelable(false);

        makeAWishDialog = makeAWishDialogBuilder.create();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wish_list_fab:
                makeAWishDialog.show();
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Utilities.hideKeyboard(this);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (makeAWishDialog.isShowing()) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    if (!TextUtils.isEmpty(userWishEditText.getText())) {
                        if (userWishEditText.getText().length() > 3) {

                            WishAddRequest wishAddRequest = new WishAddRequest();
                            wishAddRequest.setName(name);
                            wishAddRequest.setTitle("");
                            wishAddRequest.setWish(userWishEditText.getText().toString());
                            wishAddRequest.setMobileNumber(mobileNumber);
                            wishAddRequest.setCreatedAt(new Date(System.currentTimeMillis()));

                            progressDialog.show();

                            String json = gson.toJson(wishAddRequest);

                            userWishRequestTask = new HttpRequestPostAsyncTask(ApiConstants.Command.USER_WISH_ADD_REQUEST,
                                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.UTILITY_MODULE + ApiConstants.EndPoint.USER_WISH_ADD_ENDPOINT, json, this);
                            userWishRequestTask.mHttpResponseListener = this;
                            userWishRequestTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
                            userWishRequestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            userWishEditText.setError("Invalid wish");
                            userWishEditText.requestFocus();
                        }
                    } else {
                        userWishEditText.setError("Please write your wish");
                        userWishEditText.requestFocus();
                    }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    }

    @Override
    public void httpResponseReceiver(@Nullable HttpResponseObject result) {
        progressDialog.dismiss();
        makeAWishDialog.dismiss();
        userWishRequestTask = null;
        if (result == null) {
            Toast.makeText(this, R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (result.getStatus()) {
            case ApiConstants.StatusCode.S_OK:
            case ApiConstants.StatusCode.S_ACCEPTED:
                WishAddResponse wishAddResponse = gson.fromJson(result.getJsonString(), WishAddResponse.class);
                if (wishAddResponse.isSuccess())
                    Toast.makeText(this, "Wish Added", Toast.LENGTH_SHORT).show();
                break;
            case ApiConstants.StatusCode.S_UNAUTHORIZED:
            case ApiConstants.StatusCode.S_FORBIDDEN:
                ApiForbiddenResponse apiForbiddenResponse = gson.fromJson(result.getJsonString(), ApiForbiddenResponse.class);
                if (!TextUtils.isEmpty(apiForbiddenResponse.getDetail())) {
                    Toast.makeText(getApplicationContext(), apiForbiddenResponse.getDetail(), Toast.LENGTH_LONG).show();
                } else if (!TextUtils.isEmpty(apiForbiddenResponse.getMessage())) {
                    Toast.makeText(getApplicationContext(), apiForbiddenResponse.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.unauthorized, Toast.LENGTH_LONG).show();
                }
                break;
            case ApiConstants.StatusCode.S_NOT_FOUND:
            case ApiConstants.StatusCode.S_SERVER_ERROR:
                Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                break;
        }


    }

    private class WishListPagerAdapter extends FragmentPagerAdapter {

        private static final int TOTAL_FRAGMENT_COUNT = 3;

        private static final int WISH_FULFILL_FRAGMENT_POSITION = 0;
        private static final int MY_WISH_FRAGMENT_POSITION = 1;
        private static final int ALL_WISH_FRAGMENT_POSITION = 2;

        private static final String WISH_FULFILL_TAB_TITLE = "FULFILLED";
        private static final String MY_WISH_FRAGMENT_TAB_TITLE = "MY WISH";
        private static final String ALL_WISH_FRAGMENT_TAB_TITLE = "PEOPLE'S WISH";


        private WishFulfilledFragment wishFulfilledFragment;
        private WishPersonalFragment wishPersonalFragment;
        private WishAllFragment wishAllFragment;

        public WishListPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            wishFulfilledFragment = new WishFulfilledFragment();
            wishPersonalFragment = new WishPersonalFragment();
            wishAllFragment = new WishAllFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case WISH_FULFILL_FRAGMENT_POSITION:
                    return wishFulfilledFragment;
                case MY_WISH_FRAGMENT_POSITION:
                    return wishPersonalFragment;
                case ALL_WISH_FRAGMENT_POSITION:
                    return wishAllFragment;
                default:
                    return new Fragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case WISH_FULFILL_FRAGMENT_POSITION:
                    return WISH_FULFILL_TAB_TITLE;
                case MY_WISH_FRAGMENT_POSITION:
                    return MY_WISH_FRAGMENT_TAB_TITLE;
                case ALL_WISH_FRAGMENT_POSITION:
                    return ALL_WISH_FRAGMENT_TAB_TITLE;
                default:
                    return "";
            }
        }

        @Override
        public int getCount() {
            return TOTAL_FRAGMENT_COUNT;
        }
    }
}
