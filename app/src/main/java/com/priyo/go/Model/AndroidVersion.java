package com.priyo.go.Model;

/**
 * Created by MUKUL on 10/25/16.
 */

public class AndroidVersion {

    private String android_version_name;
    private String android_image_url;

    public AndroidVersion() {
    }

    public AndroidVersion(String android_version_name, String android_image_url) {
        this.android_version_name = android_version_name;
        this.android_image_url = android_image_url;
    }

    public String getAndroid_version_name() {
        return android_version_name;
    }

    public void setAndroid_version_name(String android_version_name) {
        this.android_version_name = android_version_name;
    }

    public String getAndroid_image_url() {
        return android_image_url;
    }

    public void setAndroid_image_url(String android_image_url) {
        this.android_image_url = android_image_url;
    }
}