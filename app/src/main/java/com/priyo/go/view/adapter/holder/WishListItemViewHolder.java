package com.priyo.go.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.R;

/**
 * Created by sajid.shahriar on 5/1/17.
 */

public class WishListItemViewHolder extends RecyclerView.ViewHolder {
    public TextView userNameTextView;
    public ProfileImageView userProfileImageView;
    public TextView wishDateTextView;
    public TextView wishDetailsTextView;
    public TextView wishFullTextView;
    public ImageView wishImageView;

    public WishListItemViewHolder(View itemView) {
        super(itemView);
        userNameTextView = (TextView) itemView.findViewById(R.id.user_name_text_view);
        userProfileImageView = (ProfileImageView) itemView.findViewById(R.id.user_profile_image_view);
        wishDateTextView = (TextView) itemView.findViewById(R.id.wish_date_text_view);
        wishDetailsTextView = (TextView) itemView.findViewById(R.id.wish_details_text_view);
        wishFullTextView = (TextView) itemView.findViewById(R.id.wish_full_text_view);
        wishImageView = (ImageView) itemView.findViewById(R.id.wish_image_view);
    }
}
