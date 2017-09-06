package com.priyo.go.Api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 4/25/17.
 */

public class ApiForbiddenResponse implements Serializable {
    @SerializedName("detail")
    private String detail;
    @SerializedName("message")
    private String message;

    public ApiForbiddenResponse() {
    }

    public String getDetail() {

        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiForbiddenResponse that = (ApiForbiddenResponse) o;

        if (detail != null ? !detail.equals(that.detail) : that.detail != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = detail != null ? detail.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ApiForbiddenResponse{" +
                "detail='" + detail + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
