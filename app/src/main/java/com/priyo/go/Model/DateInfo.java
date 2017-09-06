package com.priyo.go.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 4/19/17.
 */

public class DateInfo implements Serializable, Parcelable {
    public static final Creator<DateInfo> CREATOR = new Creator<DateInfo>() {
        @Override
        public DateInfo createFromParcel(Parcel in) {
            return new DateInfo(in);
        }

        @Override
        public DateInfo[] newArray(int size) {
            return new DateInfo[size];
        }
    };
    @SerializedName("$date")
    private long date;

    public DateInfo() {
    }

    public DateInfo(long date) {

        this.date = date;
    }

    protected DateInfo(Parcel in) {
        date = in.readLong();
    }

    public long getDate() {

        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateInfo dateInfo = (DateInfo) o;

        return date == dateInfo.date;

    }

    @Override
    public int hashCode() {
        return (int) (date ^ (date >>> 32));
    }

    @Override
    public String toString() {
        return "DateInfo{" +
                "date=" + date +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date);
    }
}
