package com.priyo.go.Activities.News;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.priyo.go.Adapters.NotificationListAdapter;
import com.priyo.go.DatabaseHelper.DataHelper;
import com.priyo.go.Model.Friend.PushNode;
import com.priyo.go.R;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mEmptyNotificationTextView;
    private NotificationListAdapter mAdapter;
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        List<PushNode> listData = dataHelper.getPushList();
        mEmptyNotificationTextView = (TextView) findViewById(R.id.contact_list_empty);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.contact_list);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(listData.size()==0){
            mEmptyNotificationTextView.setText("No Notification Found!");
        }else{
            mAdapter = new NotificationListAdapter(this, listData);
            mRecyclerView.setAdapter(mAdapter);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.priyo_home, menu);
        mOptionsMenu = menu;
        DataHelper dataHelper = DataHelper.getInstance(getApplicationContext());
        int c= dataHelper.getNotOpenedPush();
        if(c>0){
            updateNotificationBadgeCount(c);
        }
        return true;
    }

    private void updateNotificationBadgeCount(int badgeCount) {
        if (mOptionsMenu != null) {
            if (badgeCount > 0) {
                ActionItemBadge.update(this, mOptionsMenu.findItem(R.id.nav_notification), getResources().getDrawable(R.mipmap.notifications), ActionItemBadge.BadgeStyles.DARK_GREY, badgeCount);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.saved_news) {
            startActivity(new Intent(NotificationActivity.this,SavedNewsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
