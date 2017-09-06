package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StrangerRequest implements Serializable {

    @SerializedName("mobileNumber")
    private final String mobileNumber;

    public StrangerRequest(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
