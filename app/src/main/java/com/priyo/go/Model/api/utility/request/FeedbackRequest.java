package com.priyo.go.Model.api.utility.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class FeedbackRequest implements Serializable {

    @SerializedName("mobileNumber")
    private String mobileNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("feedback")
    private String feedback;
    @SerializedName("createdAt")
    private Date createdAt;

    public FeedbackRequest() {
    }

    public FeedbackRequest(String mobileNumber, String name, String feedback, Date createdAt) {

        this.mobileNumber = mobileNumber;
        this.name = name;
        this.feedback = feedback;
        this.createdAt = createdAt;
    }

    public String getMobileNumber() {

        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
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

        FeedbackRequest that = (FeedbackRequest) o;

        if (mobileNumber != null ? !mobileNumber.equals(that.mobileNumber) : that.mobileNumber != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (feedback != null ? !feedback.equals(that.feedback) : that.feedback != null)
            return false;
        return createdAt != null ? createdAt.equals(that.createdAt) : that.createdAt == null;

    }

    @Override
    public int hashCode() {
        int result = mobileNumber != null ? mobileNumber.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (feedback != null ? feedback.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FeedbackRequest{" +
                "mobileNumber='" + mobileNumber + '\'' +
                ", name='" + name + '\'' +
                ", feedback='" + feedback + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
