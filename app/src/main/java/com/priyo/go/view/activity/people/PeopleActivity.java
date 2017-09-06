package com.priyo.go.view.activity.people;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.priyo.go.Fragments.OnFragmentInteractionListener;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Utilities;
import com.priyo.go.view.fragment.people.PeopleListFragment;
import com.priyo.go.view.fragment.people.PeopleProfessionListFragment;

public class PeopleActivity extends AppCompatActivity implements OnFragmentInteractionListener, SearchView.OnQueryTextListener {

    public static final int ID_PEOPLE_PROFESSION_LIST_FRAGMENT = 0;
    public static final int ID_PEOPLE_LIST_FRAGMENT = 1;
    private static final String TAG = PeopleActivity.class.getSimpleName();
    private boolean isPeopleListFragmentShown = false;

    private TextView breadCrumbTextView;
    private SearchView peopleSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        breadCrumbTextView = (TextView) findViewById(R.id.bread_crumb_text_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Bundle bundle = getIntent().getBundleExtra("searchBundle");
        final int fragmentId = ((bundle == null) ? ID_PEOPLE_PROFESSION_LIST_FRAGMENT : ID_PEOPLE_LIST_FRAGMENT);
        addToBackStack(fragmentId, bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu(Menu menu)");
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search_menu_item);
        peopleSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        peopleSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        Utilities.hideKeyboard(this);
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            breadCrumbTextView.setText(String.format("People/ %s", "Profession"));
            getSupportFragmentManager().popBackStack();
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                isPeopleListFragmentShown = false;
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
        Log.d(TAG, "replaceWith(int changeToFragmentId)");
    }

    @Override
    public void replaceWith(int changeToFragmentId, Bundle bundle) {
        Log.d(TAG, "replaceWith(int changeToFragmentId, Bundle bundle)");
        switch (changeToFragmentId) {
            case ID_PEOPLE_PROFESSION_LIST_FRAGMENT:
                isPeopleListFragmentShown = false;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, PeopleProfessionListFragment.newInstance(bundle)).commit();
                break;
            case ID_PEOPLE_LIST_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, PeopleListFragment.newInstance(bundle)).commit();
                isPeopleListFragmentShown = true;
                break;

        }
    }

    @Override
    public void addToBackStack(int addFragmentId) {
        Log.d(TAG, "addToBackStack(int addFragmentId)");

    }

    @Override
    public void addToBackStack(int addFragmentId, Bundle bundle) {
        Log.d(TAG, "addToBackStack(int addFragmentId, Bundle bundle)");
        switch (addFragmentId) {
            case ID_PEOPLE_PROFESSION_LIST_FRAGMENT:
                breadCrumbTextView.setText(String.format("People/ %s", "Profession"));
                isPeopleListFragmentShown = false;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, PeopleProfessionListFragment.newInstance(bundle)).addToBackStack(null).commit();
                break;
            case ID_PEOPLE_LIST_FRAGMENT:
                if (isPeopleListFragmentShown) {
                    getSupportFragmentManager().popBackStack();
                }
                final String key = bundle.getString("fragmentActionType");
                if (key != null) {
                    breadCrumbTextView.setText(String.format("People/ %s",
                            bundle.getString("title")));
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, PeopleListFragment.newInstance(bundle)).addToBackStack(null).commit();
                    isPeopleListFragmentShown = true;
                } else {
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit(String query)");
        peopleSearchView.clearFocus();
        Utilities.hideKeyboard(this, peopleSearchView);
        Bundle bundle = new Bundle();
        bundle.putString("fragmentActionType", ApiConstants.Parameter.QUERY_STRING_PARAMETER);
        bundle.putString(ApiConstants.Parameter.QUERY_STRING_PARAMETER, query);
        bundle.putString("title", "Search");
        addToBackStack(ID_PEOPLE_LIST_FRAGMENT, bundle);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
