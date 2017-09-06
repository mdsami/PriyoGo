package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InfoUpdateProfile implements Serializable{

    @SerializedName("display")
    private String display;
    @SerializedName("key")
    private String key;
    @SerializedName("type")
    private String type;
    @SerializedName("value")
    private String value;
    @SerializedName("isDate")
    private boolean isDate;

    public InfoUpdateProfile(String display, String key, String type, String value) {
        this.display = display;
        this.key = key;
        this.type = type;
        this.value = value;
    }

    public InfoUpdateProfile(String display, String key, String type, String value, boolean isDate) {
        this.display = display;
        this.key = key;
        this.type = type;
        this.value = value;
        this.isDate = isDate;
    }

    public String getDisplay() {
        return display;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean getIsDate() {
        return isDate;
    }
}
