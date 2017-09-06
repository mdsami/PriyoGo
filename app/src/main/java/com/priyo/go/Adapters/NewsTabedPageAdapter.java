package com.priyo.go.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.priyo.go.Fragments.NewsTabed.HomeFragmentNews;
import com.priyo.go.Fragments.NewsTabed.TabFragmentNews;

public class NewsTabedPageAdapter extends FragmentPagerAdapter {
    private String [] tabTitle = {"হোম","শীর্ষ সংবাদ", "বাংলাদেশ", "আন্তর্জাতিক", "রাজনীতি", "ব্যবসা",  "খেলা", "বিনোদন", "লাইফ","প্রযুক্তি", "ভ্রমণ", "মতামত" };
    private String [] tabSlug = {"home","latest", "bangladesh", "international", "politics", "business",  "sports",
            "entertainment", "life","tech", "travel" , "opinion"};

    public NewsTabedPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return HomeFragmentNews.newInstance(position,tabSlug[position]);
        }
        else{

            return TabFragmentNews.newInstance(position,tabSlug[position]);
        }
    }

    @Override
    public int getCount() {
        return tabTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitle[position];
    }
}
