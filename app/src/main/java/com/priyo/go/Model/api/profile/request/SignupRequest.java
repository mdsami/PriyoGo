package com.priyo.go.Model.api.profile.request;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.Model.DeviceInformation;

import java.io.Serializable;

public class SignupRequest implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("mobile_number")
    private String mobileNumber;

    @SerializedName("otp")
    private String otp;

    @SerializedName("device_information")
    private DeviceInformation deviceInformation;

    public SignupRequest(String mobileNumber, String name, String otp, DeviceInformation deviceInformation) {
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.otp = otp;
        this.deviceInformation = deviceInformation;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getOtp() {
        return otp;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignupRequest that = (SignupRequest) o;

        if (!name.equals(that.name)) return false;
        if (!mobileNumber.equals(that.mobileNumber)) return false;
        if (!otp.equals(that.otp)) return false;
        return deviceInformation.equals(that.deviceInformation);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + mobileNumber.hashCode();
        result = 31 * result + otp.hashCode();
        result = 31 * result + deviceInformation.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", otp='" + otp + '\'' +
                ", deviceInformation=" + deviceInformation +
                '}';
    }
}
