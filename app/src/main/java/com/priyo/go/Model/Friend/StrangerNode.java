package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StrangerNode implements Comparable<StrangerNode> ,Serializable{

    @SerializedName("name")
    private String name;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("profilePictureUrl")
    private String profilePictureUrl;
    @SerializedName("commonFriendNo")
    private String commonFriendNo;
    @SerializedName("nodeId")
    private String nodeId;

    public StrangerNode() {

    }

    public StrangerNode(String name, String phoneNumber, String profilePictureUrl, String commonFriendNo, String nodeId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profilePictureUrl = profilePictureUrl;
        this.commonFriendNo = commonFriendNo;
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getCommonFriendNo() {
        return commonFriendNo;
    }

    public void setCommonFriendNo(String commonFriendNo) {
        this.commonFriendNo = commonFriendNo;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String toString() {
        return "FriendNode{" +
                "name=" + name +
                ", phoneNumber='" + phoneNumber  +
                ", photo=" + profilePictureUrl +
                ", info=" + commonFriendNo +
                '}';
    }

    @Override
    public int compareTo(StrangerNode another) {
        return phoneNumber.compareTo(another.phoneNumber);
    }


}
