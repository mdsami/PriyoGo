package com.priyo.go.Model.api.graph.response;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.Model.Business.CategoryNode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sajid.shahriar on 5/3/17.
 */

public class ProfessionListResponse implements Serializable {

    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("data")
    private List<CategoryNode> data;

    public ProfessionListResponse() {

    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<CategoryNode> getData() {
        return data;
    }
}
