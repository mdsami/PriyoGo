package com.priyo.go.Fragments.DetailsNews;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.priyo.go.Activities.News.NotificationDetailsActivity;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.PriyoNews.PriyoNews;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.JSONPerser;
import com.priyo.go.Utilities.Utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class HistoryFragment extends Fragment implements HttpResponseListener{

    private static ProgressDialog mProgressDialog;
    private static HttpRequestGetAsyncTask mPriyoNewsTask = null;

//    private ImageView mThumbnailView1,mThumbnailView2,mThumbnailView3;
//    private TextView mNewsTitleView1,mNewsTitleView2,mNewsTitleView3;
    private static RelativeLayout mProgressView;
    private static Context con;
    private static LinearLayout mRootView,root;
    private static TextView mMoreButtonView;

    String key;


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

        key = getArguments().getString("key");
        System.out.println("FTFT  "+key);
        View v = inflater.inflate(R.layout.fragment_details_history, container, false);

        initViews(v);
        startFetching();



        mMoreButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getContext(), SplashActivity.class);
//                startActivity(i);
            }
        });

        return v;
    }


    private void initViews(View v){
        mMoreButtonView =(TextView) v.findViewById(R.id.more_button);

        mProgressView =(RelativeLayout) v.findViewById(R.id.progress_rl);
        mRootView =(LinearLayout) v.findViewById(R.id.root_news);

    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {

        if (result == null || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_INTERNAL_ERROR
                || result.getStatus() == Constants.HTTP_RESPONSE_STATUS_NOT_FOUND) {
            //mProgressDialog.dismiss();
            mPriyoNewsTask = null;
            if (getContext() != null)
                Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        switch (result.getApiCommand()) {

            case "PRIYO_NEWS":
                try {
                    if (result.getStatus() == Constants.HTTP_RESPONSE_STATUS_OK) {
                        Constants.dashboardNewsList=  JSONPerser.prepareRelatedNews(result.getJsonString());
                        updateView(Constants.dashboardNewsList);
                        mProgressView.setVisibility(View.GONE);
                    } else {
                        //if (this != null){}
                        //Toast.makeText(getContext(), result.getJsonString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (this != null)
                        Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                }
                //mProgressDialog.dismiss();
                mPriyoNewsTask = null;

                break;


        }

    }


    private static void updateView(List<PriyoNews> newsData){


        try{
            mRootView.removeAllViews();
            for (int i=0;i<newsData.size();i=i+1){

                LayoutInflater inflater = (LayoutInflater)con.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout testL = (LinearLayout) inflater.inflate(R.layout.item_history,null);
                final PriyoNews model = newsData.get(i);
                LinearLayout  root = (LinearLayout) testL.findViewById(R.id.root);
                TextView titleTest = (TextView) testL.findViewById(R.id.news_title);
                TextView timeText = (TextView) testL.findViewById(R.id.date);
                titleTest.setText(model.getTitle());
                timeText.setText(Utilities.convertIntoBanglaDate(Utilities.changeDateFormat12(model.getPublishedAt())));




                final int j =i;

                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(con, NotificationDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Slug", model.getSlug());
                        con.startActivity(intent);
                    }
                });


                mRootView.addView(testL);

            }

        }catch (Exception e){
            e.printStackTrace();}
    }

    public void startFetching(){

        String query="";
        try {
            query = URLEncoder.encode(key.trim(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mProgressView.setVisibility(View.VISIBLE);
        mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS","https://api.priyo.com/api/mobile/related-articles-by-topics?topics="+query, con);
        mPriyoNewsTask.mHttpResponseListener = this;
        mPriyoNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}

