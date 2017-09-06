package com.priyo.go.controller.adapter.dashboard;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.priyo.go.Model.node.People;
import com.priyo.go.R;
import com.priyo.go.controller.adapter.IRecyclerViewAdapterController;
import com.priyo.go.view.adapter.holder.DashboardPeopleItemViewHolder;

import java.util.List;

/**
 * Created by sajid.shahriar on 4/27/17.
 */

public class DashboardPeopleListController implements IRecyclerViewAdapterController<DashboardPeopleItemViewHolder, People> {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<People> peopleList;

    public DashboardPeopleListController(Activity activity, List<People> peopleList) {
        this.activity = activity;
        this.peopleList = peopleList;
        layoutInflater = this.activity.getLayoutInflater();
    }

    @Override
    public DashboardPeopleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DashboardPeopleItemViewHolder(layoutInflater.inflate(R.layout.item_dashboard_list_horizontal, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DashboardPeopleItemViewHolder dashboardPeopleItemViewHolder, int i) {
        People people = peopleList.get(i);
        if (!TextUtils.isEmpty(people.getPhoto()))
            dashboardPeopleItemViewHolder.itemProfileImageView.setProfilePicture(people.getPhoto(), false);
        if (!TextUtils.isEmpty(people.getName()))
            dashboardPeopleItemViewHolder.itemTitleTextView.setText(people.getName());
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    @Override
    public void updateItemList(List<People> t) {
        this.peopleList.clear();
        this.peopleList.addAll(t);
    }

    @Override
    public void addItem(People people) {
        this.peopleList.add(people);
    }

    @Override
    public People getItem(int position) {
        return this.peopleList.get(position);
    }

    @Override
    public void addAllItem(List<People> t) {
        this.peopleList.addAll(t);
    }

    @Override
    public void clear() {
        this.peopleList.clear();
    }

    @Override
    public void removeItem(People people) {
        this.peopleList.remove(people);
    }

    @Override
    public void removeAt(int position) {
        this.peopleList.remove(position);
    }
}
