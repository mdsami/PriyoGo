package com.priyo.go.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.R;

/**
 * Created by sajid.shahriar on 5/1/17.
 */

public class BirthdayListItemViewHolder extends RecyclerView.ViewHolder {
    public TextView monthNameTextView;
    public ProfileImageView profilePictureImageView;
    public TextView profileNameTextView;
    public TextView birthdayDateTextView;
    public ImageButton birthdayAlarmButton;

    public BirthdayListItemViewHolder(View itemView) {
        super(itemView);
        monthNameTextView = (TextView) itemView.findViewById(R.id.month_name_text_view);
        profilePictureImageView = (ProfileImageView) itemView.findViewById(R.id.profile_picture_image_view);
        profileNameTextView = (TextView) itemView.findViewById(R.id.profile_name_text_view);
        birthdayDateTextView = (TextView) itemView.findViewById(R.id.birthday_date_text_view);
        birthdayAlarmButton = (ImageButton) itemView.findViewById(R.id.birthday_alarm_button);
    }
}
