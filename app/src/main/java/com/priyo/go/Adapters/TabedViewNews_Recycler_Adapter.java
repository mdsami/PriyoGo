package com.priyo.go.Adapters;

/**
 * Created by MUKUL on 11/8/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.priyo.go.PriyoContentProvider.NewsContract;
import com.priyo.go.R;
import com.priyo.go.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import static com.priyo.go.Utilities.Constants.options;


public class TabedViewNews_Recycler_Adapter extends
        RecyclerView.Adapter<NewsListHolder> {

    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;
    String[] idsArr;
    int posss = -1;
    private ArrayList<PriyoNews> arrayList;
    private Gson gson;
    private NewsImageResponse mNewsImageResponse;
    private Context context;

    public TabedViewNews_Recycler_Adapter(Context context,
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
    public void onBindViewHolder(final NewsListHolder holder, int position) {
        final PriyoNews model = arrayList.get(position);
        mNewsImageResponse = gson.fromJson(model.getImage(), NewsImageResponse.class);

        holder.titleView.setText(model.getTitle());
        holder.summaryView.setText(model.getSummary());
        String imgUrl = "", chacheHost="";
        try{
            chacheHost = mNewsImageResponse.getCacheHost();
        }catch (Exception e){e.printStackTrace();}

//        if (chacheHost.trim().isEmpty()|| chacheHost==null || chacheHost.equals("null") || chacheHost.equals(""))
//            imgUrl = mNewsImageResponse.getHostName() + "" + mNewsImageResponse.getOriginal();
//        else
//            imgUrl = mNewsImageResponse.getCacheHost() + "" + mNewsImageResponse.getOriginal();

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


        boolean isFav = false;
        String ids = model.getId();

        Cursor cursor = context.getContentResolver().query(
                NewsContract.FavEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String id = cursor.getString(cursor.getColumnIndex(NewsContract.FavEntry.COLUMN_NEWS_ID));

                if (ids.equals(id)) {
                    posss = position;
                    break;
                }
            }
        }
        if (position == posss) {
            holder.saveNews.setImageResource(R.mipmap.remove_fron_save);
        } else {
            holder.saveNews.setImageResource(R.mipmap.save_for_later_1);
        }

        System.out.println("WWWWWWWWFFF 2  " + model.getTitle() + " " + model.getCreatedAt() + " " + model.getUpdatedAt() + " " + model.getPublishedAt());

        String updateDate = Utilities.changeDateFormatGMTtoBST(model.getPublishedAt());
        updateDate = Utilities.convertIntoBanglaDate(updateDate);

        holder.list_date.setText("  " + updateDate);


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

                    case R.id.share_news:
                        shareNews(position);
                        break;

                    case R.id.save_news:

                        boolean isFav = false;
                        String ids = model.getId();
                        Cursor cursor = context.getContentResolver().query(
                                NewsContract.FavEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                null
                        );
                        if (cursor.getCount() > 0) {
                            System.out.println("WWWWWWWWFFF 3  " + cursor.getCount());


                            for (int i = 0; i < cursor.getCount(); i++) {
                                cursor.moveToPosition(i);
                                String id = cursor.getString(cursor.getColumnIndex(NewsContract.FavEntry.COLUMN_NEWS_ID));

                                if (ids.equals(id)) {
                                    context.getContentResolver().delete(NewsContract.FavEntry.CONTENT_URI,
                                            NewsContract.FavEntry.COLUMN_NEWS_ID + " = ?",
                                            new String[]{model.getId()});
                                    holder.saveNews.setImageResource(R.mipmap.save_for_later_1);
                                    isFav = true;
                                    //notifyDataSetChanged();
                                    break;
                                } else {
                                    isFav = false;
                                }
                            }
                        }
                        if (!isFav) {

                            System.out.println("WWWWWWWWFFF 4  " + cursor.getCount());

                            saveNews(model);
                            holder.saveNews.setImageResource(R.mipmap.remove_fron_save);
                        }
                        break;
                }
            }

        });


        holder.saveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFav = false;
                String ids = model.getId();
                Cursor cursor = context.getContentResolver().query(
                        NewsContract.FavEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
                if (cursor.getCount() > 0) {
                    System.out.println("WWWWWWWWFFF 3  " + cursor.getCount());


                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToPosition(i);
                        String id = cursor.getString(cursor.getColumnIndex(NewsContract.FavEntry.COLUMN_NEWS_ID));

                        if (ids.equals(id)) {
                            context.getContentResolver().delete(NewsContract.FavEntry.CONTENT_URI,
                                    NewsContract.FavEntry.COLUMN_NEWS_ID + " = ?",
                                    new String[]{model.getId()});
                            holder.saveNews.setImageResource(R.mipmap.save_for_later_1);
                            isFav = true;
                            //notifyDataSetChanged();
                            break;
                        } else {
                            isFav = false;
                        }
                    }
                }
                if (!isFav) {

                    System.out.println("WWWWWWWWFFF 4  " + cursor.getCount());

                    saveNews(model);
                    holder.saveNews.setImageResource(R.mipmap.remove_fron_save);
                }

            }
        });


    }

    public void saveNews(PriyoNews model) {

        Vector<ContentValues> cVVector = new Vector<ContentValues>(1);
        ContentValues newsValues = new ContentValues();

        Date date = new Date();

        newsValues.put(NewsContract.FavEntry.COLUMN_NEWS_ID, model.getId());
        newsValues.put(NewsContract.FavEntry.COLUMN_TITLE, model.getTitle());
        newsValues.put(NewsContract.FavEntry.COLUMN_DESCRIPTION, model.getDescription());
        newsValues.put(NewsContract.FavEntry.COLUMN_SUMMERY, model.getSummary());
        newsValues.put(NewsContract.FavEntry.COLUMN_SLUG, model.getSlug());
        newsValues.put(NewsContract.FavEntry.COLUMN_UPDATE_AT, model.getUpdatedAt());
        newsValues.put(NewsContract.FavEntry.COLUMN_CREATE_AT, model.getCreatedAt());
        newsValues.put(NewsContract.FavEntry.COLUMN_PRIORITY, model.getCreatedAt());
        newsValues.put(NewsContract.FavEntry.COLUMN_PUBLISHED_AT, model.getPublishedAt());
        newsValues.put(NewsContract.FavEntry.COLUMN_CATEGORY, model.getCategories());
        newsValues.put(NewsContract.FavEntry.COLUMN_AUTHOR, model.getAuthor());
        newsValues.put(NewsContract.FavEntry.COLUMN_TAGS, model.getTags());
        newsValues.put(NewsContract.FavEntry.COLUMN_BUSINESS, model.getBusinesses());
        newsValues.put(NewsContract.FavEntry.COLUMN_PERSONS, model.getPeople());
        newsValues.put(NewsContract.FavEntry.COLUMN_IMG, model.getImage());
        newsValues.put(NewsContract.FavEntry.COLUMN_INSERT_DATE_TIME, String.valueOf(date.getTime()));

        cVVector.add(newsValues);


        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);


            Cursor cursor = context.getContentResolver().query(
                    NewsContract.FavEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );


            System.out.println("SUCKSSSS FAV        " + cursor.getCount());
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cVVector.size(); i++) {
                    String ids = cVVector.get(i).getAsString(NewsContract.FavEntry.COLUMN_NEWS_ID);
                    System.out.println("SUCKSSSS FAV        " + ids);
                    context.getContentResolver().delete(NewsContract.FavEntry.CONTENT_URI,
                            NewsContract.FavEntry.COLUMN_NEWS_ID + " =?",
                            new String[]{ids});

                }
            }

            context.getContentResolver().bulkInsert(NewsContract.FavEntry.CONTENT_URI,
                    cvArray);

            notifyDataSetChanged();
        }
    }

    public void goToDetails(int position) {
        NewsDetailsActivity.contentsList = arrayList;
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra("POSITION", position);
        context.startActivity(intent);
    }

    public void shareNews(int position) {
        String link = "http://www.priyo.com/articles/" + arrayList.get(position).getSlug();

        System.out.println("RTRT  " + link);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, link);
        context.startActivity(Intent.createChooser(shareIntent, "Share link using"));
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
    public NewsListHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = null;

        if (viewType == ITEM_TYPE_HEADER) {
            mainGroup = (ViewGroup) mInflater.inflate(
                    R.layout.item_layout_tabed_priyo_news, viewGroup, false);
        } else if (viewType == ITEM_TYPE_NORMAL) {
            mainGroup = (ViewGroup) mInflater.inflate(
                    R.layout.item_priyo_tab_news, viewGroup, false);
        }

//
//        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
//                R.layout.item_layout_priyo_news, viewGroup, false);
        NewsListHolder listHolder = new NewsListHolder(mainGroup);

        Cursor cursor = context.getContentResolver().query(
                NewsContract.FavEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        System.out.println("SUCKSSSS FAVss>        " + cursor.getCount());
        if (cursor.getCount() > 0) {
            idsArr = new String[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);

                String ids = cursor.getString(cursor.getColumnIndex(NewsContract.FavEntry.COLUMN_NEWS_ID));

                idsArr[i] = ids;
            }
        }


        return listHolder;

    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

}
