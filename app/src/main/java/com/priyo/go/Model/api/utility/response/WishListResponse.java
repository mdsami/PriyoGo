package com.priyo.go.Model.api.utility.response;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.Model.WishDetail;

import java.io.Serializable;
import java.util.List;

public class WishListResponse implements Serializable{

    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("data")
    private List<WishDetail> data;

    public WishListResponse() {

    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<WishDetail> getData() {
        return data;
    }
}
