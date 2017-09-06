package com.priyo.go.view.widget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Sajid Shahriar
 * @version 1.0.0
 * @since 2016
 */

public class RecyclerView extends android.support.v7.widget.RecyclerView implements android.support.v7.widget.RecyclerView.OnItemTouchListener {

    public static final int HORIZONTAL_LAYOUT = HORIZONTAL;
    public static final int VERTICAL_LAYOUT = VERTICAL;
    private OnRecyclerViewScrollListener onRecyclerViewScrollListener;
    private GestureDetector gestureDetector;
    private OnItemClickListener onItemClickListener;
    private LayoutManager layoutManager;
    private boolean isSingleClickAllowed;
    private boolean requestDisallowInterceptTouchEvent = false;

    public RecyclerView(Context context) {
        super(context);
        initGestureDetector(context);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initGestureDetector(context);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initGestureDetector(context);
    }

    private void initGestureDetector(Context context) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return isSingleClickAllowed;
            }


            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }
        });
        addOnItemTouchListener(this);
    }

    public OnRecyclerViewScrollListener getOnRecyclerViewScrollListener() {
        return onRecyclerViewScrollListener;
    }

    public void setOnRecyclerViewScrollListener(OnRecyclerViewScrollListener onRecyclerViewScrollListener) {
        this.onRecyclerViewScrollListener = onRecyclerViewScrollListener;
        addOnScrollListener(new OnScrollListener());
    }

    @Override
    public boolean onInterceptTouchEvent(android.support.v7.widget.RecyclerView recyclerView, MotionEvent motionEvent) {
        requestDisallowInterceptTouchEvent(requestDisallowInterceptTouchEvent);
        View child = findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (child != null && onItemClickListener != null && gestureDetector.onTouchEvent(motionEvent)) {
            int position = getChildAdapterPosition(child);
            long id = getChildItemId(child);
            return onItemClickListener.onItemClick(child, position, id);
        }
        return false;
    }

    @Override
    public void onTouchEvent(android.support.v7.widget.RecyclerView rv, MotionEvent e) {

    }

    public void setRequestDisallowInterceptTouchEvent(boolean requestDisallowInterceptTouchEvent) {
        this.requestDisallowInterceptTouchEvent = requestDisallowInterceptTouchEvent;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if (onItemClickListener != null) {
            isSingleClickAllowed = true;
        }
    }

    public void setAsListType(@LayoutOrientation final int layoutOrientation) {
        layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(layoutOrientation);
        setLayoutManager(layoutManager);
        setHasFixedSize(true);
    }

    public void setAsGridType(int spanCount) {
        layoutManager = new GridLayoutManager(getContext(), spanCount);
        ((LinearLayoutManager) layoutManager).setOrientation(VERTICAL);
        setLayoutManager(layoutManager);
        setHasFixedSize(true);
    }

    public int getOrientation() {
        return layoutManager instanceof GridLayoutManager ?
                ((GridLayoutManager) layoutManager).getOrientation() :
                ((LinearLayoutManager) layoutManager).getOrientation();


    }

    public int findFirstCompletelyVisibleItemPosition() {
        return layoutManager instanceof GridLayoutManager ?
                ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() :
                ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();

    }


    public int findFirstVisibleItemPosition() {
        return layoutManager instanceof GridLayoutManager ?
                ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition() :
                ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

    }


    public int findLastCompletelyVisibleItemPosition() {
        return layoutManager instanceof GridLayoutManager ?
                ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() :
                ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();

    }

    public int findLastVisibleItemPosition() {
        return layoutManager instanceof GridLayoutManager ?
                ((GridLayoutManager) layoutManager).findLastVisibleItemPosition() :
                ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }


    public interface OnRecyclerViewScrollListener {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    public interface OnItemClickListener {
        boolean onItemClick(View clickedItemView, int position, long id);

    }

    @IntDef({HORIZONTAL_LAYOUT, VERTICAL_LAYOUT})
    @Retention(RetentionPolicy.SOURCE)
    @interface LayoutOrientation {
    }

    private class OnScrollListener extends android.support.v7.widget.RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState) {
            onRecyclerViewScrollListener.onScrollStateChanged(RecyclerView.this, newState);
        }

        @Override
        public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
            onRecyclerViewScrollListener.onScrolled(RecyclerView.this, dx, dy);
        }
    }
}
