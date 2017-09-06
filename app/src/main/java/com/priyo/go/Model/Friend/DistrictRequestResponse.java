package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DistrictRequestResponse implements Serializable{

    @SerializedName("totalCount")
    private String totalCount;
    @SerializedName("data")
    private List<District> data;

    public DistrictRequestResponse() {

    }

    public String getTotalCount() {
        return totalCount;
    }

    public List<District> getData() {
        return data;
    }
}
