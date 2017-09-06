package com.priyo.go.NavigationDrawersNews;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.PriyoContentProvider.NewsContract;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.JSONPerser;
import com.priyo.go.view.activity.profile.ProfileActivity;

/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationDrawerFragment extends Fragment implements NavigationDrawerCallbacks {
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREFERENCES_FILE = "my_app_settings"; //TODO: change this to your file
    public static String mobileNumber, name, prof_pic;
    SharedPreferences pref;
    private NavigationDrawerCallbacks mCallbacks;
    //private RecyclerView mDrawerList;
    private ExpandableListView mDrawerList;
    private LinearLayout mProfileView;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;
    private ProfileImageView profilePic;
    private TextView nameView, phone;

    //public static ArrayList<Cat> catList;// = new ArrayList<>();

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Cursor cursor = getActivity().getContentResolver().query(
                NewsContract.CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor.getCount() > 0){
            Constants.catList = JSONPerser.prepareCatFromCursor(cursor);
        }
        pref = getActivity().getSharedPreferences(Constants.ApplicationTag, getActivity().MODE_PRIVATE);
        mobileNumber = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        name = pref.getString(Constants.USER_FULL_NAME_KEY, "");
        prof_pic = pref.getString(Constants.USER_PHOTO_URL_KEY, "");

        View view = inflater.inflate(R.layout.navigation_drawer_expendable, container, false);
        mDrawerList = (ExpandableListView) view.findViewById(R.id.drawerList);

        mProfileView = (LinearLayout) view.findViewById(R.id.main_linearlayout_title);
        profilePic = (ProfileImageView) view.findViewById(R.id.profile_picture_image_view);
        nameView = (TextView) view.findViewById(R.id.name_text_view);
        phone = (TextView) view.findViewById(R.id.mobile_number);

        nameView.setText(name);
        phone.setText(mobileNumber);
        profilePic.setProfilePicture(prof_pic,true);


        mProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                closeDrawer();
            }
        });

        NavigationListAdapter adapter = new NavigationListAdapter(getActivity());
        adapter.setNavigationDrawerCallbacks(this);
        mDrawerList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pref = getActivity().getSharedPreferences(Constants.ApplicationTag, getActivity().MODE_PRIVATE);
        mobileNumber = pref.getString(Constants.USER_MOBILE_NUMBER_KEY, "");
        name = pref.getString(Constants.USER_FULL_NAME_KEY, "");
        prof_pic = pref.getString(Constants.USER_PHOTO_URL_KEY, "");
        nameView.setText(name);
        phone.setText(mobileNumber);
        profilePic.setProfilePicture(prof_pic,true);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }

    public void setActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        mActionBarDrawerToggle = actionBarDrawerToggle;
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "true");
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
            mDrawerLayout.openDrawer(mFragmentContainerView);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

//    public List<NavigationItem> getMenu() {
//        List<NavigationItem> items = new ArrayList<NavigationItem>();
//        items.add(new NavigationItem("item 1", getResources().getDrawable(R.drawable.left_menu_icon)));
//        items.add(new NavigationItem("item 2", getResources().getDrawable(R.drawable.left_menu_icon)));
//        items.add(new NavigationItem("item 3", getResources().getDrawable(R.drawable.left_menu_icon)));
//        return items;
//    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void selectItem(int position, int childpos,String slug, String title) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position,childpos,slug,title);
        }
        //((NavigationDrawerAdapter) mDrawerList.getAdapter()).selectPosition(position);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

//    @Override
//    public void onNavigationDrawerItemSelected(int position) {
//
//        selectItem(position);
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }

    @Override
    public void onNavigationDrawerItemSelected(int listposition, int childposition, String slug, String title) {
        selectItem(listposition, childposition,slug,  title);
    }
}
