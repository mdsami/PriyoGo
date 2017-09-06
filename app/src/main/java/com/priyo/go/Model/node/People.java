package com.priyo.go.Model.node;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class People implements Comparable<People>, Serializable {


    private static final long serialVersionUID = -1476437466740448842L;

    @SerializedName("nodeID")
    private Long nodeID;
    @SerializedName("name")
    private String name;
    @SerializedName("tag")
    private String tag;
    @SerializedName("photo")
    private String photo;
    @SerializedName("phoneNumber")
    private String phoneNumber;

    public People() {
    }

    public People(Long nodeID, String name, String tag, String photo, String phoneNumber) {

        this.nodeID = nodeID;
        this.name = name;
        this.tag = tag;
        this.photo = photo;
        this.phoneNumber = phoneNumber;
    }

    public Long getNodeID() {

        return nodeID;
    }

    public void setNodeID(Long nodeID) {
        this.nodeID = nodeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        People people = (People) o;

        if (nodeID != null ? !nodeID.equals(people.nodeID) : people.nodeID != null) return false;
        if (name != null ? !name.equals(people.name) : people.name != null) return false;
        if (tag != null ? !tag.equals(people.tag) : people.tag != null) return false;
        if (photo != null ? !photo.equals(people.photo) : people.photo != null) return false;
        return phoneNumber != null ? phoneNumber.equals(people.phoneNumber) : people.phoneNumber == null;

    }

    @Override
    public int hashCode() {
        int result = nodeID != null ? nodeID.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(@NonNull People o) {
        return this.nodeID.compareTo(o.nodeID);

    }

    @Override
    public String toString() {
        return "People{" +
                "nodeID=" + nodeID +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", photo='" + photo + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
