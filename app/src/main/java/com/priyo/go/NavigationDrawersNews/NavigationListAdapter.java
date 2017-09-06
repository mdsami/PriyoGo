package com.priyo.go.NavigationDrawersNews;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.priyo.go.R;

public class NavigationListAdapter extends BaseExpandableListAdapter {


    private Context context;
    private NavigationDrawerCallbacks mNavigationDrawerCallbacks;
    private String [] menu = {"হোম", "Phonebook", "Others"};
    private String [] menuSlug = {"home", "", ""};
    private int[] menuIcon = {R.mipmap.ic_nav_home,R.mipmap.ic_nav_phone_book , R.mipmap.ic_nav_others };
    private String[] menuPhonebook = {"Contacts", "People", "Business"};
    private int[] menuIconPhone = {R.mipmap.ic_nav_contact, R.mipmap.ic_nav_people, R.mipmap.ic_nav_business};
    private String[] menuMore = {"Birthdays","Horoscope","Wish List", "Feedback", "Settings","About"};
    private int[] menuIconMore = {R.mipmap.ic_nav_birthday,R.mipmap.ic_nav_horoscope,R.mipmap.ic_nav_wish,R.mipmap.ic_nav_feedback,R.mipmap.ic_nav_settings, R.mipmap.ic_nav_about};


    public NavigationDrawerCallbacks getNavigationDrawerCallbacks() {
        return mNavigationDrawerCallbacks;
    }

    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks navigationDrawerCallbacks) {
        mNavigationDrawerCallbacks = navigationDrawerCallbacks;
    }

    public NavigationListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        if(listPosition==1) {
            return this.menuPhonebook[expandedListPosition];
        }
        else if(listPosition==2) {
            return this.menuMore[expandedListPosition];
        }
        else{
            return null;
        }
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.item_drawer_news_cat, null);

        RelativeLayout root = (RelativeLayout) convertView.findViewById(R.id.root);
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.item_name);
        ImageView ic = (ImageView) convertView.findViewById(R.id.ic);

        if(listPosition==1) {
            listTitleTextView.setText(menuPhonebook[expandedListPosition]);
            ic.setImageResource(menuIconPhone[expandedListPosition]);
        }
        else if(listPosition==2) {
            listTitleTextView.setText(menuMore[expandedListPosition]);
            ic.setImageResource(menuIconMore[expandedListPosition]);
        }

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNavigationDrawerCallbacks != null){

                    if(listPosition==1) {
                        mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(listPosition,expandedListPosition,"",menuPhonebook[expandedListPosition]);
                    }
                    else if(listPosition==2) {
                        mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(listPosition,expandedListPosition,"",menuMore[expandedListPosition]);
                    }
                }

            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        int size=0;
        if(listPosition==1) {
            size=menuPhonebook.length;
        }
        else if(listPosition==2) {
            size=menuMore.length;
        }
        else{
            size=0;
        }
        return size;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.menu[listPosition];
    }

    @Override
    public int getGroupCount() {
        return this.menu.length;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(final int listPosition,final boolean isExpanded,
                             View convertView,final ViewGroup parent) {

        String listTitle = (String) getGroup(listPosition);
        int childCounts =0;
        if(listPosition==1)
            childCounts = this.menuPhonebook.length;
        else if(listPosition==2)
            childCounts =this.menuMore.length;
        else
            childCounts =0;


        LayoutInflater layoutInflater;
        TextView listTitleTextView ;

        ImageView plus ;
        final ImageView indic;
        final RelativeLayout imageRl;

        if(childCounts>0){
            layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_drawer_item, null);
            listTitleTextView = (TextView) convertView.findViewById(R.id.item_name);

            plus = (ImageView) convertView.findViewById(R.id.ic);
            indic = (ImageView) convertView.findViewById(R.id.indic);
            imageRl = (RelativeLayout) convertView
                    .findViewById(R.id.image_rl);

            listTitleTextView.setText(listTitle);
            plus.setImageResource(menuIcon[listPosition]);


            listTitleTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(isExpanded){

                        ((ExpandableListView) parent).collapseGroup(listPosition);
                        imageRl.setVisibility(View.GONE);
                        indic.setImageResource(R.mipmap.arrow_up);
                    }
                    else {

                        ((ExpandableListView) parent).expandGroup(listPosition, true);
                        indic.setImageResource(R.mipmap.arrow_down);
                    }

                }
            });

            indic.setVisibility(View.VISIBLE);
            if(isExpanded) {
                imageRl.setVisibility(View.GONE);
                indic.setImageResource(R.mipmap.arrow_up);
            }else {

                indic.setImageResource(R.mipmap.arrow_down);
            }

            listTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mNavigationDrawerCallbacks != null)
                        mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(listPosition, -1,"","");
                }
            });
        }

        else{
            layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_drawer_news_cat, null);

            RelativeLayout root = (RelativeLayout) convertView.findViewById(R.id.root);
            listTitleTextView = (TextView) convertView.findViewById(R.id.item_name);
            ImageView ic = (ImageView) convertView.findViewById(R.id.ic);

            listTitleTextView.setText(menu[listPosition]);
            ic.setImageResource(menuIcon[listPosition]);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mNavigationDrawerCallbacks != null){
                        mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(listPosition,-1,
                                    menuSlug[listPosition],menu[listPosition]);
                    }
                }
            });

            listTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(listPosition,-1,
                            menuSlug[listPosition],menu[listPosition]);
                }
            });
        }





        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}
