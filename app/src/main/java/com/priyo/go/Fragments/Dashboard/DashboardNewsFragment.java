package com.priyo.go.Fragments.Dashboard;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.priyo.go.Activities.News.NewsDetailsActivity;
import com.priyo.go.Adapters.HomeNews_Recycler_Adapter;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.PriyoNews.NewsImageResponse;
import com.priyo.go.Model.PriyoNews.PriyoNews;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.JSONPerser;
import com.priyo.go.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardNewsFragment extends Fragment {
    private static HttpRequestGetAsyncTask mPriyoNewsTask = null;
    private static RelativeLayout mProgressView;
    private static Context con;
    private static LinearLayout mRootView,root;
    private static TextView mMoreButtonView;
    private static Gson gson;
    private static NewsImageResponse mNewsImageResponse;

    private static RecyclerView mRecyclerView;
    private static HomeNews_Recycler_Adapter mAdapter;
    private static RelativeLayout mBottomLayout;
    private static GridLayoutManager mLayoutManager;

    private static void updateView(final ArrayList<PriyoNews> newsData){
        try{

            final int noOfCard = con.getResources().getInteger(R.integer.no_of_card);

            if(noOfCard==1) {
                mRootView.removeAllViews();
                for (int i=0;i<newsData.size();i++) {
                    PriyoNews model = newsData.get(i);
                    mNewsImageResponse = gson.fromJson(newsData.get(i).getImage(), NewsImageResponse.class);
                    LayoutInflater inflater = (LayoutInflater) con.getSystemService
                            (Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout testL = (LinearLayout) inflater.inflate(R.layout.item_home_priyo_news, null);

                    root = (LinearLayout) testL.findViewById(R.id.root);
                    TextView titleTest = (TextView) testL.findViewById(R.id.news_title);
                    TextView timeText = (TextView) testL.findViewById(R.id.update_date);
                    ImageView mThumbnailView1 = (ImageView) testL.findViewById(R.id.thumbnail);
                    titleTest.setText(model.getTitle());
                    timeText.setText(" " + Utilities.convertIntoBanglaDate(Utilities.TimeDifferenceWithCurrentDate(model.getPublishedAt())) + " আগে");
                    if (i == 0) {
                        titleTest.setMaxLines(3);
                        titleTest.setEllipsize(TextUtils.TruncateAt.END);

                        String imgUrl = "";
                        if (!mNewsImageResponse.getCacheHost().equals(""))
                            imgUrl = mNewsImageResponse.getCacheHost() + "" + mNewsImageResponse.getMedium();
                        else
                            imgUrl = mNewsImageResponse.getHostName() + "" + mNewsImageResponse.getOriginal();

                        Glide.with(con).load(imgUrl).centerCrop().placeholder(R.mipmap.no_image).crossFade().into(mThumbnailView1);
                    } else {
                        titleTest.setMaxLines(2);
                        titleTest.setEllipsize(TextUtils.TruncateAt.END);
                        mThumbnailView1.setVisibility(View.GONE);
                    }

                    final int j = i;

                    root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewsDetailsActivity.contentsList = newsData;
                            Intent intent = new Intent(con, NewsDetailsActivity.class);
                            intent.putExtra("POSITION", j);
                            con.startActivity(intent);
                        }
                    });

                    mThumbnailView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewsDetailsActivity.contentsList = newsData;
                            Intent intent = new Intent(con, NewsDetailsActivity.class);
                            intent.putExtra("POSITION", j);
                            con.startActivity(intent);
                        }
                    });

                    mRootView.addView(testL);
                }
            }
            else{

                mAdapter = new HomeNews_Recycler_Adapter(con, newsData);
                mRecyclerView.setAdapter(mAdapter);// set adapter on recyclerview
                mAdapter.notifyDataSetChanged();

//                PriyoNews model = newsData.get(0);
//                mNewsImageResponse = gson.fromJson(newsData.get(0).getImage(), NewsImageResponse.class);
//                LayoutInflater inflater = (LayoutInflater) con.getSystemService
//                        (Context.LAYOUT_INFLATER_SERVICE);
//                CardView testL = (CardView) inflater.inflate(R.layout.item_home_news_first, null);
//
//                TextView titleTest = (TextView) testL.findViewById(R.id.news_title);
//                TextView timeText = (TextView) testL.findViewById(R.id.update_date);
//                ImageView mThumbnailView1 = (ImageView) testL.findViewById(R.id.thumbnail);
//                titleTest.setText(model.getTitle());
//                timeText.setText(" " + Utilities.convertIntoBanglaDate(Utilities.TimeDifferenceWithCurrentDate(model.getPublishedAt())) + " আগে");
//                titleTest.setMaxLines(4);
//                titleTest.setEllipsize(TextUtils.TruncateAt.END);
//                String imgUrl = "";
//                if (!mNewsImageResponse.getCacheHost().equals(""))
//                    imgUrl = mNewsImageResponse.getCacheHost() + "" + mNewsImageResponse.getMedium();
//                else
//                    imgUrl = mNewsImageResponse.getHostName() + "" + mNewsImageResponse.getOriginal();
//                Glide.with(con).load(imgUrl).centerCrop().placeholder(R.mipmap.no_image).crossFade().into(mThumbnailView1);
//
//                testL.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        goToDetails(newsData,0);
//                    }
//                });
//
//                titleTest.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        goToDetails(newsData,0);
//                    }
//                });
//
//                timeText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        goToDetails(newsData,0);
//                    }
//                });
//
//                mThumbnailView1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        goToDetails(newsData,0);
//                    }
//                });
//
//                mRootView.addView(testL);
//
//                for(int j=1;j<newsData.size();j=j+2){
//
//                    LayoutInflater inflater1 = (LayoutInflater)con.getSystemService
//                            (Context.LAYOUT_INFLATER_SERVICE);
//                    LinearLayout testL1 = (LinearLayout) inflater1.inflate(R.layout.item_home_new_tav,null);
//                    final PriyoNews model1 = newsData.get(j);
//                    mNewsImageResponse = gson.fromJson(model1.getImage(), NewsImageResponse.class);
//                    CardView  root1 = (CardView) testL1.findViewById(R.id.card_view);
//                    TextView titleTest1 = (TextView) testL1.findViewById(R.id.news_title);
//                    TextView timeText1 = (TextView) testL1.findViewById(R.id.update_date);
//                    ImageView mThumbnailView11 = (ImageView) testL1.findViewById(R.id.thumbnail);
//                    titleTest1.setText(model1.getTitle());
//                    timeText1.setText(" " + Utilities.convertIntoBanglaDate(Utilities.TimeDifferenceWithCurrentDate(model1.getPublishedAt())) + " আগে");
//                    titleTest1.setMaxLines(3);
//                    titleTest1.setEllipsize(TextUtils.TruncateAt.END);
//                    String imgUrl1 = "";
//                    if (!mNewsImageResponse.getCacheHost().equals(""))
//                        imgUrl1 = mNewsImageResponse.getCacheHost() + "" + mNewsImageResponse.getMedium();
//                    else
//                        imgUrl1 = mNewsImageResponse.getHostName() + "" + mNewsImageResponse.getOriginal();
//                    Glide.with(con).load(imgUrl1).placeholder(R.mipmap.no_image).crossFade().into(mThumbnailView11);
//
//                    final int x=j;
//                    root1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            goToDetails(newsData,x);
//                        }
//                    });
//
//                    titleTest1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            goToDetails(newsData,x);
//                        }
//                    });
//
//                    timeText1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            goToDetails(newsData,x);
//                        }
//                    });
//
//                    mThumbnailView11.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            goToDetails(newsData,x);
//                        }
//                    });
//
//                    final PriyoNews model2 = newsData.get(j+1);
//                    mNewsImageResponse = gson.fromJson(model2.getImage(), NewsImageResponse.class);
//                    CardView  root2 = (CardView) testL1.findViewById(R.id.card_view1);
//                    TextView titleTest2 = (TextView) testL1.findViewById(R.id.news_title1);
//                    TextView timeText2 = (TextView) testL1.findViewById(R.id.update_date1);
//                    ImageView mThumbnailView12 = (ImageView) testL1.findViewById(R.id.thumbnail1);
//                    titleTest2.setText(model2.getTitle());
//                    timeText2.setText(" " + Utilities.convertIntoBanglaDate(Utilities.TimeDifferenceWithCurrentDate(model2.getPublishedAt())) + " আগে");
//                    titleTest2.setMaxLines(3);
//                    titleTest2.setEllipsize(TextUtils.TruncateAt.END);
//                    String imgUrl2 = "";
//                    if (!mNewsImageResponse.getCacheHost().equals(""))
//                        imgUrl2 = mNewsImageResponse.getCacheHost() + "" + mNewsImageResponse.getMedium();
//                    else
//                        imgUrl2 = mNewsImageResponse.getHostName() + "" + mNewsImageResponse.getOriginal();
//                    Glide.with(con).load(imgUrl2).placeholder(R.mipmap.no_image).crossFade().into(mThumbnailView12);
//
//                    final int x1=j+1;
//                    root2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            goToDetails(newsData,x1);
//                        }
//                    });
//
//                    titleTest2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            goToDetails(newsData,x1);
//                        }
//                    });
//
//                    timeText2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            goToDetails(newsData,x1);
//                        }
//                    });
//
//                    mThumbnailView12.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            goToDetails(newsData,x1);
//                        }
//                    });
//
//                    mRootView.addView(testL1);
//
//                }
            }



        }catch (Exception e){
            e.printStackTrace();}
    }

    public static void goToDetails(ArrayList<PriyoNews> newsdata, int j){
        NewsDetailsActivity.contentsList = newsdata;
        Intent intent = new Intent(con, NewsDetailsActivity.class);
        intent.putExtra("POSITION", j);
        con.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_news, container, false);
        gson = new Gson();

        initViews(v);
        mProgressView.setVisibility(View.VISIBLE);
        getActivity().startService(new Intent(getActivity(), MyService.class));
        mMoreButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getContext(), SplashActivity.class);
//                startActivity(i);
            }
        });

        return v;
    }

    private void initViews(View v) {
        mMoreButtonView = (TextView) v.findViewById(R.id.more_button);
        mProgressView = (RelativeLayout) v.findViewById(R.id.progress_rl);
        mRootView = (LinearLayout) v.findViewById(R.id.root_news);

        final int noOfCard = con.getResources().getInteger(R.integer.no_of_card);

        if (noOfCard == 2) {
            mLayoutManager = new GridLayoutManager(con, noOfCard);

            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position == 0 ? noOfCard : 1;
                }
            });

            mRecyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
    }

    public static class MyService extends Service implements HttpResponseListener {

        private Timer timer;
        @Override
        public void onCreate() {
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    System.out.println("MMMKKKLLL> News ");
                    mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS","https://api.priyo.com/api/articles/?filter[order][0]=publishedAt%20DESC&filter[limit]=5", con);
                    mPriyoNewsTask.mHttpResponseListener = MyService.this;
                    mPriyoNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }, 0 , 5* 60 * 1000);

            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            timer.cancel();
        }


        @Override
        public void httpResponseReceiver(HttpResponseObject result) {

            if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                    || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
                //mProgressDialog.dismiss();
                mPriyoNewsTask = null;
                if (con != null)
                    Toast.makeText(con, R.string.service_not_available, Toast.LENGTH_SHORT).show();
                return;
            }

            Gson gson = new Gson();
            switch (result.getApiCommand()) {

                case "PRIYO_NEWS":
                    try {
                        if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                            Constants.dashboardNewsList=  JSONPerser.prepareNews(result.getJsonString());
                            updateView(Constants.dashboardNewsList);
                            mProgressView.setVisibility(View.GONE);
                        } else {
                            //if (this != null){}
                            //Toast.makeText(getContext(), result.getJsonString(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (this != null)
                            Toast.makeText(con, R.string.service_not_available, Toast.LENGTH_LONG).show();
                    }
                    mPriyoNewsTask = null;

                    break;


            }

        }
    }
}

