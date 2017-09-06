package com.priyo.go.Model.Business;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BusinessCategoryResponse implements Serializable{

    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("data")
    private List<CategoryNode> data;

    public BusinessCategoryResponse() {

    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<CategoryNode> getData() {
        return data;
    }
}
