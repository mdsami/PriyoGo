package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ThanaRequestResponse implements Serializable{

    @SerializedName("totalCount")
    private String totalCount;
    @SerializedName("data")
    private List<Thana> data;

    public ThanaRequestResponse() {

    }

    public String getTotalCount() {
        return totalCount;
    }

    public List<Thana> getData() {
        return data;
    }
}
