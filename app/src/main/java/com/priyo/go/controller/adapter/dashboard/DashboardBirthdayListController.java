package com.priyo.go.controller.adapter.dashboard;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.priyo.go.Model.Friend.BirthdayNode;
import com.priyo.go.R;
import com.priyo.go.controller.adapter.IRecyclerViewAdapterController;
import com.priyo.go.view.adapter.holder.BirthdayDashboardItemViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by sajid.shahriar on 4/27/17.
 */

public class DashboardBirthdayListController implements IRecyclerViewAdapterController<BirthdayDashboardItemViewHolder, BirthdayNode> {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<BirthdayNode> birthdayNodeList;
    private SimpleDateFormat birthDateFormat;


    public DashboardBirthdayListController(Activity activity, List<BirthdayNode> birthdayNodeList) {
        this.activity = activity;
        this.birthdayNodeList = birthdayNodeList;
        birthDateFormat = new SimpleDateFormat("MMMM dd", Locale.US);
        layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public BirthdayDashboardItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BirthdayDashboardItemViewHolder(layoutInflater.inflate(R.layout.item_dashboard_birthday, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(BirthdayDashboardItemViewHolder dashboardItemViewHolder, int i) {
        BirthdayNode birthdayNode = birthdayNodeList.get(i);
        dashboardItemViewHolder.profileImageView.setProfilePicture(R.mipmap.ic_profile);
        if (!TextUtils.isEmpty(birthdayNode.getPhoto()))
            dashboardItemViewHolder.profileImageView.setProfilePicture(birthdayNode.getPhoto(), false);
        else
            dashboardItemViewHolder.profileImageView.setProfilePicture("", false);
        if (!TextUtils.isEmpty(birthdayNode.getName()))
            dashboardItemViewHolder.nameTextView.setText(birthdayNode.getName());
        if (birthdayNode.getDateOfBirth() != null) {
            dashboardItemViewHolder.birthdayDateTextView.setText(birthdayNode.getDateOfBirth());
        }
    }

    @Override
    public int getItemCount() {
        return birthdayNodeList.size();
    }

    @Override
    public void updateItemList(List<BirthdayNode> t) {
        this.birthdayNodeList.clear();
        this.birthdayNodeList.addAll(t);
    }

    @Override
    public void addItem(BirthdayNode birthdayNode) {
        this.birthdayNodeList.add(birthdayNode);
    }

    @Override
    public BirthdayNode getItem(int position) {
        return this.birthdayNodeList.get(position);
    }

    @Override
    public void addAllItem(List<BirthdayNode> t) {
        this.birthdayNodeList.addAll(t);
    }

    @Override
    public void clear() {
        this.birthdayNodeList.clear();
    }

    @Override
    public void removeItem(BirthdayNode birthdayNode) {
        this.birthdayNodeList.remove(birthdayNode);
    }

    @Override
    public void removeAt(int position) {
        this.birthdayNodeList.remove(position);
    }
}
