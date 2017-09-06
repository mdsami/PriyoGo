package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.DatabaseHelper.DBConstants;

import java.io.Serializable;

public class FriendInfo implements Serializable{

    @SerializedName("accountType")
    private int accountType;
    @SerializedName("isMember")
    private boolean isMember;
    @SerializedName("isVerified")
    private boolean isVerified;
    @SerializedName("name")
    private String name;
    @SerializedName("originalName")
    private String originalName;
    @SerializedName("profilePictureUrl")
    private String profilePictureUrl;
    @SerializedName("profilePictureUrlMedium")
    private String profilePictureUrlMedium;
    @SerializedName("profilePictureUrlHigh")
    private String profilePictureUrlHigh;
    @SerializedName("relationship")
    private String relationship;
    @SerializedName("updateAt")
    private long updateAt;

    public FriendInfo(String name, String profilePictureUrl) {
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
    }

    public FriendInfo(int accountType, boolean isMember, boolean isVerified, String name, String originalName,
                      String profilePictureUrl, String profilePictureUrlMedium, String profilePictureUrlHigh, String relationship, long updateAt) {
        this.accountType = accountType;
        this.isMember = isMember;
        this.isVerified = isVerified;
        this.name = name;
        this.originalName = originalName;
        this.profilePictureUrl = profilePictureUrl;
        this.profilePictureUrlMedium = profilePictureUrlMedium;
        this.profilePictureUrlHigh = profilePictureUrlHigh;
        this.relationship = relationship;
        this.updateAt = updateAt;
    }

    public FriendInfo(int accountType, int isMember, int verificationStatus, String name, String originalName,
                      String profilePictureUrl, String profilePictureUrlMedium, String profilePictureUrlHigh, String relationship, long updateAt) {
        this(accountType, isMember == DBConstants.IPAY_MEMBER, verificationStatus == DBConstants.VERIFIED_USER,
                name, originalName, profilePictureUrl, profilePictureUrlMedium, profilePictureUrlHigh, relationship, updateAt);
    }

    public int getAccountType() {
        return accountType;
    }

    public boolean isMember() {
        return isMember;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public long getUpdateTime() {
        return updateAt;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    // TODO remove extra checking once medium quality profile picture becomes available in live
    public String getProfilePictureUrlMedium() {
        if (profilePictureUrlMedium != null && !profilePictureUrlMedium.isEmpty())
            return profilePictureUrlMedium;
        else
            return getProfilePictureUrl();
    }

    // TODO remove extra checking once high quality profile picture becomes available in live
    public String getProfilePictureUrlHigh() {
        if (profilePictureUrlHigh != null && !profilePictureUrlHigh.isEmpty())
            return profilePictureUrlHigh;
        else
            return getProfilePictureUrlMedium();
    }

    public String getRelationship() {
        return relationship;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Override
    public String toString() {
        return "FriendInfo{" +
                "accountType=" + accountType +
                ", isMember=" + isMember +
                ", isVerified=" + isVerified +
                ", name='" + name + '\'' +
                ", originalName='" + originalName + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                '}';
    }
}
