package com.priyo.go.Fragments.NewsTabed;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.priyo.go.Adapters.TabedViewNews_Recycler_Adapter;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.CustomView.CustomSwipeRefreshLayout;
import com.priyo.go.Model.PriyoNews.PriyoNews;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.JSONPerser;
import com.priyo.go.Utilities.Utilities;

import java.util.ArrayList;

public class TabFragmentNews extends Fragment implements HttpResponseListener {

    int page=0;

//    private ProgressDialog mProgressDialog;
    private HttpRequestGetAsyncTask mPriyoNewsTask = null;
    private RecyclerView mRecyclerView;
    private TabedViewNews_Recycler_Adapter mAdapter;
    private RelativeLayout mBottomLayout,progessLayout;
    private GridLayoutManager mLayoutManager;
    private boolean mUserScrolled = true;
    private int mPastVisiblesItems, mVisibleItemCount, mTotalItemCount;
    private String mSlug ="sports",mType="Category";
    private String mCatTitle="Sports";
    int position;
    private CustomSwipeRefreshLayout mSwipeRefreshLayout;

   // private static ArrayList<PriyoNews> priyoCatNewsList = new ArrayList<>();
    ArrayList<PriyoNews>[] lists = (ArrayList<PriyoNews>[])new ArrayList[13];


    public TabFragmentNews() {
        for(int i=0;i<13;i++)
        {
            lists[i] = new ArrayList<PriyoNews>();           // Problem here
        }
    }

    public static TabFragmentNews newInstance(int page, String slug) {
        Bundle args = new Bundle();
        args.putInt("position", page);
        args.putString("slug", slug);
        TabFragmentNews fragment = new TabFragmentNews();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        this.mSlug = getArguments().getString("slug");
        this.position = getArguments().getInt("position");

        mSwipeRefreshLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new CustomSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isConnectionAvailable(getContext())) {
                    refreshTransactionHistory();
                }
            }
        });

        init(view);
        implementScrollListener();
        updateData();

        return view;

    }

    private void refreshTransactionHistory() {
        getTransactionHistory();
    }

    private void getTransactionHistory() {
        if(position==1) {
            mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS_NEW", "https://api.priyo.com/api/mobile/top-story-articles?limit=10&skip=0", getContext());
        }else{
            mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS_NEW", Constants.PRIYO_BASE_URL + Constants.URL_FILTER_CATEGORY + "="
                    + mSlug + "&" + Constants.URL_FILTER_LIMIT + "=10&" + Constants.URL_FILTER_SKIP + "=" + 0, getContext());
        }
        mPriyoNewsTask.mHttpResponseListener = this;
        mPriyoNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



    private void init(View view) {
        mBottomLayout = (RelativeLayout) view.findViewById(R.id.loadItemsLayout_recyclerView);
        final int noOfCard = getResources().getInteger(R.integer.no_of_card);
        mLayoutManager = new GridLayoutManager(getContext(),noOfCard);

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? noOfCard : 1;
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mProgressDialog = new ProgressDialog(getContext());
//        mProgressDialog.setMessage(getString(R.string.progress_dialog_text_fetching_newsr));

        progessLayout =(RelativeLayout) view.findViewById(R.id.progress_layout);
    }

    private void updateData() {


            fetchNews();

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
//        mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS_UPDATE",Constants.PRIYO_API+Constants.URL_FILTER_CATEGORY+"="
//                    +mSlug+"&"+Constants.URL_FILTER_LIMIT+"=10&"+Constants.URL_FILTER_SKIP+"="+priyoCatNewsList.size(), getContext());


        if(position==1) {
            mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS_UPDATE", "https://api.priyo.com/api/mobile/top-story-articles?limit=10&skip=0"+lists[position].size(), getContext());
        }else{
            mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS_UPDATE",Constants.PRIYO_BASE_URL+Constants.URL_FILTER_CATEGORY+"="
                    +mSlug+"&"+Constants.URL_FILTER_LIMIT+"=10&"+Constants.URL_FILTER_SKIP+"="+lists[position].size(), getContext());
        }
        mPriyoNewsTask.mHttpResponseListener = this;
        mPriyoNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void fetchNews() {
        //mProgressDialog.show();

        System.out.println("Position "+position);
        progessLayout.setVisibility(View.VISIBLE);

        if(position==1) {
            mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS", "https://api.priyo.com/api/mobile/top-story-articles?limit=10&skip=0", getContext());
        }else{
            mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS", Constants.PRIYO_BASE_URL + Constants.URL_FILTER_CATEGORY + "="
                    + mSlug + "&" + Constants.URL_FILTER_LIMIT + "=10&" + Constants.URL_FILTER_SKIP + "=" + 0, getContext());
        }
//        mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS",Constants.PRIYO_API+Constants.URL_FILTER_CATEGORY+"="
//                    +mSlug+"&"+Constants.URL_FILTER_LIMIT+"=10&"+Constants.URL_FILTER_SKIP+"="+0, getContext());
        mPriyoNewsTask.mHttpResponseListener = this;
        mPriyoNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {

        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            //mProgressDialog.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);
            mPriyoNewsTask = null;
            if (getContext() != null)
                Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        switch (result.getApiCommand()) {

            case "PRIYO_NEWS_NEW":
                try {
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        ArrayList<PriyoNews> temp = new ArrayList<>();
//                        prepareNews(result.getJsonString());
//                        updateData();
//                        createPage();

                        if(position==1) {
                            temp = JSONPerser.prepareTopNews(result.getJsonString());
                        }else{
                            temp = JSONPerser.prepareNews(result.getJsonString());
                        }


                        mAdapter = new TabedViewNews_Recycler_Adapter(getContext(), temp);
                        mRecyclerView.setAdapter(mAdapter);// set adapter on recyclerview
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (this != null){}
                        Toast.makeText(getContext(), result.getJsonString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (this != null)
                        Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                }

                mSwipeRefreshLayout.setRefreshing(false);
                mPriyoNewsTask = null;

                break;

            case "PRIYO_NEWS":
                try {
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        //priyoCatNewsList=  JSONPerser.prepareNews(result.getJsonString());

                        if(position==1) {
                            lists[position]= JSONPerser.prepareTopNews(result.getJsonString());
                        }else{
                            lists[position]=  JSONPerser.prepareNews(result.getJsonString());
                        }
                        //lists[position]=  JSONPerser.prepareNews(result.getJsonString());
                        mAdapter = new TabedViewNews_Recycler_Adapter(getContext(),lists[position]);
                        mRecyclerView.setAdapter(mAdapter);// set adapter on recyclerview
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (this != null){}
                        Toast.makeText(getContext(), result.getJsonString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (this != null)
                        Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                }
                progessLayout.setVisibility(View.GONE);
                //mProgressDialog.dismiss();
                mPriyoNewsTask = null;

                break;

            case "PRIYO_NEWS_UPDATE":
                try {
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        ArrayList<PriyoNews> temp = new ArrayList<>();
                        if(position==1) {
                            temp = JSONPerser.prepareTopNews(result.getJsonString());
                        }else{
                            temp = JSONPerser.prepareNews(result.getJsonString());
                        }
//                        priyoCatNewsList.addAll(temp);
                        lists[position].addAll(temp);
                        mAdapter.notifyDataSetChanged();// notify adapter
                        mBottomLayout.setVisibility(View.GONE);
                    } else {
                        if (getContext() != null){}
                        Toast.makeText(getContext(), result.getJsonString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (getContext() != null)
                        Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                }
                mPriyoNewsTask = null;

                break;


        }
    }



}