package com.priyo.go.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by sajid.shahriar on 5/1/17.
 */

public interface IRecyclerViewAdapterController<T extends RecyclerView.ViewHolder, K> {

    T onCreateViewHolder(ViewGroup viewGroup, int i);

    void onBindViewHolder(T t, int i);

    int getItemCount();

    void updateItemList(List<K> t);

    void addItem(K t);

    K getItem(int position);

    void addAllItem(List<K> t);

    void clear();

    void removeItem(K t);

    void removeAt(int position);
}
