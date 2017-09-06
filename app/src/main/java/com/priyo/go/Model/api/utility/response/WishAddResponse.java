package com.priyo.go.Model.api.utility.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 5/2/17.
 */

public class WishAddResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    public WishAddResponse() {
    }

    public WishAddResponse(boolean success) {

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

        WishAddResponse that = (WishAddResponse) o;

        return success == that.success;

    }

    @Override
    public int hashCode() {
        return (success ? 1 : 0);
    }

    @Override
    public String toString() {
        return "WishAddResponse{" +
                "success=" + success +
                '}';
    }
}
