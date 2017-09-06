package com.priyo.go.Model.Business;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.Model.node.People;

import java.io.Serializable;
import java.util.List;

public class GraphNodeListResponse implements Serializable{

    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("data")
    private List<People> data;

    public GraphNodeListResponse() {

    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<People> getData() {
        return data;
    }
}
