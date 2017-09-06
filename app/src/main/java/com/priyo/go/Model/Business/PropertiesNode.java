package com.priyo.go.Model.Business;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PropertiesNode implements Serializable{

    @SerializedName("display")
    private String display;
    @SerializedName("key")
    private String key;
    @SerializedName("type")
    private String type;
    @SerializedName("value")
    private String value;

    public PropertiesNode() {

    }

    public PropertiesNode(String display, String key, String type, String value) {
        this.display = display;
        this.key = key;
        this.type = type;
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
