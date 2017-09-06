package com.priyo.go.Fragments.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Api.ApiForbiddenResponse;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Model.api.graph.response.PeopleListResponse;
import com.priyo.go.Model.node.People;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.MyApplication;
import com.priyo.go.controller.adapter.dashboard.DashboardPeopleListController;
import com.priyo.go.database.model.NearbyPeople;
import com.priyo.people.database.model.NearbyPeopleDao;
import com.priyo.go.view.activity.people.PeopleActivity;
import com.priyo.go.view.activity.people.PeopleDetailsActivity;
import com.priyo.go.view.adapter.RecyclerViewAdapter;
import com.priyo.go.view.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.priyo.go.view.widget.RecyclerView.HORIZONTAL_LAYOUT;

public class NewPeopleDashboardFragment extends Fragment implements View.OnClickListener, RecyclerView.OnItemClickListener, HttpResponseListener {

    private static final String TAG = NewBusinessDashboardFragment.class.getSimpleName();
    private final Gson gson = new GsonBuilder().create();

    private HttpRequestGetAsyncTask getNewPeopleListTask = null;

    private View rootView;
    private TextView fragmentTitleTextView;
    private Button showMoreButton;
    private ProgressBar progressBar;
    private RecyclerView newPeopleRecyclerView;

    private DashboardPeopleListController dashboardItemListController;
    private RecyclerViewAdapter recyclerViewAdapter;

    private String accessToken;
    private SharedPreferences preferences;
    private NearbyPeopleDao nearbyPeopleDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        accessToken = preferences.getString(Constants.ACCESS_TOKEN_KEY, "");
        nearbyPeopleDao = ((MyApplication) getActivity().getApplication()).getDaoSession().getNearbyPeopleDao();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (nearbyPeopleDao.count() > 0) {
            List<NearbyPeople> nearbyPeopleList = nearbyPeopleDao.loadAll();
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.VISIBLE);
            fragmentTitleTextView.setText("People near you");
            dashboardItemListController = new DashboardPeopleListController(getActivity(), convertToNewPeopleList(nearbyPeopleList));
            recyclerViewAdapter = new RecyclerViewAdapter(dashboardItemListController);
            newPeopleRecyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null)
            rootView = inflater.inflate(R.layout.fragment_new_people_dashboard, container, false);

        fragmentTitleTextView = (TextView) rootView.findViewById(R.id.fragment_title_text_view);
        showMoreButton = (Button) rootView.findViewById(R.id.show_more_button);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        newPeopleRecyclerView = (RecyclerView) rootView.findViewById(R.id.new_people_recycler_view);
        newPeopleRecyclerView.setOnItemClickListener(this);
        newPeopleRecyclerView.setAsListType(HORIZONTAL_LAYOUT);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(newPeopleRecyclerView.getContext(), newPeopleRecyclerView.getOrientation());
        newPeopleRecyclerView.addItemDecoration(itemDecoration);
        progressBar.setVisibility(View.GONE);

        showMoreButton.setOnClickListener(this);


        fetchNewPeopleList();

        return rootView;
    }

    private void fetchNewPeopleList() {
        List<NearbyPeople> nearbyPeopleList = nearbyPeopleDao.loadAll();
        if (nearbyPeopleList.isEmpty()) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiConstants.Parameter.PAGE_SIZE_PARAMETER, "10");
            params.put(ApiConstants.Parameter.PAGE_NUMBER_PARAMETER, "0");
            params.put(ApiConstants.Parameter.TYPE_PARAMETER, "People");
            fragmentTitleTextView.setText("People you may know");
            progressBar.setVisibility(View.VISIBLE);
            getNewPeopleListTask = new HttpRequestGetAsyncTask(ApiConstants.Command.PHONEBOOK_LIST_FETCH,
                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + ApiConstants.EndPoint.PHONE_BOOK_LIST_FETCH_ENDPOINT, getActivity(), params, this);
            getNewPeopleListTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
            getNewPeopleListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.VISIBLE);
            fragmentTitleTextView.setText("People near you");
            dashboardItemListController = new DashboardPeopleListController(getActivity(), convertToNewPeopleList(nearbyPeopleList));
            recyclerViewAdapter = new RecyclerViewAdapter(dashboardItemListController);
            newPeopleRecyclerView.setAdapter(recyclerViewAdapter);
        }

    }

    private List<People> convertToNewPeopleList(List<NearbyPeople> nearbyPeopleList) {

        List<People> peopleList = new ArrayList<>();
        for (NearbyPeople nearbyPeople : nearbyPeopleList) {
            peopleList.add(new People(nearbyPeople.getNodeID(), nearbyPeople.getName(), nearbyPeople.getTag(), nearbyPeople.getPhoto(), nearbyPeople.getPhoneNumber()));
        }
        return peopleList;
    }


    @Override
    public boolean onItemClick(View clickedItemView, int position, long id) {
        if (position >= 0) {
            People people = dashboardItemListController.getItem(position);
            Intent intent = new Intent(getActivity(), PeopleDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("hashedMobileNumber", people.getPhoneNumber());
            startActivity(intent);
            return true;
        } else
            return false;
    }

    @Override
    public void httpResponseReceiver(@Nullable HttpResponseObject result) {
        if (result == null) {
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.GONE);
            getNewPeopleListTask = null;
            if (getContext() != null)
                Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        String message = "";

        try {
            switch (result.getStatus()) {
                case ApiConstants.StatusCode.S_OK:
                case ApiConstants.StatusCode.S_ACCEPTED:
                    PeopleListResponse peopleListResponse = gson.fromJson(result.getJsonString(), PeopleListResponse.class);
                    if (peopleListResponse != null && peopleListResponse.getData() != null && peopleListResponse.getData().size() > 0) {
                        progressBar.setVisibility(View.GONE);
                        rootView.setVisibility(View.VISIBLE);
                        if (dashboardItemListController == null || dashboardItemListController.getItemCount() == 0) {
                            dashboardItemListController = new DashboardPeopleListController(getActivity(), peopleListResponse.getData());
                            recyclerViewAdapter = new RecyclerViewAdapter(dashboardItemListController);
                            newPeopleRecyclerView.setAdapter(recyclerViewAdapter);
                        }
                    } else {
                        throw new Exception();
                    }
                    break;
                case ApiConstants.StatusCode.S_FORBIDDEN:
                    ApiForbiddenResponse apiForbiddenResponse = gson.fromJson(result.getJsonString(), ApiForbiddenResponse.class);
                    message = apiForbiddenResponse.getDetail();
                    break;
                case ApiConstants.StatusCode.S_BAD_REQUEST:
                case ApiConstants.StatusCode.S_NOT_ACCEPTED:
                case ApiConstants.StatusCode.S_NOT_FOUND:
                case ApiConstants.StatusCode.S_SERVER_ERROR:
                    message = getString(R.string.service_not_available);
                    break;
            }
        } catch (Exception e) {
            message = getString(R.string.service_not_available);
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.GONE);
        }

        getNewPeopleListTask = null;
        if (!TextUtils.isEmpty(message))
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_more_button:
                Intent i = new Intent(getContext(), PeopleActivity.class);
                startActivity(i);
                break;
        }
    }


}

