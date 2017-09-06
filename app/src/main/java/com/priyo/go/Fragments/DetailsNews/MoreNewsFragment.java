package com.priyo.go.Fragments.DetailsNews;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.priyo.go.Activities.News.NotificationDetailsActivity;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.PriyoNews.NewsImageResponse;
import com.priyo.go.Model.PriyoNews.PriyoNews;
import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.JSONPerser;
import com.priyo.go.Utilities.Utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class MoreNewsFragment extends Fragment implements HttpResponseListener{

    private static ProgressDialog mProgressDialog;
    private static HttpRequestGetAsyncTask mPriyoNewsTask = null;

//    private ImageView mThumbnailView1,mThumbnailView2,mThumbnailView3;
//    private TextView mNewsTitleView1,mNewsTitleView2,mNewsTitleView3;
    private static RelativeLayout mProgressView;
    private static Context con;
    private static LinearLayout mRootView,root;
    private static TextView mMoreButtonView;

    String key;

    private static Gson gson;
    private static NewsImageResponse mNewsImageResponse;
    private static NewsImageResponse mNewsImageResponse2;


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = getContext();
        gson= new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        key = getArguments().getString("key");
        System.out.println("FTFT  "+key);
        View v = inflater.inflate(R.layout.fragment_details_more_news, container, false);

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
            for (int i=0;i<newsData.size();i=i+2){

                LayoutInflater inflater = (LayoutInflater)con.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout testL = (LinearLayout) inflater.inflate(R.layout.item_more_news,null);
                final PriyoNews model = newsData.get(i);
                CardView  root = (CardView) testL.findViewById(R.id.card_view);
                TextView titleTest = (TextView) testL.findViewById(R.id.news_title);
                TextView timeText = (TextView) testL.findViewById(R.id.update_date);
                ImageView mThumbnailView1 =(ImageView) testL.findViewById(R.id.thumbnail);
                titleTest.setText(model.getTitle());
                timeText.setText(" "+Utilities.convertIntoBanglaDate(Utilities.TimeDifferenceWithCurrentDate(model.getPublishedAt()))+" আগে");
                titleTest.setMaxLines(3);
                titleTest.setEllipsize(TextUtils.TruncateAt.END);
                mNewsImageResponse = gson.fromJson(model.getImage(), NewsImageResponse.class);
                String imgUrl = "";
                if(!mNewsImageResponse.getCacheHost().equals(""))
                    imgUrl = mNewsImageResponse.getCacheHost()+""+mNewsImageResponse.getMedium();
                else
                    imgUrl = mNewsImageResponse.getHostName()+""+mNewsImageResponse.getOriginal();
                Glide.with(con).load(imgUrl).placeholder(R.mipmap.no_image).crossFade().into(mThumbnailView1);


                CardView root1 = (CardView) testL.findViewById(R.id.card_view1);
                TextView titleTest1 = (TextView) testL.findViewById(R.id.news_title1);
                TextView timeText1 = (TextView) testL.findViewById(R.id.update_date1);
                ImageView mThumbnailView11 = (ImageView) testL.findViewById(R.id.thumbnail1);

                if(newsData.size()>=i+2) {


                    final PriyoNews model1 = newsData.get(i + 1);


                    titleTest1.setText(model1.getTitle());
                    timeText1.setText(" " + Utilities.convertIntoBanglaDate(Utilities.TimeDifferenceWithCurrentDate(model1.getPublishedAt())) + " আগে");
                    titleTest1.setMaxLines(3);
                    titleTest1.setEllipsize(TextUtils.TruncateAt.END);
                    mNewsImageResponse2 = gson.fromJson(model1.getImage(), NewsImageResponse.class);

                    String imgUrl1 = "";
                    if (!mNewsImageResponse2.getCacheHost().equals(""))
                        imgUrl1 = mNewsImageResponse2.getCacheHost() + "" + mNewsImageResponse2.getMedium();
                    else
                        imgUrl1 = mNewsImageResponse2.getHostName() + "" + mNewsImageResponse2.getOriginal();

                    Glide.with(con).load(imgUrl1).placeholder(R.mipmap.no_image).crossFade().into(mThumbnailView11);


                    root1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(con, NotificationDetailsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("Slug", model1.getSlug());
                            con.startActivity(intent);
                        }
                    });
                }
                else
                {
                    root1.setVisibility(View.INVISIBLE);
                }

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

//    private static void updateView(final ArrayList<PriyoNews> newsData){
//
//
//        try{
//            mRootView.removeAllViews();
//            for (int i=0;i<newsData.size();i++){
//                PriyoNews model = newsData.get(i);
//                LayoutInflater inflater = (LayoutInflater)con.getSystemService
//                        (Context.LAYOUT_INFLATER_SERVICE);
//                LinearLayout testL = (LinearLayout) inflater.inflate(R.layout.item_home_priyo_news,null);
//
//                root = (LinearLayout) testL.findViewById(R.id.root);
//                TextView titleTest = (TextView) testL.findViewById(R.id.news_title);
//                TextView timeText = (TextView) testL.findViewById(R.id.update_date);
//                ImageView mThumbnailView1 =(ImageView) testL.findViewById(R.id.thumbnail);
//                titleTest.setText(model.getTitle());
//                timeText.setText(" "+Utilities.convertIntoBanglaDate(Utilities.TimeDifferenceWithCurrentDate(model.getPublishedAt()))+" আগে");
////                if(i==0){
//                    titleTest.setMaxLines(3);
//                    titleTest.setEllipsize(TextUtils.TruncateAt.END);
//                    NewsImageResponse img = new NewsImageResponse();
//                    img = new JSONPerser().parseImage(model.getImage());
//                    String imgUrl = "";
//                    if(!img.getCacheHost().equals(""))
//                        imgUrl = img.getCacheHost()+""+img.getMedium();
//                    else
//                        imgUrl = img.getHostName()+""+img.getOriginal();
//                    ImageLoader.getInstance().displayImage(imgUrl, mThumbnailView1, options, new ImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String imageUri, View view) {
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        }
//
//                        @Override
//                        public void onLoadingCancelled(String imageUri, View view) {
//                        }
//                    });
//
////                }else{
////                    titleTest.setMaxLines(2);
////                    titleTest.setEllipsize(TextUtils.TruncateAt.END);
////                    mThumbnailView1.setVisibility(View.GONE);
////                }
//
//                final int j =i;
//
//                root.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        NewsDeatailsActivity.contentsList= newsData;
//                        Intent intent= new Intent(con,NewsDeatailsActivity.class);
//                        intent.putExtra("POSITION", j);
//                        con.startActivity(intent);
//                    }
//                });
//
//                mThumbnailView1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        NewsDeatailsActivity.contentsList= newsData;
//                        Intent intent= new Intent(con,NewsDeatailsActivity.class);
//                        intent.putExtra("POSITION", j);
//                        con.startActivity(intent);
//                    }
//                });
//
//                mRootView.addView(testL);
//
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();}
//    }

    public void startFetching(){

        String query="";
        try {
            query = URLEncoder.encode(key.trim(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mProgressView.setVisibility(View.VISIBLE);
        mPriyoNewsTask = new HttpRequestGetAsyncTask("PRIYO_NEWS","https://api.priyo.com/api/mobile/related-articles-by-categories?categories="+query +"&limit=6", con);
        mPriyoNewsTask.mHttpResponseListener = this;
        mPriyoNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}

