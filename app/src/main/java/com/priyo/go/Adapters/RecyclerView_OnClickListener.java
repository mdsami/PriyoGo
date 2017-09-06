package com.priyo.go.Adapters;

/**
 * Created by MUKUL on 11/8/16.
 */

import android.view.View;

public class RecyclerView_OnClickListener {
    /** Interface for Item Click over Recycler View Items **/
    public interface OnClickListener {
        public void OnItemClick(View view, int position);
    }
}
