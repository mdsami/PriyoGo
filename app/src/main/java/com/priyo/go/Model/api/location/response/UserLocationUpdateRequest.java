package com.priyo.go.Model.api.location.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserLocationUpdateRequest implements Serializable {

    @SerializedName("mobileNumber")
    private String mobileNumber;
    @SerializedName("deviceID")
    private String deviceID;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public UserLocationUpdateRequest() {
    }

    public UserLocationUpdateRequest(String mobileNumber, String deviceID, String latitude, String longitude) {

        this.mobileNumber = mobileNumber;
        this.deviceID = deviceID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getMobileNumber() {

        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLocationUpdateRequest that = (UserLocationUpdateRequest) o;

        if (mobileNumber != null ? !mobileNumber.equals(that.mobileNumber) : that.mobileNumber != null)
            return false;
        if (deviceID != null ? !deviceID.equals(that.deviceID) : that.deviceID != null)
            return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null)
            return false;
        return longitude != null ? longitude.equals(that.longitude) : that.longitude == null;

    }

    @Override
    public int hashCode() {
        int result = mobileNumber != null ? mobileNumber.hashCode() : 0;
        result = 31 * result + (deviceID != null ? deviceID.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserLocationUpdateRequest{" +
                "mobileNumber='" + mobileNumber + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
