package com.priyo.go.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.R;

/**
 * Created by sajid.shahriar on 5/1/17.
 */

public class BirthdayDashboardItemViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView;
    public TextView birthdayDateTextView;
    public ProfileImageView profileImageView;

    public BirthdayDashboardItemViewHolder(View itemView) {
        super(itemView);
        nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
        birthdayDateTextView = (TextView) itemView.findViewById(R.id.birthday_date_text_view);
        profileImageView = (ProfileImageView) itemView.findViewById(R.id.profile_image_view);
    }
}
