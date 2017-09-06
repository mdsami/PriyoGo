package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InfoAddFriend implements Serializable {

    @SerializedName("mobileNumber")
    private String mobileNumber;
    @SerializedName("name")
    private String name;

    public InfoAddFriend() {

    }

    public InfoAddFriend(String mobileNumber, String name) {
        this.mobileNumber = mobileNumber;
        this.name = name;
    }



    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getName() {
        return name;
    }
}

