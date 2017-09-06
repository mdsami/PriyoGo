package com.priyo.go.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HoroscopeNode implements Serializable {

    @SerializedName("keyName")
    private String keyName;
    @SerializedName("horoscopeName")
    private String horoscopeName;


    public HoroscopeNode(String keyName, String horoscopeName) {
        this.keyName = keyName;
        this.horoscopeName = horoscopeName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getHoroscopeName() {
        return horoscopeName;
    }

    public void setHoroscopeName(String horoscopeName) {
        this.horoscopeName = horoscopeName;
    }
}
