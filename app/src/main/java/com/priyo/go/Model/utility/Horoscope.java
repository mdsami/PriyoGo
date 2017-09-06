package com.priyo.go.Model.utility;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 4/30/17.
 */

public class Horoscope implements Serializable {
    private String sign;
    private String horoscope;

    public Horoscope() {
    }

    public Horoscope(String sign, String horoscope) {

        this.sign = sign;
        this.horoscope = horoscope;
    }

    public String getSign() {

        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Horoscope horoscope = (Horoscope) o;

        if (sign != null ? !sign.equals(horoscope.sign) : horoscope.sign != null) return false;
        return this.horoscope != null ? this.horoscope.equals(horoscope.horoscope) : horoscope.horoscope == null;

    }

    @Override
    public int hashCode() {
        int result = sign != null ? sign.hashCode() : 0;
        result = 31 * result + (horoscope != null ? horoscope.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Horoscope{" +
                "sign='" + sign + '\'' +
                ", horoscope='" + horoscope + '\'' +
                '}';
    }
}
