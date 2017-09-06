package com.priyo.go.controller.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.priyo.go.Model.node.People;
import com.priyo.go.R;
import com.priyo.go.view.adapter.holder.PeopleListItemHolder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sajid.shahriar on 5/1/17.
 */

public class PeopleListController implements IRecyclerViewAdapterController<PeopleListItemHolder, People> {

    private Activity activity;
    private List<People> peopleList;
    private LayoutInflater layoutInflater;


    public PeopleListController(Activity activity, List<People> peopleList) {
        this.activity = activity;
        this.peopleList = peopleList;
        layoutInflater = this.activity.getLayoutInflater();
    }

    @Override
    public PeopleListItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PeopleListItemHolder(layoutInflater.inflate(R.layout.item_people_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(PeopleListItemHolder peopleListItemHolder, int i) {
        People people = getItem(i);

        if (!TextUtils.isEmpty(people.getName()))
            peopleListItemHolder.peopleNameTextView.setText(people.getName());
        if (!TextUtils.isEmpty(people.getTag()))
            peopleListItemHolder.peopleTagTextView.setText(people.getTag());
        if (!TextUtils.isEmpty(people.getPhoto()))
            peopleListItemHolder.profilePictureImageView.setProfilePicture(people.getPhoto(), false);
        else
            peopleListItemHolder.profilePictureImageView.setProfilePicture("", false);
    }

    @Override
    public int getItemCount() {
        if (peopleList != null) {
            return peopleList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void updateItemList(List<People> peopleList) {
        this.peopleList.clear();
        this.peopleList.addAll(peopleList);
    }

    @Override
    public void addItem(People people) {
        addAllItem(Arrays.asList(people));
    }

    @Override
    public People getItem(int position) {
        return this.peopleList.get(position);
    }

    @Override
    public void addAllItem(List<People> peopleList) {
        this.peopleList.addAll(peopleList);
    }

    @Override
    public void clear() {
        this.peopleList.clear();
    }

    @Override
    public void removeItem(People categoryNode) {
        this.peopleList.remove(categoryNode);
    }

    @Override
    public void removeAt(int position) throws IndexOutOfBoundsException {
        this.peopleList.remove(position);
    }
}
