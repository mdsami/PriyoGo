package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AddFriendRequest implements Serializable {

    @SerializedName("contacts")
    private final List<InfoAddFriend> contacts;

    public AddFriendRequest(List<InfoAddFriend> contacts) {
        this.contacts = contacts;
    }

    public List<InfoAddFriend> getContacts() {
        return contacts;
    }
}
