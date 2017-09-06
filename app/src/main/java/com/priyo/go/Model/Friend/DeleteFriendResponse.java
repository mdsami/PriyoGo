package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

class DeleteFriendResponse implements Serializable{

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
