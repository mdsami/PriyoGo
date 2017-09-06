package com.priyo.go.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.R;

/**
 * Created by sajid.shahriar on 5/11/17.
 */

public class DashboardBusinessItemViewHolder extends RecyclerView.ViewHolder {


    public ProfileImageView businessProfileImageView;
    public TextView businessNameTextView;

    public DashboardBusinessItemViewHolder(View itemView) {
        super(itemView);
        businessProfileImageView = (ProfileImageView) itemView.findViewById(R.id.business_profile_image_view);
        businessNameTextView = (TextView) itemView.findViewById(R.id.business_name_text_view);
        businessProfileImageView.setPlaceHolder(R.mipmap.buld);
    }
}