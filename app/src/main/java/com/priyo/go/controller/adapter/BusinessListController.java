package com.priyo.go.controller.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.priyo.go.Model.node.Business;
import com.priyo.go.R;
import com.priyo.go.view.adapter.holder.BusinessListItemHolder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sajid.shahriar on 5/1/17.
 */

public class BusinessListController implements IRecyclerViewAdapterController<BusinessListItemHolder, Business> {

    private Activity activity;
    private List<Business> businessList;
    private LayoutInflater layoutInflater;


    public BusinessListController(Activity activity, List<Business> businessList) {
        this.activity = activity;
        this.businessList = businessList;
        layoutInflater = this.activity.getLayoutInflater();
    }

    @Override
    public BusinessListItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BusinessListItemHolder(layoutInflater.inflate(R.layout.item_business_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(BusinessListItemHolder businessListItemHolder, int i) {
        Business business = getItem(i);

        if (!TextUtils.isEmpty(business.getNodeTitle()))
            businessListItemHolder.businessNameTextView.setText(business.getNodeTitle());
        if (!TextUtils.isEmpty(business.getNodeTag()))
            businessListItemHolder.businessTagTextView.setText(business.getNodeTag());
        if (!TextUtils.isEmpty(business.getNodePhoto()))
            businessListItemHolder.businessProfileImageView.setProfilePicture(business.getNodePhoto(), false);
        else
            businessListItemHolder.businessProfileImageView.setProfilePicture("", false);
    }

    @Override
    public int getItemCount() {
        if (businessList != null) {
            return businessList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void updateItemList(List<Business> peopleList) {
        this.businessList.clear();
        this.businessList.addAll(peopleList);
    }

    @Override
    public void addItem(Business people) {
        addAllItem(Arrays.asList(people));
    }

    @Override
    public Business getItem(int position) {
        return this.businessList.get(position);
    }

    @Override
    public void addAllItem(List<Business> peopleList) {
        this.businessList.addAll(peopleList);
    }

    @Override
    public void clear() {
        this.businessList.clear();
    }

    @Override
    public void removeItem(Business business) {
        this.businessList.remove(business);
    }

    @Override
    public void removeAt(int position) throws IndexOutOfBoundsException {
        this.businessList.remove(position);
    }
}
