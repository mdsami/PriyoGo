package com.priyo.go.Model.graph;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 4/12/17.
 */

public class Property implements Serializable {

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


    public Property() {
        this(null, null, null, null, false);
    }

    public Property(String display, String key, String type, String value) {
        this(display, key, type, value, false);
    }

    public Property(String display, String key, String type, String value, boolean isDate) {

        this.display = display;
        this.key = key;
        this.type = type;
        this.value = value;
        this.isDate = isDate;
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

    public boolean isDate() {
        return isDate;
    }

    public void setDate(boolean date) {
        isDate = date;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        if (isDate != property.isDate) return false;
        if (display != null ? !display.equals(property.display) : property.display != null)
            return false;
        if (key != null ? !key.equals(property.key) : property.key != null) return false;
        if (type != null ? !type.equals(property.type) : property.type != null) return false;
        return value != null ? value.equals(property.value) : property.value == null;

    }

    @Override
    public int hashCode() {
        int result = display != null ? display.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (isDate ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Property{" +
                "display='" + display + '\'' +
                ", key='" + key + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", isDate=" + isDate +
                '}';
    }
}
