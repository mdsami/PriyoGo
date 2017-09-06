package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

class DeleteFriendRequest implements Serializable {

    @SerializedName("friendsNumber")
    private final String friendsNumber;

    public DeleteFriendRequest(String friendsNumber) {
        this.friendsNumber = friendsNumber;
    }
}
