package com.priyo.go.Model.Friend;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BirthdayNode implements Comparable<BirthdayNode>, Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("photo")
    private String photo;
    @SerializedName("nodeID")
    private int nodeID;
    private String nodeLabel;
    @SerializedName("dob")
    private String dateOfBirth;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("isCelebrity")
    private boolean isCelebrity;

    public BirthdayNode() {
    }

    public BirthdayNode(String name, String photo, int nodeID, String nodeLabel, String dateOfBirth, String phoneNumber, boolean isCelebrity) {

        this.name = name;
        this.photo = photo;
        this.nodeID = nodeID;
        this.nodeLabel = nodeLabel;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.isCelebrity = isCelebrity;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isCelebrity() {
        return isCelebrity;
    }

    public void setCelebrity(boolean celebrity) {
        isCelebrity = celebrity;
    }

    @Override
    public int compareTo(@NonNull BirthdayNode o) {
        if (this.dateOfBirth.equals(o.dateOfBirth)) {
            if (this.name.equals(o.name)) {
                return this.nodeID < o.nodeID ? -1 : (this.nodeID == o.nodeID ? 0 : 1);
            } else {
                return this.name.compareTo(o.name);
            }
        } else {
            return this.dateOfBirth.compareTo(o.dateOfBirth);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BirthdayNode that = (BirthdayNode) o;

        if (nodeID != that.nodeID) return false;
        if (isCelebrity != that.isCelebrity) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (photo != null ? !photo.equals(that.photo) : that.photo != null) return false;
        if (nodeLabel != null ? !nodeLabel.equals(that.nodeLabel) : that.nodeLabel != null)
            return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(that.dateOfBirth) : that.dateOfBirth != null)
            return false;
        return phoneNumber != null ? phoneNumber.equals(that.phoneNumber) : that.phoneNumber == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + nodeID;
        result = 31 * result + (nodeLabel != null ? nodeLabel.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (isCelebrity ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BirthdayNode{" +
                "name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", nodeID=" + nodeID +
                ", nodeLabel='" + nodeLabel + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isCelebrity=" + isCelebrity +
                '}';
    }
}
