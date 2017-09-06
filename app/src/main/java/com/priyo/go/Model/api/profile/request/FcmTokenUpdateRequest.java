package com.priyo.go.Model.api.profile.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 5/4/17.
 */

public class FcmTokenUpdateRequest implements Serializable {

    @SerializedName("fcm_token")
    private String fcmToken;

    public FcmTokenUpdateRequest() {
    }

    public FcmTokenUpdateRequest(String fcmToken) {

        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {

        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FcmTokenUpdateRequest that = (FcmTokenUpdateRequest) o;

        return fcmToken != null ? fcmToken.equals(that.fcmToken) : that.fcmToken == null;

    }

    @Override
    public int hashCode() {
        return fcmToken != null ? fcmToken.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FcmTokenUpdateRequest{" +
                "fcmToken='" + fcmToken + '\'' +
                '}';
    }
}
