package com.priyo.go.Activities.News;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.priyo.go.Activities.Contacts.ContactActivity;
import com.priyo.go.Activities.HoroscopeActivity;
import com.priyo.go.Api.HttpRequestPostAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.DatabaseHelper.DataHelper;
import com.priyo.go.Fragments.Dashboard.DashboardNewsFragment;
import com.priyo.go.Fragments.NewsTabed.HomeFragmentNews;
import com.priyo.go.Fragments.NewsTabed.NewsFragment;
import com.priyo.go.Model.api.utility.request.WishAddRequest;
import com.priyo.go.NavigationDrawersNews.NavigationDrawerCallbacks;
import com.priyo.go.NavigationDrawersNews.NavigationDrawerFragment;
import com.priyo.go.PriyoSyncAdapter.StarSyncAdapter;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.view.activity.AboutActivity;
import com.priyo.go.view.activity.BirthdaysActivity;
import com.priyo.go.view.activity.FeedbackActivity;
import com.priyo.go.view.activity.SettingsActivity;
import com.priyo.go.view.activity.business.BusinessActivity;
import com.priyo.go.view.activity.people.PeopleActivity;
import com.priyo.go.view.activity.wish.WishListActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PriyoNewsTabActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks, HttpResponseListener {

    public static NavigationDrawerFragment mNavigationDrawerFragment;
    private static String mobileNumber, name, nodeId;
    ExpandableListView rcv;
    FloatingActionMenu fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    AlertDialog.Builder dialogBuilder;
    AlertDialog mAlertDialog;
    SharedPreferences pref;
    private FragmentManager fragmentManager;
    private Fragment fragment = null;
    private Menu mOptionsMenu;
    private int mBadgeCount = 0;
    private ProgressDialog mProgressDialog;
    private HttpRequestPostAsyncTask mSignUpTask = null;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priyo_news_tabed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressDialog = new ProgressDialog(this);

//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        StarSyncAdapter.initializeSyncAdapter(getApplicationContext());
        StarSyncAdapter.syncImmediately(getApplicationContext());

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.nav_view);
        mNavigationDrawerFragment.setup(R.id.nav_view, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        rcv = (ExpandableListView) findViewById(R.id.drawerList);
        rcv.expandGroup(1);
        rcv.expandGroup(2);

        String name = "", picUrl = "";
        SharedPreferences pref;
        pref = getSharedPreferences(Constants.ApplicationTag, MODE_PRIVATE);
        name = pref.getString(Constants.USER_FULL_NAME_KEY, "");
        picUrl = pref.getString(Constants.USER_PHOTO_URL_KEY, "");
        TextView names = (TextView) findViewById(R.id.name_text_view);
        ProfileImageView pic = (ProfileImageView) findViewById(R.id.profile_picture_image_view);
        names.setText(name);
        pic.setProfilePicture(picUrl, false);

        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();


//        fragmentManager = getSupportFragmentManager();
//        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragment = new NewsFragment();
//        fragmentTransaction.replace(R.id.main_containerr, fragment);
//        fragmentTransaction.commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_containerr, new HomeFragmentNews()).addToBackStack(null).commit();


        //Floating Action Buttons
        fab = (FloatingActionMenu) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_news);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_wish);
        fab3 = (FloatingActionButton) findViewById(R.id.fab_feedback);


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplication(), "Floating Action Button 1", Toast.LENGTH_SHORT).show();
                showChangeLangDialog();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplication(), "Floating Action Button 2", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), NewsPostActivity.class));
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplication(), "Floating Action Button 3", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), FeedbackActivity.class);
                startActivity(i);
            }
        });


//        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (FAB_Status) {
//                    hideFAB();
//                    FAB_Status = false;
//                }
//                return false;
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.priyo_home, menu);
        mOptionsMenu = menu;


        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        int c = dataHelper.getNotOpenedPush();

        if (c > 0) {
            updateNotificationBadgeCount(c);
        }
        return true;
    }

    private void updateNotificationBadgeCount(int badgeCount) {
        mBadgeCount = badgeCount;

        Log.d("Notification Count", badgeCount + "");
        if (mOptionsMenu != null) {
            if (badgeCount > 0) {
                ActionItemBadge.update(this, mOptionsMenu.findItem(R.id.nav_notification), getResources().getDrawable(R.mipmap.notifications), ActionItemBadge.BadgeStyles.DARK_GREY, badgeCount);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_notification) {
            startActivity(new Intent(PriyoNewsTabActivity.this, NotificationActivity.class));
            return true;
        }
        if (id == R.id.saved_news) {
            startActivity(new Intent(PriyoNewsTabActivity.this, SavedNewsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();


        } else {
            this.stopService(new Intent(this, DashboardNewsFragment.MyService.class));

            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            System.gc();
            android.os.Process.killProcess(android.os.Process.myPid());

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(new Intent(this, DashboardNewsFragment.MyService.class));

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.stopService(new Intent(this, DashboardNewsFragment.MyService.class));

    }

//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        if (drawer.isDrawerOpen(GravityCompat.START)) {
////            drawer.closeDrawer(GravityCompat.START);
//
//        if (mNavigationDrawerFragment.isDrawerOpen()){
//            mNavigationDrawerFragment.closeDrawer();
//
//
//        } else {
//            finish();
//
////            Intent startMain = new Intent(Intent.ACTION_MAIN);
////            startMain.addCategory(Intent.CATEGORY_HOME);
////            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            startActivity(startMain);
////            System.gc();
////            android.os.Process.killProcess(android.os.Process.myPid());
//
//        }
//    }


    @Override
    public void onNavigationDrawerItemSelected(int listposition, int childposition, String slugNews, String title) {


        if (childposition == -1) {
            mNavigationDrawerFragment.openDrawer();

//            if (!slugNews.equals("")) {
//                if (mNavigationDrawerFragment.isDrawerOpen())
//                    mNavigationDrawerFragment.closeDrawer();
//                NewsFragment.viewPager.setCurrentItem(listposition, true);
//                //finish();
//            } else {
                if (mNavigationDrawerFragment.isDrawerOpen())
                    mNavigationDrawerFragment.closeDrawer();
//            }
        } else {
            if (listposition == 1) {

                if (childposition == 0) {
                    if (mNavigationDrawerFragment.isDrawerOpen())
                        mNavigationDrawerFragment.closeDrawer();
                    Intent i = new Intent(getApplicationContext(), ContactActivity.class);
                    startActivity(i);
                }
                if (childposition == 1) {
                    if (mNavigationDrawerFragment.isDrawerOpen())
                        mNavigationDrawerFragment.closeDrawer();
                    Intent i = new Intent(getApplicationContext(), PeopleActivity.class);
                    startActivity(i);

                }
                if (childposition == 2) {
                    if (mNavigationDrawerFragment.isDrawerOpen())
                        mNavigationDrawerFragment.closeDrawer();

                    Intent i = new Intent(getApplicationContext(), BusinessActivity.class);
                    startActivity(i);

                }

            } else if (listposition == 2) {
                if (childposition == 0) {
                    if (mNavigationDrawerFragment.isDrawerOpen())
                        mNavigationDrawerFragment.closeDrawer();

                    Intent i = new Intent(getApplicationContext(), BirthdaysActivity.class);
                    startActivity(i);

                }
                if (childposition == 1) {
                    if (mNavigationDrawerFragment.isDrawerOpen())
                        mNavigationDrawerFragment.closeDrawer();

                    Intent i = new Intent(getApplicationContext(), HoroscopeActivity.class);
                    startActivity(i);

                }

                if (childposition == 2) {
                    if (mNavigationDrawerFragment.isDrawerOpen())
                        mNavigationDrawerFragment.closeDrawer();

                    Intent i = new Intent(getApplicationContext(), WishListActivity.class);
                    startActivity(i);

                }
                if (childposition == 3) {
                    if (mNavigationDrawerFragment.isDrawerOpen())
                        mNavigationDrawerFragment.closeDrawer();

                    Intent i = new Intent(getApplicationContext(), FeedbackActivity.class);
                    startActivity(i);

                }
                if (childposition == 4) {
                    if (mNavigationDrawerFragment.isDrawerOpen())
                        mNavigationDrawerFragment.closeDrawer();

                    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(i);

                }
                if (childposition == 5) {
                    if (mNavigationDrawerFragment.isDrawerOpen())
                        mNavigationDrawerFragment.closeDrawer();

                    Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                    startActivity(i);

                }

            } else {
                if (mNavigationDrawerFragment.isDrawerOpen())
                    mNavigationDrawerFragment.closeDrawer();
                Intent intent = new Intent(PriyoNewsTabActivity.this, PriyoCategoryNewsActivity.class);
                intent.putExtra("Slug", slugNews);
                intent.putExtra("Type", "Category");
                intent.putExtra("Title", title);
                startActivity(intent);
            }
        }
    }


    public void showChangeLangDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setPositiveButton("MAKE WISH", null);
        dialogBuilder.setNegativeButton("CANCEL", null);
        dialogBuilder.setTitle("Make a Wish");

        mAlertDialog = dialogBuilder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (edt.getText().toString().length() > 3) {
                            pref = getApplicationContext().getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
                            nodeId = pref.getString(Constants.USER_NODE_ID_KEY, "12345");
                            mobileNumber = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
                            name = pref.getString(Constants.USER_FULL_NAME_KEY, "");
                            accessToken = pref.getString(Constants.ACCESS_TOKEN_KEY, "");
                            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
                            Date now = new Date();
                            String strDate = sdfDate.format(now);

                            mProgressDialog.setMessage("Sending your wish..");
                            mProgressDialog.show();
                            WishAddRequest wishAddRequest = new WishAddRequest();
                            wishAddRequest.setMobileNumber(mobileNumber);
                            wishAddRequest.setName(name);
                            wishAddRequest.setWish(edt.getText().toString());
                            wishAddRequest.setCreatedAt(new Date(System.currentTimeMillis()));
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
                            String json = gson.toJson(wishAddRequest);
                            mSignUpTask = new HttpRequestPostAsyncTask(ApiConstants.Command.USER_WISH_ADD_REQUEST,
                                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.UTILITY_MODULE + ApiConstants.EndPoint.USER_WISH_ADD_ENDPOINT, json, getApplicationContext());
                            mSignUpTask.mHttpResponseListener = PriyoNewsTabActivity.this;
                            mSignUpTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
                            mSignUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid text...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        mAlertDialog.show();


//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
//
//        dialogBuilder.setTitle("Make a Wish");
//        //dialogBuilder.setMessage("Enter text below");
//        dialogBuilder.setPositiveButton("MAKE WISH", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                if(edt.getText().toString().length()>7){
//                    pref = getApplicationContext().getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
//                    nodeId = pref.getString(Constants.USER_NODE_ID_KEY, "");
//                    mobileNumber = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
//                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
//                    Date now = new Date();
//                    String strDate = sdfDate.format(now);
//
//                    mProgressDialog.setMessage("Sending your wish..");
//                    mProgressDialog.show();
//                    WishRequestPersonal mSignupModel = new WishRequestPersonal(mobileNumber,nodeId,edt.getText().toString(),strDate);
//                    Gson gson = new Gson();
//                    String json = gson.toJson(mSignupModel);
//                    mSignUpTask = new HttpRequestPostAsyncTask(Constants.COMMAND_SIGN_UP,
//                            Constants.BASE_URL + "wish/add", json, getApplicationContext());
//                    mSignUpTask.mHttpResponseListener = DashboardActivity.this;
//                    mSignUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                }
//            }
//        });
//        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//            }
//        });
//        AlertDialog b = dialogBuilder.create();
//        b.show();
    }


    @Override
    public void httpResponseReceiver(HttpResponseObject result) {

        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            mProgressDialog.dismiss();
            mSignUpTask = null;
            if (getApplicationContext() != null)
                Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }


        Gson gson = new Gson();

        switch (result.getApiCommand()) {


            case ApiConstants.Command.USER_WISH_ADD_REQUEST:
                try {
                    String message = result.getJsonString();
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {

                        mAlertDialog.dismiss();
                    } else {
                        if (getApplicationContext() != null)
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (getApplicationContext() != null)
                        Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
                }

                mProgressDialog.dismiss();
                mSignUpTask = null;

                break;
        }
    }
}
