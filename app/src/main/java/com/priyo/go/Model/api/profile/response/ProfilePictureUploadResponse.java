package com.priyo.go.Model.api.profile.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 4/23/17.
 */

public class ProfilePictureUploadResponse implements Serializable {

    @SerializedName("url")
    private String url;


    public ProfilePictureUploadResponse() {
    }

    public ProfilePictureUploadResponse(String url) {

        this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfilePictureUploadResponse that = (ProfilePictureUploadResponse) o;

        return url != null ? url.equals(that.url) : that.url == null;

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProfilePictureUploadResponse{" +
                "url='" + url + '\'' +
                '}';
    }
}
