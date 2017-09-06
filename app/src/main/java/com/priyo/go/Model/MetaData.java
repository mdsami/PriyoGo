package com.priyo.go.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sajid.shahriar on 4/19/17.
 */

public class MetaData {

    @SerializedName("version")
    private int version;
    @SerializedName("location")
    private UserLocation location;

    public MetaData() {
    }

    public MetaData(int version, UserLocation location) {

        this.version = version;
        this.location = location;
    }

    public int getVersion() {

        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetaData metaData = (MetaData) o;

        if (version != metaData.version) return false;
        return location != null ? location.equals(metaData.location) : metaData.location == null;

    }

    @Override
    public int hashCode() {
        int result = version;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "version=" + version +
                ", location=" + location +
                '}';
    }
}
