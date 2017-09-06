package com.priyo.go.Activities.News;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.priyo.go.Adapters.TabedViewNews_Recycler_Adapter;
import com.priyo.go.CustomView.CustomSwipeRefreshLayout;
import com.priyo.go.DatabaseHelper.DataHelper;
import com.priyo.go.Model.Friend.PushNode;
import com.priyo.go.NavigationDrawersNews.NavigationDrawerFragment;
import com.priyo.go.PriyoContentProvider.NewsContract;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.JSONPerser;

import java.util.ArrayList;
import java.util.List;

public class SavedNewsActivity extends AppCompatActivity {



    private CustomSwipeRefreshLayout mSwipeRefreshLayout;
    private static RecyclerView mRecyclerView;
    private static TabedViewNews_Recycler_Adapter adapter;
    private static LinearLayoutManager mLayoutManager;

    public static NavigationDrawerFragment mNavigationDrawerFragment;
    ListView rcv;

    private Menu mOptionsMenu;

    private int mBadgeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        init();
        updateData();
        createPage();
    }


    private void init() {
        mSwipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private void updateData() {
        Cursor cursor = this.getContentResolver().query(
                NewsContract.FavEntry.CONTENT_URI,
                null,
                null,
                null,
                NewsContract.NewsEntry.COLUMN_PUBLISHED_AT+" DESC"
        );
        if (cursor.getCount() > 0){
            Constants.favNewsList = new ArrayList<>();
            Constants.favNewsList = JSONPerser.prepareNewsFromCursor(cursor);
        }

    }

    private void createPage() {
        adapter = new TabedViewNews_Recycler_Adapter(this, Constants.favNewsList);
        mRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.priyo_home, menu);
        mOptionsMenu = menu;


        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        List<PushNode> listData = dataHelper.getPushList();

        if(listData.size()>0){

//            MenuItem item = menu.findItem(R.id.nav_notification);
//            LayerDrawable icon = (LayerDrawable) item.getIcon();
//
//            // Update LayerDrawable's BadgeDrawable
//            Utils2.setBadgeCount(this, icon, listData.size());

            updateNotificationBadgeCount(listData.size());
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
            startActivity(new Intent(SavedNewsActivity.this,NotificationActivity.class));
            return true;
        }
        if (id == R.id.saved_news) {
            startActivity(new Intent(SavedNewsActivity.this,SavedNewsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
