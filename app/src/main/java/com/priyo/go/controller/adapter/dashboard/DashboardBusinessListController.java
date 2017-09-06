package com.priyo.go.controller.adapter.dashboard;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.priyo.go.Model.node.Business;
import com.priyo.go.R;
import com.priyo.go.controller.adapter.IRecyclerViewAdapterController;
import com.priyo.go.view.adapter.holder.DashboardBusinessItemViewHolder;

import java.util.List;

/**
 * Created by sajid.shahriar on 4/27/17.
 */

public class DashboardBusinessListController implements IRecyclerViewAdapterController<DashboardBusinessItemViewHolder, Business> {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Business> businessList;

    public DashboardBusinessListController(Activity activity, List<Business> businessList) {
        this.activity = activity;
        this.businessList = businessList;
        layoutInflater = this.activity.getLayoutInflater();
    }

    @Override
    public DashboardBusinessItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DashboardBusinessItemViewHolder(layoutInflater.inflate(R.layout.item_dashboard_business, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DashboardBusinessItemViewHolder dashboardBusinessItemViewHolder, int i) {
        Business business = businessList.get(i);

        dashboardBusinessItemViewHolder.businessProfileImageView.setOval(false);
        if (!TextUtils.isEmpty(business.getNodePhoto()))
            dashboardBusinessItemViewHolder.businessProfileImageView.setProfilePicture(business.getNodePhoto(), true);
        else
            dashboardBusinessItemViewHolder.businessProfileImageView.setProfilePicture("", true);
        if (!TextUtils.isEmpty(business.getNodeTitle()))
            dashboardBusinessItemViewHolder.businessNameTextView.setText(business.getNodeTitle());
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    @Override
    public void updateItemList(List<Business> t) {
        this.businessList.clear();
        this.businessList.addAll(t);
    }

    @Override
    public void addItem(Business business) {
        this.businessList.add(business);
    }

    @Override
    public Business getItem(int position) {
        return this.businessList.get(position);
    }

    @Override
    public void addAllItem(List<Business> businessList) {
        this.businessList.addAll(businessList);
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
    public void removeAt(int position) {
        this.businessList.remove(position);
    }
}
