package com.priyo.go.view.fragment.business;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Api.ApiForbiddenResponse;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.Fragments.OnFragmentInteractionListener;
import com.priyo.go.Model.api.graph.response.BusinessListResponse;
import com.priyo.go.Model.node.Business;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.Utilities.Utilities;
import com.priyo.go.controller.adapter.BusinessListController;
import com.priyo.go.view.activity.business.BusinessDetailsActivity;
import com.priyo.go.view.adapter.RecyclerViewAdapter;
import com.priyo.go.view.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sajid.shahriar on 5/3/17.
 */

public class BusinessListFragment extends Fragment implements HttpResponseListener, RecyclerView.OnItemClickListener, RecyclerView.OnRecyclerViewScrollListener {

    private static final int MAX_FETCH_VALUE = 10;
    private final Gson gson = new GsonBuilder().create();
    public boolean mUserScrolled = false;
    private HttpRequestGetAsyncTask getPeopleListTask;
    private List<Business> businessList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private BusinessListController businessListController;
    private View rootView;
    private RecyclerView businessRecyclerView;
    private String accessToken;
    private ProgressDialog progressDialog;
    private View progressBarLayout;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private String parameterName;
    private String parameterValue;
    private int pageNumber;
    private int total;

    public BusinessListFragment() {

    }

    public static BusinessListFragment newInstance(Bundle bundle) {
        BusinessListFragment peopleListFragment = new BusinessListFragment();
        peopleListFragment.setArguments(bundle);
        return peopleListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        parameterName = bundle.getString("fragmentActionType");
        parameterValue = bundle.getString(parameterName);
        pageNumber = 0;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ApplicationTag, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_business_list, container, false);
        }
        progressBarLayout = rootView.findViewById(R.id.progress_bar_layout);
        businessRecyclerView = (RecyclerView) rootView.findViewById(R.id.business_recycler_view);
        businessRecyclerView.setAsListType(RecyclerView.VERTICAL_LAYOUT);
        businessRecyclerView.setOnRecyclerViewScrollListener(this);
        businessRecyclerView.setOnItemClickListener(this);

        businessList = new ArrayList<>();
        businessListController = new BusinessListController(getActivity(), businessList);
        recyclerViewAdapter = new RecyclerViewAdapter(businessListController);
        businessRecyclerView.setAdapter(recyclerViewAdapter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Business List...");
        progressDialog.setCancelable(false);

        progressBarLayout.setVisibility(View.GONE);
        fetchPeopleList();
        progressDialog.show();
        return rootView;
    }

    private void fetchPeopleList() {
        final String url;
        Map<String, String> params = new HashMap<>();
        if (parameterName.equals(ApiConstants.Parameter.QUERY_STRING_PARAMETER)) {
            url = ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + ApiConstants.EndPoint.BUSINESS_SEARCH_ENDPOINT;
            params.put(ApiConstants.Parameter.QUERY_STRING_PARAMETER, parameterValue);
        } else {
            url = ApiConstants.Url.SPIDER_API + ApiConstants.Module.GRAPH_MODULE + ApiConstants.EndPoint.BUSINESS_LIST_ENDPOINT;
            params.put(ApiConstants.Parameter.RELATED_TO_ID_PARAMETER, parameterValue);
        }
        params.put(ApiConstants.Parameter.SORTING_PARAMETER, "name");
        params.put(ApiConstants.Parameter.SORTING_ORDER_PARAMETER, "asc");
        params.put(ApiConstants.Parameter.RELATED_TO_ID_PARAMETER, parameterValue);
        params.put(ApiConstants.Parameter.PAGE_SIZE_PARAMETER, Integer.toString(MAX_FETCH_VALUE));
        params.put(ApiConstants.Parameter.PAGE_NUMBER_PARAMETER, Integer.toString(pageNumber));

        getPeopleListTask = new HttpRequestGetAsyncTask(ApiConstants.Command.PHONEBOOK_LIST_FETCH, url, getActivity(), params, this);
        getPeopleListTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        getPeopleListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void httpResponseReceiver(@Nullable HttpResponseObject result) {
        progressDialog.cancel();
        progressBarLayout.setVisibility(View.GONE);
        getPeopleListTask = null;
        Utilities.hideKeyboard(getActivity());
        businessRecyclerView.requestFocus();
        if (result == null) {
            Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            switch (result.getStatus()) {
                case ApiConstants.StatusCode.S_OK:
                case ApiConstants.StatusCode.S_ACCEPTED:
                    BusinessListResponse peopleListResponse = gson.fromJson(result.getJsonString(), BusinessListResponse.class);
                    total = peopleListResponse.getTotalCount();
                    businessList = peopleListResponse.getData();
                    businessListController.addAllItem(businessList);
                    recyclerViewAdapter.notifyDataSetChanged();
                    break;
                case ApiConstants.StatusCode.S_UNAUTHORIZED:
                case ApiConstants.StatusCode.S_FORBIDDEN:
                    ApiForbiddenResponse apiForbiddenResponse = gson.fromJson(result.getJsonString(), ApiForbiddenResponse.class);
                    if (!TextUtils.isEmpty(apiForbiddenResponse.getDetail())) {
                        Toast.makeText(getContext(), apiForbiddenResponse.getDetail(), Toast.LENGTH_LONG).show();
                    } else if (!TextUtils.isEmpty(apiForbiddenResponse.getMessage())) {
                        Toast.makeText(getContext(), apiForbiddenResponse.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), R.string.unauthorized, Toast.LENGTH_LONG).show();
                    }
                    break;
                case ApiConstants.StatusCode.S_NOT_FOUND:
                case ApiConstants.StatusCode.S_SERVER_ERROR:
                    Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onItemClick(View clickedItemView, int position, long id) {
        if (position >= 0) {
            Business business = businessListController.getItem(position);
            Intent intent = new Intent(getActivity(), BusinessDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("nodeID", business.getNodeID().toString());
            intent.putExtra("nodeTitle", "Business");
            startActivity(intent);
            return true;
        } else
            return false;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            mUserScrolled = true;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int lastVisibleItemPosition = recyclerView.findLastVisibleItemPosition();
        if ((lastVisibleItemPosition + 1) == (MAX_FETCH_VALUE * (pageNumber + 1))) {
            if (businessListController.getItemCount() < total) {
                pageNumber++;
                progressBarLayout.setVisibility(View.VISIBLE);
                fetchPeopleList();
            }
        }

    }
}
