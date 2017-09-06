package com.priyo.go.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.priyo.go.R;

/**
 * Created by MUKUL on 11/8/16.
 */

public class NewsListHolder extends RecyclerView.ViewHolder implements
        OnClickListener {

    public TextView titleView, summaryView, list_date;
    public ImageView list_imageView, shareNews, saveNews;
    public CardView cardView;

    private RecyclerView_OnClickListener.OnClickListener onClickListener;

    public NewsListHolder(View view) {
        super(view);

        // Find all views ids
        this.titleView = (TextView) view.findViewById(R.id.news_title);
        this.summaryView = (TextView) view.findViewById(R.id.news_summary);
        this.list_date = (TextView) view.findViewById(R.id.update_date);
        this.list_imageView = (ImageView) view.findViewById(R.id.thumbnail);
        this.shareNews = (ImageView) view.findViewById(R.id.share_news);
        this.saveNews = (ImageView) view.findViewById(R.id.save_news);
        this.cardView = (CardView) view.findViewById(R.id.card_view);

        this.cardView.setOnClickListener(this);
        this.titleView.setOnClickListener(this);
        this.summaryView.setOnClickListener(this);
        this.list_date.setOnClickListener(this);
        this.list_imageView.setOnClickListener(this);
        this.shareNews.setOnClickListener(this);
        this.saveNews.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.OnItemClick(v, getAdapterPosition());
        }
    }

    public void setClickListener(
            RecyclerView_OnClickListener.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
