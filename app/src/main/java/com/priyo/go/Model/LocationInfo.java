package com.priyo.go.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by sajid.shahriar on 4/26/17.
 */

public class LocationInfo implements Serializable, Parcelable {
    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel in) {
            return new LocationInfo(in);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };
    @SerializedName("type")
    private String type;
    @SerializedName("coordinates")
    private double[] coordinates;

    public LocationInfo() {
    }

    public LocationInfo(String type, double[] coordinates) {

        this.type = type;
        this.coordinates = coordinates;
    }

    protected LocationInfo(Parcel in) {
        type = in.readString();
        coordinates = in.createDoubleArray();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationInfo that = (LocationInfo) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return Arrays.equals(coordinates, that.coordinates);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(coordinates);
        return result;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "type='" + type + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeDoubleArray(coordinates);
    }
}
