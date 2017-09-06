package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CommonFriendToStrangerInfo  implements Serializable{

    @SerializedName("name")
    private String name;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("profilePictureUrl")
    private String profilePictureUrl;

    public CommonFriendToStrangerInfo(String name, String phoneNumber, String profilePictureUrl) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePictureUrl = profilePictureUrl;
    }



    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }



    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

//    // TODO remove extra checking once medium quality profile picture becomes available in live
//    public String getProfilePictureUrlMedium() {
//        if (profilePictureUrlMedium != null && !profilePictureUrlMedium.isEmpty())
//            return profilePictureUrlMedium;
//        else
//            return getProfilePictureUrl();
//    }



    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Override
    public String toString() {
        return "FriendInfo{" +
                "name=" + name +
                ", phoneNumber=" + phoneNumber +
                ", profilePictureUrl=" + profilePictureUrl +
                "}";
    }
}
