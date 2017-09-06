package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhoneName implements Serializable{

    public PhoneName(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public PhoneName() {

    }


    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("number")
    public String number;
    @SerializedName("type")
    public String type;
    @SerializedName("starred")
    public String starred;
    @SerializedName("state")
    public int state;
}
