package com.priyo.go.Model;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 4/18/17.
 */

public class DeviceInformation implements Serializable {
    private final String android_id;
    private final String device_manufacturer;
    private final String device_id;
    private final String device_name;
    private final String iid;
    private final String guid;

    public DeviceInformation(String android_id, String device_manufacturer, String device_id, String device_name, String iid, String guid) {
        this.android_id = android_id;
        this.device_manufacturer = device_manufacturer;
        this.device_id = device_id;
        this.device_name = device_name;
        this.iid = iid;
        this.guid = guid;
    }

    public String getAndroid_id() {

        return android_id;
    }

    public String getDevice_manufacturer() {
        return device_manufacturer;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public String getIid() {
        return iid;
    }

    public String getGuid() {
        return guid;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceInformation that = (DeviceInformation) o;

        if (!android_id.equals(that.android_id)) return false;
        if (!device_manufacturer.equals(that.device_manufacturer)) return false;
        if (!device_id.equals(that.device_id)) return false;
        if (!device_name.equals(that.device_name)) return false;
        if (!iid.equals(that.iid)) return false;
        return guid.equals(that.guid);

    }

    @Override
    public int hashCode() {
        int result = android_id.hashCode();
        result = 31 * result + device_manufacturer.hashCode();
        result = 31 * result + device_id.hashCode();
        result = 31 * result + device_name.hashCode();
        result = 31 * result + iid.hashCode();
        result = 31 * result + guid.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DeviceInformation{" +
                "android_id='" + android_id + '\'' +
                ", device_manufacturer='" + device_manufacturer + '\'' +
                ", device_id='" + device_id + '\'' +
                ", device_name='" + device_name + '\'' +
                ", iid='" + iid + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}
