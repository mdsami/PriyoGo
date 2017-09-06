package com.priyo.go.Model.api.location.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 5/8/17.
 */

public class LocationSearchableUpdateRequest implements Serializable {
    @SerializedName("is_searchable")
    private boolean isSearchable;

    public LocationSearchableUpdateRequest() {
    }

    public LocationSearchableUpdateRequest(boolean isSearchable) {

        this.isSearchable = isSearchable;
    }

    public boolean isSearchable() {

        return isSearchable;
    }

    public void setSearchable(boolean searchable) {
        isSearchable = searchable;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationSearchableUpdateRequest that = (LocationSearchableUpdateRequest) o;

        return isSearchable == that.isSearchable;

    }

    @Override
    public int hashCode() {
        return (isSearchable ? 1 : 0);
    }

    @Override
    public String toString() {
        return "LocationSearchableUpdateRequest{" +
                "isSearchable=" + isSearchable +
                '}';
    }
}
