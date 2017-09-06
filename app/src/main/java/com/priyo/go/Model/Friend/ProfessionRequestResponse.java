package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProfessionRequestResponse implements Serializable{

    @SerializedName("totalCount")
    private String totalCount;
    @SerializedName("data")
    private List<Profession> data;

    public ProfessionRequestResponse() {

    }

    public String getTotalCount() {
        return totalCount;
    }

    public List<Profession> getData() {
        return data;
    }
}
