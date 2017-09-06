package com.priyo.go.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.R;

/**
 * Created by sajid.shahriar on 4/27/17.
 */

public class DashboardItemViewHolder extends RecyclerView.ViewHolder {


    public ProfileImageView itemProfileImageView;
    public TextView itemTitleTextView;

    public DashboardItemViewHolder(View itemView) {
        super(itemView);
        itemProfileImageView = (ProfileImageView) itemView.findViewById(R.id.business_profile_image_view);
        itemTitleTextView = (TextView) itemView.findViewById(R.id.business_name_text_view);
    }
}