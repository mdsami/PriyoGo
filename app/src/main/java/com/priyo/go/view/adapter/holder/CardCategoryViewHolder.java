package com.priyo.go.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.priyo.go.R;

/**
 * Created by sajid.shahriar on 4/27/17.
 */

public class CardCategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView categoryNameTextView;

    public CardCategoryViewHolder(View itemView) {
        super(itemView);
        categoryNameTextView = (TextView) itemView.findViewById(R.id.category_name_text_view);
    }
}