package com.priyo.go.Model.api.utility.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sajid.shahriar on 5/2/17.
 */

public class WishAddRequest implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("title")
    private String title;
    @SerializedName("wish")
    private String wish;
    @SerializedName("mobileNumber")
    private String mobileNumber;
    @SerializedName("createdAt")
    private Date createdAt;

    public WishAddRequest() {
    }

    public WishAddRequest(String name, String title, String wish, String mobileNumber, Date createdAt) {

        this.name = name;
        this.title = title;
        this.wish = wish;
        this.mobileNumber = mobileNumber;
        this.createdAt = createdAt;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WishAddRequest that = (WishAddRequest) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (wish != null ? !wish.equals(that.wish) : that.wish != null) return false;
        if (mobileNumber != null ? !mobileNumber.equals(that.mobileNumber) : that.mobileNumber != null)
            return false;
        return createdAt != null ? createdAt.equals(that.createdAt) : that.createdAt == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (wish != null ? wish.hashCode() : 0);
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WishAddRequest{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", wish='" + wish + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
