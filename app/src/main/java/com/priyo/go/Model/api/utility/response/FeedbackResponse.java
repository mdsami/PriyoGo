package com.priyo.go.Model.api.utility.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeedbackResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    public FeedbackResponse() {
    }

    public FeedbackResponse(boolean success) {

        this.success = success;
    }

    public boolean isSuccess() {

        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedbackResponse that = (FeedbackResponse) o;

        return success == that.success;

    }

    @Override
    public int hashCode() {
        return (success ? 1 : 0);
    }

    @Override
    public String toString() {
        return "FeedbackResponse{" +
                "success=" + success +
                '}';
    }
}
