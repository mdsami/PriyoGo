package com.priyo.go.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sajid.shahriar on 4/19/17.
 */

public class UserLocation {

    @SerializedName("lat")
    private double latitude;
    @SerializedName("long")
    private double longitude;

    public UserLocation() {
        this(0.0, 0.0);
    }

    public UserLocation(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {

        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLocation that = (UserLocation) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
