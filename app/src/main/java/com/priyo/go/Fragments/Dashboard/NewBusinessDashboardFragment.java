package com.priyo.go.Fragments.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import com.priyo.go.Model.api.graph.response.BusinessListResponse;
import com.priyo.go.Model.node.Business;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.MyApplication;
import com.priyo.go.controller.adapter.dashboard.DashboardBusinessListController;
import com.priyo.go.database.model.NearbyBusiness;
import com.priyo.people.database.model.NearbyBusinessDao;
import com.priyo.go.view.activity.business.BusinessActivity;
import com.priyo.go.view.activity.business.BusinessDetailsActivity;
import com.priyo.go.view.adapter.RecyclerViewAdapter;
import com.priyo.go.view.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewBusinessDashboardFragment extends Fragment implements View.OnClickListener, RecyclerView.OnItemClickListener, HttpResponseListener {

    private static final String TAG = NewBusinessDashboardFragment.class.getSimpleName();
    private final Gson gson = new GsonBuilder().create();
    BusinessListResponse businessListResponse;
    private HttpRequestGetAsyncTask mBusinessCategoryTask = null;
    private View rootView;
    private TextView fragmentTitleTextView;
    private Button showMoreButton;
    private ProgressBar progressBar;
    private RecyclerView newBusinessRecyclerView;
    private DashboardBusinessListController dashboardItemListController;
    private RecyclerViewAdapter recyclerViewAdapter;
    private String accessToken;
    private SharedPreferences preferences;
    private NearbyBusinessDao nearbyBusinessDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(@Nullable Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        accessToken = preferences.getString(Constants.ACCESS_TOKEN_KEY, "");
        nearbyBusinessDao = ((MyApplication) getActivity().getApplication()).getDaoSession().getNearbyBusinessDao();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (nearbyBusinessDao.count() > 0) {
            List<NearbyBusiness> nearbyBusinessList = nearbyBusinessDao.loadAll();
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.VISIBLE);
            fragmentTitleTextView.setText("Business near you");
            newBusinessRecyclerView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            dashboardItemListController = new DashboardBusinessListController(getActivity(), convertToNewBusinessList(nearbyBusinessList));
            recyclerViewAdapter = new RecyclerViewAdapter(dashboardItemListController);
            newBusinessRecyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)");
        // Inflate the layout for this fragment
        if (rootView == null)
            rootView = inflater.inflate(R.layout.fragment_new_business_dashboard, container, false);

        fragmentTitleTextView = (TextView) rootView.findViewById(R.id.fragment_title_text_view);
        showMoreButton = (Button) rootView.findViewById(R.id.show_more_button);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        newBusinessRecyclerView = (RecyclerView) rootView.findViewById(R.id.new_business_recycler_view);


        newBusinessRecyclerView.setOnItemClickListener(this);
        newBusinessRecyclerView.setAsGridType(2);

        progressBar.setVisibility(View.GONE);

        showMoreButton.setOnClickListener(this);


        fetchNewBusinessList();

        return rootView;
    }

    private void fetchNewBusinessList() {
        Log.d(TAG, "fetchNewBusinessList()");
        List<NearbyBusiness> nearbyBusinessList = nearbyBusinessDao.loadAll();
        if (nearbyBusinessList.isEmpty()) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiConstants.Parameter.PAGE_SIZE_PARAMETER, "6");
            params.put(ApiConstants.Parameter.PAGE_NUMBER_PARAMETER, "0");
            fragmentTitleTextView.setText("Business you may like");
            progressBar.setVisibility(View.VISIBLE);
            mBusinessCategoryTask = new HttpRequestGetAsyncTask(ApiConstants.Command.BUSINESS_LIST_FETCH,
                    ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + ApiConstants.EndPoint.BUSINESS_LIST_ENDPOINT, getActivity(), params, this);
            mBusinessCategoryTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
            mBusinessCategoryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.VISIBLE);
            fragmentTitleTextView.setText("Business near you");
            newBusinessRecyclerView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            dashboardItemListController = new DashboardBusinessListController(getActivity(), convertToNewBusinessList(nearbyBusinessList));
            recyclerViewAdapter = new RecyclerViewAdapter(dashboardItemListController);
            newBusinessRecyclerView.setAdapter(recyclerViewAdapter);
        }

    }

    private List<Business> convertToNewBusinessList(List<NearbyBusiness> nearbyBusinessList) {
        List<Business> businessList = new ArrayList<>();
        for (NearbyBusiness nearbyBusiness : nearbyBusinessList) {
            businessList.add(new Business(nearbyBusiness.getNodeID(), nearbyBusiness.getNodeLabel(), nearbyBusiness.getNodeTitle(), nearbyBusiness.getNodeTag(), nearbyBusiness.getNodePhoto(), nearbyBusiness.getTopupOffered()));
        }
        return businessList;
    }

    @Override
    public boolean onItemClick(View clickedItemView, int position, long id) {
        Log.d(TAG, "onItemClick(View clickedItemView, int position, long id)");
        Intent intent = new Intent(getActivity(), BusinessDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("nodeID", dashboardItemListController.getItem(position).getNodeID().toString());
        intent.putExtra("nodeTitle", "Business");
        startActivity(intent);
        return true;
    }

    @Override
    public void httpResponseReceiver(@Nullable HttpResponseObject result) {
        Log.d(TAG, "httpResponseReceiver(@Nullable HttpResponseObject result)");
        if (result == null) {
            Log.d(TAG, "Null Response");
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.GONE);
            mBusinessCategoryTask = null;
            if (getContext() != null)
                Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        String message = "";

        try {
            switch (result.getStatus()) {
                case ApiConstants.StatusCode.S_OK:
                case ApiConstants.StatusCode.S_ACCEPTED:
                    businessListResponse = gson.fromJson(result.getJsonString(), BusinessListResponse.class);
                    if (businessListResponse != null && businessListResponse.getData() != null && businessListResponse.getData().size() > 0) {
                        progressBar.setVisibility(View.GONE);
                        rootView.setVisibility(View.VISIBLE);
                        newBusinessRecyclerView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        if (dashboardItemListController == null || dashboardItemListController.getItemCount() == 0) {
                            dashboardItemListController = new DashboardBusinessListController(getActivity(), businessListResponse.getData());
                            recyclerViewAdapter = new RecyclerViewAdapter(dashboardItemListController);
                            newBusinessRecyclerView.setAdapter(recyclerViewAdapter);
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
            message = getActivity().getString(R.string.service_not_available);
            progressBar.setVisibility(View.GONE);
            rootView.setVisibility(View.GONE);
        }

        mBusinessCategoryTask = null;
        if (!TextUtils.isEmpty(message))
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick(View v)");
        switch (v.getId()) {
            case R.id.show_more_button:
                Intent i = new Intent(getContext(), BusinessActivity.class);
                startActivity(i);
                break;
        }
    }
}

