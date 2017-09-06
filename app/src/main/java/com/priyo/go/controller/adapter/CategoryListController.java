package com.priyo.go.controller.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.priyo.go.Model.Business.CategoryNode;
import com.priyo.go.R;
import com.priyo.go.view.adapter.holder.CardCategoryViewHolder;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by sajid.shahriar on 5/1/17.
 */

public class CategoryListController implements IRecyclerViewAdapterController<CardCategoryViewHolder, CategoryNode> {

    private Activity activity;
    private List<CategoryNode> categoryNodeList;
    private LayoutInflater layoutInflater;
    private SimpleDateFormat wishDateFormat;


    public CategoryListController(Activity activity, List<CategoryNode> categoryNodeList) {
        this.activity = activity;
        this.categoryNodeList = categoryNodeList;
        layoutInflater = activity.getLayoutInflater();
        wishDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    }

    @Override
    public CardCategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CardCategoryViewHolder(layoutInflater.inflate(R.layout.item_card_category, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(CardCategoryViewHolder cardCategoryViewHolder, int i) {
        CategoryNode categoryNode = getItem(i);

        cardCategoryViewHolder.categoryNameTextView.setText(categoryNode.getNodeTitle());
    }

    @Override
    public int getItemCount() {
        if (categoryNodeList != null) {
            return categoryNodeList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void updateItemList(List<CategoryNode> categoryNodeList) {
        this.categoryNodeList.clear();
        this.categoryNodeList.addAll(categoryNodeList);
    }

    @Override
    public void addItem(CategoryNode categoryNode) {
        addAllItem(Arrays.asList(categoryNode));
    }

    @Override
    public CategoryNode getItem(int position) {
        return this.categoryNodeList.get(position);
    }

    @Override
    public void addAllItem(List<CategoryNode> categoryNodeList) {
        this.categoryNodeList.addAll(categoryNodeList);
    }

    @Override
    public void clear() {
        this.categoryNodeList.clear();
    }

    @Override
    public void removeItem(CategoryNode categoryNode) {
        this.categoryNodeList.remove(categoryNode);
    }

    @Override
    public void removeAt(int position) throws IndexOutOfBoundsException {
        this.categoryNodeList.remove(position);
    }
}
