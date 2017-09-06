package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FriendNode implements Comparable<FriendNode>,Serializable {

    @SerializedName("contactName")
    private String contactName;
    @SerializedName("phoneNumber")
    private String mobileNumber;
    @SerializedName("isPeople")
    private boolean isPeople;
    @SerializedName("peopleName")
    private String peopleName;

    public FriendNode() {

    }

    public FriendNode(String contactName, String mobileNumber) {
        this.contactName = contactName;
        this.mobileNumber = mobileNumber;
    }

    public FriendNode(String contactName, String mobileNumber, boolean isPeople, String peopleName) {
        this.contactName = contactName;
        this.mobileNumber = mobileNumber;
        this.isPeople = isPeople;
        this.peopleName = peopleName;
    }

    public String getContactName() {
        return contactName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public boolean isPeople() {
        return isPeople;
    }

    public String getPeopleName() {
        return peopleName;
    }

    @Override
    public String toString() {
        return "FriendNode{" +
                "contactName='" + contactName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", isPeople=" + isPeople +
                ", peopleName='" + peopleName + '\'' +
                '}';
    }

    @Override
    public int compareTo(FriendNode another) {
        return mobileNumber.compareTo(another.mobileNumber);
    }
}
