package com.priyo.go.Adapters;

/**
 * Created by MUKUL on 11/8/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.priyo.go.Activities.News.NewsDetailsActivity;
import com.priyo.go.Model.PriyoNews.NewsImageResponse;
import com.priyo.go.Model.PriyoNews.PriyoNews;
import com.priyo.go.R;
import com.priyo.go.Utilities.Utilities;

import java.util.ArrayList;

import static com.priyo.go.Utilities.Constants.options;


public class HomeNews_Recycler_Adapter extends
        RecyclerView.Adapter<NewsListHolder_> {

    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;
    String[] idsArr;
    int posss = -1;
    private ArrayList<PriyoNews> arrayList;
    private Gson gson;
    private NewsImageResponse mNewsImageResponse;
    private Context context;

    public HomeNews_Recycler_Adapter(Context context,
                                     ArrayList<PriyoNews> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        gson = new Gson();
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(final NewsListHolder_ holder, int position) {
        final PriyoNews model = arrayList.get(position);
        mNewsImageResponse = gson.fromJson(model.getImage(), NewsImageResponse.class);

        holder.titleView.setText(model.getTitle());
        String imgUrl = "", chacheHost="";
        try{
            chacheHost = mNewsImageResponse.getCacheHost();
        }catch (Exception e){e.printStackTrace();}

//        if (chacheHost.trim().isEmpty()|| chacheHost==null || chacheHost.equals("null") || chacheHost.equals(""))
//            imgUrl = mNewsImageResponse.getHostName() + "" + mNewsImageResponse.getOriginal();
//        else
//            imgUrl = mNewsImageResponse.getCacheHost() + "" + mNewsImageResponse.getMedium();



        if (chacheHost.trim().isEmpty()|| chacheHost==null || chacheHost.equals("null") || chacheHost.equals(""))
            imgUrl = "https://res.cloudinary.com/priyo/f_auto,q_auto,c_thumb,g_auto:faces,w_350,h_200/img" + "" + mNewsImageResponse.getOriginal();
        else
            imgUrl = "https://res.cloudinary.com/priyo/f_auto,q_auto,c_thumb,g_auto:faces,w_350,h_200/img" + "" + mNewsImageResponse.getOriginal();



//        Glide.with(this.context).load(imgUrl).placeholder(R.mipmap.no_image).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.list_imageView);
        ImageLoader.getInstance().displayImage(imgUrl, holder.list_imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });

        holder.list_date.setText(" " + Utilities.convertIntoBanglaDate(Utilities.TimeDifferenceWithCurrentDate(model.getPublishedAt())) + " আগে");


        // Implement click listener over layout
        holder.setClickListener(new RecyclerView_OnClickListener.OnClickListener() {

            @Override
            public void OnItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.thumbnail:
                        goToDetails(position);
                        break;
                    case R.id.news_title:
                        goToDetails(position);
                        break;
                    case R.id.news_summary:
                        goToDetails(position);
                        break;

                    case R.id.update_date:
                        goToDetails(position);
                        break;


                }
            }

        });



    }

    public void goToDetails(int position) {
        NewsDetailsActivity.contentsList = arrayList;
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra("POSITION", position);
        context.startActivity(intent);
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }


    @Override
    public NewsListHolder_ onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = null;

        if (viewType == ITEM_TYPE_HEADER) {
            mainGroup = (ViewGroup) mInflater.inflate(
                    R.layout.item_home_news_first, viewGroup, false);
        } else if (viewType == ITEM_TYPE_NORMAL) {
            mainGroup = (ViewGroup) mInflater.inflate(
                    R.layout.item_home_new_tav_, viewGroup, false);
        }

        NewsListHolder_ listHolder = new NewsListHolder_(mainGroup);


        return listHolder;

    }

}
