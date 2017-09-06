package com.priyo.go.Activities.Contacts;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.priyo.go.Api.GetFriendsAsyncTask;
import com.priyo.go.Fragments.Contact.ContactsAllFragment;
import com.priyo.go.Fragments.Contact.ContactStrangerFragment;
import com.priyo.go.Model.Friend.StrangerNode;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;

import java.util.ArrayList;


public class ContactActivity extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSION = 1001;
    private ProgressDialog mProgressDialog;
    private CheckBox mAllContactsSelector;
    private CheckBox miPayContactsSelector;
    private ContactsAllFragment mAllContactsFragment;

    public static ArrayList<StrangerNode> mStrangerContactList = new ArrayList<StrangerNode>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_stranger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        mAllContactsSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.hideKeyboard(ContactActivity.this);
                switchToAllContacts();
            }
        });

        miPayContactsSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.hideKeyboard(ContactActivity.this);
                switchToStrangersContact();
            }
        });

        Utilities.hideKeyboard(ContactActivity.this);
        switchToAllContacts();
    }

    private void initView(){
//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.progress_dialog_text_fetching_stranger));

        mAllContactsSelector = (CheckBox) findViewById(R.id.checkbox_contacts_all);
        miPayContactsSelector = (CheckBox) findViewById(R.id.checkbox_contacts_ipay);
    }

    private void switchToAllContacts() {
        mAllContactsSelector.setChecked(true);
        miPayContactsSelector.setChecked(false);
        mAllContactsSelector.setTextColor(getApplicationContext().getResources().getColor(android.R.color.white));
        miPayContactsSelector.setTextColor(getApplicationContext().getResources().getColor(R.color.colorTextPrimary));

        try {
            if (getApplicationContext() != null) {
                if (mAllContactsFragment == null) {
                    mAllContactsFragment = new ContactsAllFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mAllContactsFragment).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchToStrangersContact() {
        mAllContactsSelector.setChecked(false);
        miPayContactsSelector.setChecked(true);

        mAllContactsSelector.setTextColor(getApplicationContext().getResources().getColor(R.color.colorTextPrimary));
        miPayContactsSelector.setTextColor(getApplicationContext().getResources().getColor(android.R.color.white));
        switchToStrangerAllFragment();
    }

    public void switchToStrangerAllFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ContactStrangerFragment()).addToBackStack(null).commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                for (int i = 0; i < permissions.length; i++) {
                    Log.w(permissions[i], grantResults[i] + "");

                    if (permissions[i].equals(Manifest.permission.READ_CONTACTS)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            SharedPreferences pref = this.getSharedPreferences(Constants.ApplicationTag, this.MODE_PRIVATE);
                            String mobileNumber = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
                            new GetFriendsAsyncTask(this,mobileNumber).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                }

                break;
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


}

