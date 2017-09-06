package com.priyo.go.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class WishDetail implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("mobileNumber")
    private String mobileNumber;

    @SerializedName("title")
    private String title;
    @SerializedName("wish")
    private String wish;

    @SerializedName("isPublic")
    private int isPublic;
    @SerializedName("isCompleted")
    private int isCompleted;

    @SerializedName("completedMobileNumber")
    private String completedMobileNumber;

    @SerializedName("createdAt")
    private Date createdAt;
    @SerializedName("completedAt")
    private Date completedAt;


    @SerializedName("imageURL")
    private String imageUrl;
    @SerializedName("imageCaption")
    private String imageCaption;
    @SerializedName("internalNote")
    private String internalNote;

    public WishDetail() {
    }

    public WishDetail(String name, String mobileNumber, String title, String wish, int isPublic, int isCompleted, String completedMobileNumber, Date createdAt, Date completedAt, String imageUrl, String imageCaption, String internalNote) {

        this.name = name;
        this.mobileNumber = mobileNumber;
        this.title = title;
        this.wish = wish;
        this.isPublic = isPublic;
        this.isCompleted = isCompleted;
        this.completedMobileNumber = completedMobileNumber;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.imageUrl = imageUrl;
        this.imageCaption = imageCaption;
        this.internalNote = internalNote;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getCompletedMobileNumber() {
        return completedMobileNumber;
    }

    public void setCompletedMobileNumber(String completedMobileNumber) {
        this.completedMobileNumber = completedMobileNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageCaption() {
        return imageCaption;
    }

    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }

    public String getInternalNote() {
        return internalNote;
    }

    public void setInternalNote(String internalNote) {
        this.internalNote = internalNote;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WishDetail wishDetail = (WishDetail) o;

        if (isPublic != wishDetail.isPublic) return false;
        if (isCompleted != wishDetail.isCompleted) return false;
        if (name != null ? !name.equals(wishDetail.name) : wishDetail.name != null) return false;
        if (mobileNumber != null ? !mobileNumber.equals(wishDetail.mobileNumber) : wishDetail.mobileNumber != null)
            return false;
        if (title != null ? !title.equals(wishDetail.title) : wishDetail.title != null)
            return false;
        if (wish != null ? !wish.equals(wishDetail.wish) : wishDetail.wish != null) return false;
        if (completedMobileNumber != null ? !completedMobileNumber.equals(wishDetail.completedMobileNumber) : wishDetail.completedMobileNumber != null)
            return false;
        if (createdAt != null ? !createdAt.equals(wishDetail.createdAt) : wishDetail.createdAt != null)
            return false;
        if (completedAt != null ? !completedAt.equals(wishDetail.completedAt) : wishDetail.completedAt != null)
            return false;
        if (imageUrl != null ? !imageUrl.equals(wishDetail.imageUrl) : wishDetail.imageUrl != null)
            return false;
        if (imageCaption != null ? !imageCaption.equals(wishDetail.imageCaption) : wishDetail.imageCaption != null)
            return false;
        return internalNote != null ? internalNote.equals(wishDetail.internalNote) : wishDetail.internalNote == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (wish != null ? wish.hashCode() : 0);
        result = 31 * result + isPublic;
        result = 31 * result + isCompleted;
        result = 31 * result + (completedMobileNumber != null ? completedMobileNumber.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (completedAt != null ? completedAt.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (imageCaption != null ? imageCaption.hashCode() : 0);
        result = 31 * result + (internalNote != null ? internalNote.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WishDetail{" +
                "name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", title='" + title + '\'' +
                ", wish='" + wish + '\'' +
                ", isPublic=" + isPublic +
                ", isCompleted=" + isCompleted +
                ", completedMobileNumber='" + completedMobileNumber + '\'' +
                ", createdAt=" + createdAt +
                ", completedAt=" + completedAt +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageCaption='" + imageCaption + '\'' +
                ", internalNote='" + internalNote + '\'' +
                '}';
    }
}
