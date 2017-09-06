package com.priyo.go.controller.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.priyo.go.Model.WishDetail;
import com.priyo.go.R;
import com.priyo.go.view.adapter.holder.WishListItemViewHolder;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by sajid.shahriar on 5/1/17.
 */

public class WishListController implements IRecyclerViewAdapterController<WishListItemViewHolder, WishDetail> {

    private Activity activity;
    private List<WishDetail> wishDetailList;
    private LayoutInflater layoutInflater;
    private SimpleDateFormat wishDateFormat;


    public WishListController(Activity activity, List<WishDetail> wishDetailList) {
        this.activity = activity;
        this.wishDetailList = wishDetailList;
        layoutInflater = this.activity.getLayoutInflater();
        wishDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    }

    @Override
    public WishListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new WishListItemViewHolder(layoutInflater.inflate(R.layout.item_wish_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(WishListItemViewHolder wishListItemViewHolder, int i) {
        WishDetail wishDetail = getItem(i);

        wishListItemViewHolder.userNameTextView.setText(wishDetail.getName());
        wishListItemViewHolder.wishDetailsTextView.setText(wishDetail.getWish());
        wishListItemViewHolder.wishDateTextView.setText(wishDateFormat.format(wishDetail.getCreatedAt()));
        if (!TextUtils.isEmpty(wishDetail.getImageUrl())) {
            wishListItemViewHolder.userProfileImageView.setProfilePicture(wishDetail.getImageUrl(), false);
        }
    }

    @Override
    public int getItemCount() {
        if (wishDetailList != null) {
            return wishDetailList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void updateItemList(List<WishDetail> wishDetailList) {
        this.wishDetailList.clear();
        this.wishDetailList.addAll(wishDetailList);
    }

    @Override
    public void addItem(WishDetail wishDetail) {
        addAllItem(Arrays.asList(wishDetail));
    }

    @Override
    public WishDetail getItem(int position) {
        return this.wishDetailList.get(position);
    }

    @Override
    public void addAllItem(List<WishDetail> wishDetailList) {
        this.wishDetailList.addAll(wishDetailList);
    }

    @Override
    public void clear() {
        this.wishDetailList.clear();
    }

    @Override
    public void removeItem(WishDetail wishDetail) {
        this.wishDetailList.remove(wishDetail);
    }

    @Override
    public void removeAt(int position) throws IndexOutOfBoundsException {
        this.wishDetailList.remove(position);
    }
}
