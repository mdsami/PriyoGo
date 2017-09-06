package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UpdateFriendRequest implements Serializable {

    @SerializedName("updateFriends")
    private final List<InfoUpdateFriend> updateFriends;

    public UpdateFriendRequest(List<InfoUpdateFriend> updateFriends) {
        this.updateFriends = updateFriends;
    }

    public List<InfoUpdateFriend> getUpdateFriends() {
        return updateFriends;
    }
}
