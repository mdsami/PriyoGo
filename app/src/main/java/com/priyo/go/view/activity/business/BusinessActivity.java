package com.priyo.go.view.activity.business;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.priyo.go.Fragments.OnFragmentInteractionListener;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Utilities;
import com.priyo.go.view.fragment.business.BusinessCategoryFragment;
import com.priyo.go.view.fragment.business.BusinessListFragment;
import com.priyo.go.view.fragment.business.BusinessSubCategoryFragment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sajid.shahriar on 4/30/17.
 */

public class BusinessActivity extends AppCompatActivity implements OnFragmentInteractionListener, SearchView.OnQueryTextListener {

    public static final int ID_FRAGMENT_BUSINESS_CATEGORY = 0;
    public static final int ID_FRAGMENT_BUSINESS_SUB_CATEGORY = 1;
    public static final int ID_FRAGMENT_BUSINESS_LIST = 2;
    private static final String TAG = BusinessActivity.class.getSimpleName();
    List<String> breadCrumbTitleList;
    private TextView breadCrumbTextView;
    private SearchView businessSearchView;
    private boolean isBusinessListFragmentShown = false;
    private boolean isSearchDone = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(@Nullable Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        breadCrumbTextView = (TextView) findViewById(R.id.bread_crumb_text_view);
        breadCrumbTitleList = Arrays.asList("Business", "Category");
        addToBackStack(ID_FRAGMENT_BUSINESS_CATEGORY, new Bundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu(Menu menu)");
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search_menu_item);
        businessSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        businessSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        Utilities.hideKeyboard(this);
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            if (!isSearchDone) {
                breadCrumbTitleList.remove(breadCrumbTitleList.size() - 1);
                if (breadCrumbTitleList.size() == 1) {
                    breadCrumbTitleList = new LinkedList<>(Arrays.asList("Business", "Category"));
                }
            } else {
                isSearchDone = false;
            }
            updateBreadCrumb();
            getSupportFragmentManager().popBackStack();
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                isBusinessListFragmentShown = false;
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected(MenuItem item)");
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void replaceWith(int changeToFragmentId) {

    }

    @Override
    public void replaceWith(int changeToFragmentId, Bundle bundle) {

    }

    @Override
    public void addToBackStack(int addFragmentId) {
        switch (addFragmentId) {
            case ID_FRAGMENT_BUSINESS_CATEGORY:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new BusinessCategoryFragment()).addToBackStack(null).commit();
                break;
            case ID_FRAGMENT_BUSINESS_SUB_CATEGORY:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new BusinessSubCategoryFragment()).addToBackStack(null).commit();
                break;
            case ID_FRAGMENT_BUSINESS_LIST:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new BusinessListFragment()).addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void addToBackStack(int addFragmentId, Bundle bundle) {
        switch (addFragmentId) {
            case ID_FRAGMENT_BUSINESS_CATEGORY:
                isBusinessListFragmentShown = false;
                isSearchDone = false;
                breadCrumbTitleList = new LinkedList<>(Arrays.asList("Business", "Category"));
                updateBreadCrumb();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, BusinessCategoryFragment.newInstance(bundle)).addToBackStack(null).commit();
                break;
            case ID_FRAGMENT_BUSINESS_SUB_CATEGORY:
                isBusinessListFragmentShown = false;
                isSearchDone = false;
                breadCrumbTitleList = new LinkedList<>(Arrays.asList("Business", bundle.getString("title")));
                updateBreadCrumb();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, BusinessSubCategoryFragment.newInstance(bundle)).addToBackStack(null).commit();
                break;
            case ID_FRAGMENT_BUSINESS_LIST:
                if (isBusinessListFragmentShown) {
                    getSupportFragmentManager().popBackStack();
                }
                isBusinessListFragmentShown = true;
                final String fragmentActionType = bundle.getString("fragmentActionType");
                if (fragmentActionType != null && fragmentActionType.equals(ApiConstants.Parameter.QUERY_STRING_PARAMETER)) {
                    breadCrumbTextView.setText(String.format("Business/ %s", "Search"));
                } else {
                    breadCrumbTitleList = new LinkedList<>(Arrays.asList("Business", bundle.getString("title"), bundle.getString("subTitle")));
                    updateBreadCrumb();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, BusinessListFragment.newInstance(bundle)).addToBackStack(null).commit();
                break;
        }
    }

    private void updateBreadCrumb() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < breadCrumbTitleList.size(); i++) {
            if (!TextUtils.isEmpty(breadCrumbTitleList.get(i))) {
                stringBuilder.append(breadCrumbTitleList.get(i)).append(i == breadCrumbTitleList.size() - 1 ? "" : "/ ");
            }
        }
        breadCrumbTextView.setText(stringBuilder.toString());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit(String query)");
        isSearchDone = true;
        businessSearchView.clearFocus();
        Utilities.hideKeyboard(this, businessSearchView);
        Bundle bundle = new Bundle();
        bundle.putString("fragmentActionType", ApiConstants.Parameter.QUERY_STRING_PARAMETER);
        bundle.putString(ApiConstants.Parameter.QUERY_STRING_PARAMETER, query);
        bundle.putString("title", "Search");
        addToBackStack(ID_FRAGMENT_BUSINESS_LIST, bundle);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
