package com.priyo.go.Model.Address;

import java.io.Serializable;
import java.util.List;


public class AddressClass implements Serializable {
    private String name;
    private String dob;
    private String gender;
    private String aboutMe;
    private int profession;
    private int district;
    private int thana;
    private String org;

    public AddressClass() {

    }

    public AddressClass(String addressLine1, String addressLine2,
                        int district, int thana) {
        this.name = addressLine1;
        this.dob = addressLine2;
        this.district = district;
        this.thana = thana;
    }

    public AddressClass(String name, String dob, String gender, String aboutMe, int profession, int district, int thana, String org) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.aboutMe = aboutMe;
        this.profession = profession;
        this.district = district;
        this.thana = thana;
        this.org = org;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public int getProfession() {
        return profession;
    }

    public int getDistrict() {
        return district;
    }

    public int getThana() {
        return thana;
    }

    public String getOrg() {
        return org;
    }

    public int getDistrictCode() {
        return district;
    }

    public int getThanaCode() {
        return thana;
    }

    private String getThana(List<Thana> thanaList) {
        if (thanaList != null) {
            for (Thana thana : thanaList) {
                if (thana.getId() == this.thana)
                    return thana.getName();

            }
        }

        return null;
    }

    private String getDistrict(List<District> districtList) {
        if (districtList != null) {
            for (District district : districtList) {
                if (district.getId() == this.district)
                    return district.getName();
            }
        }

        return null;
    }


    public String toString(List<Thana> thanaList, List<District> districtList) {
        StringBuilder builder = new StringBuilder();

        String thanaName = getThana(thanaList);
        if (thanaName != null)
            builder.append(thanaName.trim()).append(", ");

        String districtName = getDistrict(districtList);
        if (districtName != null)
            builder.append(districtName.trim()).append(" ");

        builder.append("\n");
        return builder.toString();
    }
}
