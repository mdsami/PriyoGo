package com.priyo.go.Model.api.graph.response;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.Model.node.People;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sajid.shahriar on 5/4/17.
 */

public class PeopleListResponse implements Serializable {
    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("data")
    private List<People> data;

    public PeopleListResponse() {
    }

    public PeopleListResponse(int totalCount, List<People> data) {

        this.totalCount = totalCount;
        this.data = data;
    }

    public int getTotalCount() {

        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<People> getData() {
        return data;
    }

    public void setData(List<People> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PeopleListResponse that = (PeopleListResponse) o;

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
        return "PeopleListResponse{" +
                "totalCount=" + totalCount +
                ", data=" + data +
                '}';
    }
}
