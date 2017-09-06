package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InfoUpdateFriend implements Serializable{

    @SerializedName("friendsNumber")
    private final String friendsNumber;
    @SerializedName("friendsName")
    private final String friendsName;

    public InfoUpdateFriend(String friendsNumber, String friendsName) {


        this.friendsNumber = friendsNumber;
        this.friendsName = friendsName;
    }
}
