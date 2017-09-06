package com.priyo.go.Model.api.graph.response;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.Model.node.Business;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sajid.shahriar on 5/4/17.
 */

public class BusinessListResponse implements Serializable {
    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("data")
    private List<Business> data;

    public BusinessListResponse() {
    }

    public BusinessListResponse(int totalCount, List<Business> data) {

        this.totalCount = totalCount;
        this.data = data;
    }

    public int getTotalCount() {

        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Business> getData() {
        return data;
    }

    public void setData(List<Business> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessListResponse that = (BusinessListResponse) o;

        if (totalCount != that.totalCount) return false;
        return data != null ? data.equals(that.data) : that.data == null;

    }

    @Override
    public int hashCode() {
        int result = totalCount;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BusinessListResponse{" +
                "totalCount=" + totalCount +
                ", data=" + data +
                '}';
    }
}
