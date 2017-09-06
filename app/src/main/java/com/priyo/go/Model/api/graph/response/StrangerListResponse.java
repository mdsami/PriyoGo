package com.priyo.go.Model.api.graph.response;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.Model.Friend.StrangerNode;

import java.io.Serializable;
import java.util.List;

public class StrangerListResponse implements Serializable{

    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("strangers")
    private List<StrangerNode> strangers;

    public StrangerListResponse() {

    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<StrangerNode> getStrangers() {
        return strangers;
    }

    public void setStrangers(List<StrangerNode> strangers) {
        this.strangers = strangers;
    }
}
