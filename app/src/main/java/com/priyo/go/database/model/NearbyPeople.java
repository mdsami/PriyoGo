package com.priyo.go.database.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

@Entity
public class NearbyPeople implements Comparable<NearbyPeople>, Serializable {


    private static final long serialVersionUID = -2501187876432763584L;
    @Id
    private Long id;
    @Unique
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

    @Generated(hash = 1790161758)
    public NearbyPeople() {
    }

    public NearbyPeople(Long id) {
        this.id = id;
    }

    @Generated(hash = 2104511786)
    public NearbyPeople(Long id, Long nodeID, String name, String tag, String photo, String phoneNumber) {
        this.id = id;
        this.nodeID = nodeID;
        this.name = name;
        this.tag = tag;
        this.photo = photo;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        NearbyPeople nearbyPeople = (NearbyPeople) o;

        if (id != null ? !id.equals(nearbyPeople.id) : nearbyPeople.id != null) return false;
        if (nodeID != null ? !nodeID.equals(nearbyPeople.nodeID) : nearbyPeople.nodeID != null)
            return false;
        if (name != null ? !name.equals(nearbyPeople.name) : nearbyPeople.name != null)
            return false;
        if (tag != null ? !tag.equals(nearbyPeople.tag) : nearbyPeople.tag != null) return false;
        if (photo != null ? !photo.equals(nearbyPeople.photo) : nearbyPeople.photo != null)
            return false;
        return phoneNumber != null ? phoneNumber.equals(nearbyPeople.phoneNumber) : nearbyPeople.phoneNumber == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nodeID != null ? nodeID.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(@NonNull NearbyPeople o) {
        return this.nodeID.compareTo(o.nodeID);

    }

    @Override
    public String toString() {
        return "NearbyPeople{" +
                "id=" + id +
                ", nodeID=" + nodeID +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", photo='" + photo + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
