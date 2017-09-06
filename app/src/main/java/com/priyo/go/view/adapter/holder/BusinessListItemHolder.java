package com.priyo.go.view.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.R;
import com.priyo.go.view.widget.RecyclerView;

/**
 * Created by sajid.shahriar on 5/4/17.
 */

public class BusinessListItemHolder extends RecyclerView.ViewHolder {
    public TextView businessNameTextView;
    public ProfileImageView businessProfileImageView;
    public TextView businessTagTextView;

    public BusinessListItemHolder(View itemView) {
        super(itemView);
        businessNameTextView = (TextView) itemView.findViewById(R.id.business_name_text_view);
        businessProfileImageView = (ProfileImageView) itemView.findViewById(R.id.business_profile_picture_image_view);
        businessTagTextView = (TextView) itemView.findViewById(R.id.business_tag_text_view);
    }
}
