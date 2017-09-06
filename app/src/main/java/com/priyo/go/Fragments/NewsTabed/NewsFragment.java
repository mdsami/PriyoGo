package com.priyo.go.Fragments.NewsTabed;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.priyo.go.Adapters.NewsTabedPageAdapter;
import com.priyo.go.R;


public class NewsFragment extends Fragment {

    private TabLayout tabLayout;
    public static ViewPager viewPager;


    public NewsFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_tabed, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        viewPager = (ViewPager)view.findViewById(R.id.view_pager);

        viewPager.setAdapter(new NewsTabedPageAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(6);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
