package com.priyo.go.Model.api.profile.response;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignupInitResponse implements Serializable {


    @SerializedName("tries_left")
    private int triesLeft;

    public SignupInitResponse() {
    }

    public SignupInitResponse(int triesLeft) {
        this.triesLeft = triesLeft;
    }

    public int getTriesLeft() {

        return triesLeft;
    }

    public void setTriesLeft(int triesLeft) {
        this.triesLeft = triesLeft;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignupInitResponse that = (SignupInitResponse) o;

        return triesLeft == that.triesLeft;

    }

    @Override
    public int hashCode() {
        return triesLeft;
    }

    @Override
    public String toString() {
        return "SignupInitResponse{" +
                "triesLeft=" + triesLeft +
                '}';
    }
}
