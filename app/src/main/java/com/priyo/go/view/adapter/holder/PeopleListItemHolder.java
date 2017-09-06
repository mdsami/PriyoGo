package com.priyo.go.view.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.R;
import com.priyo.go.view.widget.RecyclerView;

/**
 * Created by sajid.shahriar on 5/4/17.
 */

public class PeopleListItemHolder extends RecyclerView.ViewHolder {
    public TextView peopleNameTextView;
    public ProfileImageView profilePictureImageView;
    public TextView peopleTagTextView;

    public PeopleListItemHolder(View itemView) {
        super(itemView);
        peopleNameTextView = (TextView) itemView.findViewById(R.id.people_name_text_view);
        profilePictureImageView = (ProfileImageView) itemView.findViewById(R.id.people_profile_picture_image_view);
        peopleTagTextView = (TextView) itemView.findViewById(R.id.people_tag_text_view);
    }
}
