package com.priyo.go.Model.api.profile.response;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.Model.ProfileInfo;

import java.io.Serializable;

public class SignupResponse implements Serializable {

    @SerializedName("token")
    private String token;
    @SerializedName("device_token")
    private String deviceToken;

    @SerializedName("profile")
    private ProfileInfo profileInfo;

    public SignupResponse() {
        this(null, null, null);
    }

    public SignupResponse(String token, String deviceToken, ProfileInfo profileInfo) {

        this.token = token;
        this.deviceToken = deviceToken;
        this.profileInfo = profileInfo;
    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignupResponse that = (SignupResponse) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (deviceToken != null ? !deviceToken.equals(that.deviceToken) : that.deviceToken != null)
            return false;
        return profileInfo != null ? profileInfo.equals(that.profileInfo) : that.profileInfo == null;

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (deviceToken != null ? deviceToken.hashCode() : 0);
        result = 31 * result + (profileInfo != null ? profileInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignupResponse{" +
                "token='" + token + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", profileInfo=" + profileInfo +
                '}';
    }
}
