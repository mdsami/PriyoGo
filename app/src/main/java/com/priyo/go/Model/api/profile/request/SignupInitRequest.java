package com.priyo.go.Model.api.profile.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignupInitRequest implements Serializable {

    @SerializedName("mobile_number")
    private String mobileNumber;

    public SignupInitRequest() {
    }

    public SignupInitRequest(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {

        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignupInitRequest that = (SignupInitRequest) o;

        return mobileNumber != null ? mobileNumber.equals(that.mobileNumber) : that.mobileNumber == null;

    }

    @Override
    public int hashCode() {
        return mobileNumber != null ? mobileNumber.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SignupInitRequest{" +
                "mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
