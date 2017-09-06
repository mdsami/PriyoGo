package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StrangerResponse implements Serializable{

    @SerializedName("message")
    private String message;
    @SerializedName("otp")
    private String otp;

    public StrangerResponse() {

    }

    public String getOtp() {
        return otp;
    }

    public String getMessage() {
        return message;
    }
}
