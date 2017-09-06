package com.priyo.go.Activities.News;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.priyo.go.DatabaseHelper.DataHelper;
import com.priyo.go.Fragments.DetailsNews.NewsDetailsFragment;
import com.priyo.go.Model.PriyoNews.PriyoNews;
import com.priyo.go.R;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsDetailsActivity extends AppCompatActivity implements Serializable {
    public static ArrayList<PriyoNews> contentsList;
    private Bundle bundle = null;
    private String title="Test";
    private int count=0;
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_deatails_copy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            count=bundle.getInt("POSITION", 0);
        }

        NewsDetailsFragment cc = NewsDetailsFragment.newInstance(count,contentsList,title);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.card_frame,cc)
                .commit();
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

        Log.d("Notification Count", badgeCount + "");
        if (mOptionsMenu != null) {
            if (badgeCount > 0) {
                ActionItemBadge.update(this, mOptionsMenu.findItem(R.id.nav_notification), getResources().getDrawable(R.mipmap.notifications), ActionItemBadge.BadgeStyles.DARK_GREY, badgeCount);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_notification) {
            startActivity(new Intent(NewsDetailsActivity.this, NotificationActivity.class));
            return true;
        }
        if (id == R.id.saved_news) {
            startActivity(new Intent(NewsDetailsActivity.this, SavedNewsActivity.class));
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}