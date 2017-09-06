package com.priyo.go.view.fragment.wish;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priyo.go.Api.ApiForbiddenResponse;
import com.priyo.go.Api.HttpRequestGetAsyncTask;
import com.priyo.go.Api.HttpResponseListener;
import com.priyo.go.Api.HttpResponseObject;
import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.Model.WishDetail;
import com.priyo.go.Model.api.utility.response.WishListResponse;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.controller.adapter.WishListController;
import com.priyo.go.view.adapter.RecyclerViewAdapter;
import com.priyo.go.view.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class WishFulfilledFragment extends Fragment implements HttpResponseListener, RecyclerView.OnItemClickListener {

    private final SimpleDateFormat wishDateFormat = new SimpleDateFormat("MMM dd,yyyy", Locale.US);
    public List<WishDetail> wishDetailList = new ArrayList<>();
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
    private HttpRequestGetAsyncTask getAllWishListTask = null;
    private View rootView;
    private RecyclerView wishAllRecyclerView;
    private TextView noWishFoundTextView;
    private ProgressBar progressBar;
    private RecyclerViewAdapter recyclerViewAdapter;
    private WishListController wishListController;
    private String accessToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ApplicationTag, MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_wish_all, container, false);
        }

        wishAllRecyclerView = (RecyclerView) rootView.findViewById(R.id.wish_all_recycler_view);
        noWishFoundTextView = (TextView) rootView.findViewById(R.id.no_wish_found_text_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        wishAllRecyclerView.setAsListType(RecyclerView.VERTICAL_LAYOUT);
        wishAllRecyclerView.setOnItemClickListener(this);
        wishDetailList = new ArrayList<>();
        wishListController = new WishListController(getActivity(), wishDetailList);
        recyclerViewAdapter = new RecyclerViewAdapter(wishListController);
        wishAllRecyclerView.setAdapter(recyclerViewAdapter);
        progressBar.setVisibility(View.VISIBLE);

        fetchAllWishList();
        return rootView;
    }

    private void fetchAllWishList() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.Parameter.IS_COMPLETED_PARAMETER, "true");
        getAllWishListTask = new HttpRequestGetAsyncTask(ApiConstants.Command.WISH_LIST_FETCH,
                ApiConstants.Url.SPIDER_API + ApiConstants.Module.UTILITY_MODULE + ApiConstants.EndPoint.WISH_FIND_ENDPOINT, getContext(), params, this);
        getAllWishListTask.mHttpResponseListener = this;

        getAllWishListTask.addHeader(ApiConstants.Header.TOKEN, accessToken);
        getAllWishListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void httpResponseReceiver(HttpResponseObject result) {
        progressBar.setVisibility(View.GONE);
        getAllWishListTask = null;
        if (result == null) {
            Toast.makeText(getContext(), R.string.service_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (result.getStatus()) {
            case ApiConstants.StatusCode.S_OK:
            case ApiConstants.StatusCode.S_ACCEPTED:
                WishListResponse wishListResponse = gson.fromJson(result.getJsonString(), WishListResponse.class);
                wishDetailList = wishListResponse.getData();
                wishListController.updateItemList(wishDetailList);
                recyclerViewAdapter.notifyDataSetChanged();
                if (wishDetailList.size() == 0) {
                    wishAllRecyclerView.setVisibility(View.GONE);
                    noWishFoundTextView.setText("Sorry no fulfilled wish found");
                    noWishFoundTextView.setVisibility(View.VISIBLE);
                } else {
                    wishAllRecyclerView.setVisibility(View.VISIBLE);
                    noWishFoundTextView.setVisibility(View.GONE);
                }
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

    }

    @Override
    public boolean onItemClick(View clickedItemView, int position, long id) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_wish, null);
        dialogBuilder.setView(dialogView);

        final TextView tv_name = (TextView) dialogView.findViewById(R.id.title);
        final TextView tv_title = (TextView) dialogView.findViewById(R.id.wish_title);
        final TextView tv_date = (TextView) dialogView.findViewById(R.id.date);
        final TextView tv_desc = (TextView) dialogView.findViewById(R.id.wish_desc);
        final ProfileImageView img_android = (ProfileImageView) dialogView.findViewById(R.id.thumbnail);

        WishDetail w = wishDetailList.get(position);

        tv_name.setText(w.getName());
        tv_title.setText(w.getTitle());
        tv_date.setText(wishDateFormat.format(w.getCreatedAt()));
        tv_desc.setText(w.getWish());

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
        return false;
    }
}

