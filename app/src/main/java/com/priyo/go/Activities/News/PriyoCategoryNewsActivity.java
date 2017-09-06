package com.priyo.go.Activities.News;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.priyo.go.Adapters.TabedViewNews_Recycler_Adapter;
import com.priyo.go.Api.HttpResponseObject;

import java.util.ArrayList;

import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.DatabaseHelper.DataHelper;
import com.priyo.go.Model.PriyoNews.NewsCategoryResponse;
import com.priyo.go.Model.PriyoNews.PriyoNews;

import com.priyo.go.PriyoContentProvider.NewsContract;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.JSONPerser;

public class PriyoCategoryNewsActivity extends AppCompatActivity implements  HttpResponseListener {
    int page=0;

    private ProgressDialog mProgressDialog;
    private HttpRequestGetAsyncTask mPriyoNewsTask = null;
    private RecyclerView mRecyclerView;
    private TabedViewNews_Recycler_Adapter mAdapter;
    private RelativeLayout mBottomLayout;
    private GridLayoutManager mLayoutManager;
    private boolean mUserScrolled = true;
    private int mPastVisiblesItems, mVisibleItemCount, mTotalItemCount;
    private String mSlug ="",mType;
    private String mCatTitle;

    private Menu mOptionsMenu;

    private int mBadgeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priyo_category_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        mSlug=intent.getStringExtra("Slug");
        mType=intent.getStringExtra("Type");
        mCatTitle=intent.getStringExtra("Title");
        getSupportActionBar().setTitle(mCatTitle);

        init();

        implementScrollListener();

        updateData();
    }


    private void init() {
        mBottomLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_recyclerView);
        final int noOfCard = getResources().getInteger(R.integer.no_of_card);
        mLayoutManager = new GridLayoutManager(this,noOfCard);

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? noOfCard : 1;
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.progress_dialog_text_fetching_news));
    }

    private void updateData() {

        Constants.priyoCatNewsList = new ArrayList<>();
        Cursor cursor = this.getContentResolver().query(
                NewsContract.NewsEntry.CONTENT_URI,
                null,
                null,
                null,
                NewsContract.NewsEntry.COLUMN_PUBLISHED_AT+" DESC, "+NewsContract.NewsEntry.COLUMN_PRIORITY+" DESC"
        );
        if (cursor.getCount() > 0){
            ArrayList<PriyoNews> temp= new ArrayList<>();
            temp = JSONPerser.prepareNewsFromCursor(cursor);
            for(int i=0; i<temp.size();i++)
            {
                PriyoNews newTemp = new PriyoNews();
                newTemp = temp.get(i);
                ArrayList<NewsCategoryResponse> tempCat = new ArrayList<>();
                tempCat= JSONPerser.parseNewsCategory(newTemp.getCategories());
                for(int j=0;j<tempCat.size();j++) {
                    if (tempCat.get(j).getSlug().equals(mSlug)) {
                        Constants.priyoCatNewsList.add(newTemp);
                        break;
                    }
                }
            }
            if(Constants.priyoCatNewsList.size()>2)
            {
                mAdapter = new TabedViewNews_Recycler_Adapter(this, Constants.priyoCatNewsList);
                mRecyclerView.setAdapter(mAdapter);// set adapter on recyclerview
                mAdapter.notifyDataSetChanged();
            }
            else{
                fetchNews();
            }
        }
        else {
           fetchNews();
        }

    }

    // Implement scroll listener
    private void implementScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mUserScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mVisibleItemCount = mLayoutManager.getChildCount();
                mTotalItemCount = mLayoutManager.getItemCount();
                mPastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (mUserScrolled
                        && (mVisibleItemCount + mPastVisiblesItems) == mTotalItemCount) {
                    mUserScrolled = false;
                    updateRecyclerView();
                }
            }
        });

    }

    // Method for repopulating recycler view
    private void updateRecyclerView() {
        mBottomLayout.setVisibility(View.VISIBLE);
        page++;
        if(mType.equals("Category")) {
            if (mSlug.equals("top")) {
                mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS_UPDATE", "https://api.priyo.com/api/mobile/top-story-articles?limit=10&skip=" + Constants.priyoCatNewsList.size(), getApplicationContext());
            } else if (mSlug.equals("latest")) {
                mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS_UPDATE", "https://api.priyo.com/api/mobile/latest-articles?limit=10&skip=" + Constants.priyoCatNewsList.size(), getApplicationContext());
            } else {
                mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS_UPDATE", Constants.PRIYO_BASE_URL + Constants.URL_FILTER_CATEGORY + "="
                        + mSlug + "&" + Constants.URL_FILTER_LIMIT + "=10&" + Constants.URL_FILTER_SKIP + "=" + Constants.priyoCatNewsList.size(), getApplicationContext());
            }
        }
        else if(mType.equals("People"))
            mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS_UPDATE",Constants.PRIYO_BASE_URL+Constants.URL_FILTER_PEOPLE+"="+mSlug+"&"+Constants.URL_FILTER_LIMIT+"=10&"+Constants.URL_FILTER_SKIP+"="+Constants.priyoCatNewsList.size(), this);
        else
            mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS_UPDATE",Constants.PRIYO_BASE_URL+Constants.URL_FILTER_TAG+"="+mSlug+"&"+Constants.URL_FILTER_LIMIT+"=10&"+Constants.URL_FILTER_SKIP+"="+Constants.priyoCatNewsList.size(), this);
        mPriyoNewsTask.mHttpResponseListener = this;
        mPriyoNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void fetchNews() {
        mProgressDialog.show();
        if(mType.equals("Category")){
            if (mSlug.equals("top")) {
                mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS", "https://api.priyo.com/api/mobile/top-story-articles?limit=10&skip=0", getApplicationContext());
            } else if (mSlug.equals("latest")) {
                mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS", "https://api.priyo.com/api/mobile/latest-articles?limit=10&skip=0", getApplicationContext());
            } else {
                mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS",Constants.PRIYO_BASE_URL+Constants.URL_FILTER_CATEGORY+"="
                        +mSlug+"&"+Constants.URL_FILTER_LIMIT+"=10&"+Constants.URL_FILTER_SKIP+"="+0, this);
            }
        }
        else if(mType.equals("People"))
            mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS",Constants.PRIYO_BASE_URL+Constants.URL_FILTER_PEOPLE+"="+mSlug+"&"+Constants.URL_FILTER_LIMIT+"=10&"+Constants.URL_FILTER_SKIP+"="+0, this);
        else
            mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS",Constants.PRIYO_BASE_URL+Constants.URL_FILTER_TAG+"="+mSlug+"&"+Constants.URL_FILTER_LIMIT+"=10&"+Constants.URL_FILTER_SKIP+"="+0, this);
        mPriyoNewsTask.mHttpResponseListener = this;
        mPriyoNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {

        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            mProgressDialog.dismiss();
            mPriyoNewsTask = null;
            if (getApplicationContext() != null)
                Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        switch (result.getApiCommand()) {

            case "PRIYO_NEWS":
                try {
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        if (mSlug.equals("top")) {
                            Constants.priyoCatNewsList=  JSONPerser.prepareTopNews(result.getJsonString());
                        } else if (mSlug.equals("latest")) {
                            Constants.priyoCatNewsList=  JSONPerser.prepareLatestNews(result.getJsonString());
                        } else {
                            Constants.priyoCatNewsList=  JSONPerser.prepareNews(result.getJsonString());
                        }

                        mAdapter = new TabedViewNews_Recycler_Adapter(this, Constants.priyoCatNewsList);
                        mRecyclerView.setAdapter(mAdapter);// set adapter on recyclerview
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (this != null){}
                        Toast.makeText(this, result.getJsonString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (this != null)
                        Toast.makeText(this, R.string.service_not_available, Toast.LENGTH_LONG).show();
                }
                mProgressDialog.dismiss();
                mPriyoNewsTask = null;

                break;

            case "PRIYO_NEWS_UPDATE":
                try {
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        ArrayList<PriyoNews> temp = new ArrayList<>();

                        if (mSlug.equals("top")) {
                            temp=  JSONPerser.prepareTopNews(result.getJsonString());
                        } else if (mSlug.equals("latest")) {
                            temp=  JSONPerser.prepareLatestNews(result.getJsonString());
                        } else {
                            temp=  JSONPerser.prepareNews(result.getJsonString());
                        }
                        //temp = JSONPerser.prepareNews(result.getJsonString());
                        Constants.priyoCatNewsList.addAll(temp);
                        mAdapter.notifyDataSetChanged();// notify adapter
                        mBottomLayout.setVisibility(View.GONE);
                    } else {
                        if (getApplicationContext() != null){}
                            Toast.makeText(getApplicationContext(), result.getJsonString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (getApplicationContext() != null)
                        Toast.makeText(getApplicationContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                }
                mPriyoNewsTask = null;

                break;


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
            startActivity(new Intent(PriyoCategoryNewsActivity.this,NotificationActivity.class));
            return true;
        }
        if (id == R.id.saved_news) {
            startActivity(new Intent(PriyoCategoryNewsActivity.this,SavedNewsActivity.class));
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
