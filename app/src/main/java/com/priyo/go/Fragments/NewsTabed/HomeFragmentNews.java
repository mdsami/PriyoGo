package com.priyo.go.Fragments.NewsTabed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.priyo.go.Fragments.Dashboard.NewBusinessDashboardFragment;
import com.priyo.go.Fragments.Dashboard.NewHoroscopeDashboardFragment;
import com.priyo.go.Fragments.Dashboard.NewPeopleDashboardFragment;
import com.priyo.go.Fragments.Dashboard.UpcomingBirthdayDashboardFragment;
import com.priyo.go.R;
import com.priyo.go.Utilities.GPSTracker;

public class HomeFragmentNews extends Fragment {

//    int page = 0;
//    int position;
    GPSTracker gps;
    TextView address, loc;
//    private String mSlug = "sports", mType = "Category";
//    private String mCatTitle = "Sports";


    public HomeFragmentNews() {
    }

    public static HomeFragmentNews newInstance(int page, String slug) {
        Bundle args = new Bundle();
        args.putInt("position", page);
        args.putString("slug", slug);
        HomeFragmentNews fragment = new HomeFragmentNews();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_news_tab, container, false);

//        address = (TextView) view.findViewById(R.id.test);
//        loc = (TextView) view.findViewById(R.id.loc);
//
//        loc.setVisibility(View.INVISIBLE);

//        this.mSlug = getArguments().getString("slug");
//        this.position = getArguments().getInt("position");

//        gps = new GPSTracker(getContext());
//        if (gps.canGetLocation()) {
//
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//
//            LocationAddress.getAddressFromLocation(latitude, longitude, getContext(), new GeocoderHandler());
//
//        }


//        switchToNewsFragment();
        switchToBirthdayFragment();
        switchToHorscopeFragment();
        switchToStrangerFragment();
        switchToBussinessFragment();

        return view;

    }


//    public void switchToNewsFragment() {
//        getFragmentManager().beginTransaction()
//                .replace(R.id.news_frame, new DashboardNewsFragment()).addToBackStack(null).commit();
//    }

    public void switchToBirthdayFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.birthday_frame, new UpcomingBirthdayDashboardFragment()).addToBackStack(null).commit();
    }

    public void switchToHorscopeFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.horscope_frame, new NewHoroscopeDashboardFragment()).addToBackStack(null).commit();
    }

    private void switchToStrangerFragment() {
        getFragmentManager().beginTransaction().
                replace(R.id.starnge_frame, new NewPeopleDashboardFragment()).addToBackStack(null).commit();

    }

    private void switchToBussinessFragment() {
        getFragmentManager().beginTransaction().
                replace(R.id.business_frame, new NewBusinessDashboardFragment()).addToBackStack(null).commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

//        if (gps.canGetLocation()) {
//
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//
//            LocationAddress.getAddressFromLocation(latitude, longitude, getContext(), new GeocoderHandler());
//
//        }
    }

//    private class GeocoderHandler extends Handler {
//        @Override
//        public void handleMessage(Message message) {
//            String locationAddress;
//            switch (message.what) {
//                case 1:
//                    Bundle bundle = message.getData();
//                    locationAddress = bundle.getString("address");
//                    break;
//                default:
//                    locationAddress = null;
//            }
//
//            if (!address.equals("")) {
//                loc.setVisibility(View.VISIBLE);
//                address.setText(locationAddress);
//            } else {
//                loc.setVisibility(View.INVISIBLE);
//            }
//
//            System.out.println("Text Loc " + locationAddress);
//        }
//    }
}