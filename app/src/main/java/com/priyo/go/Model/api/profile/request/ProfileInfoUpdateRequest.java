package com.priyo.go.Model.api.profile.request;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.Model.Friend.District;
import com.priyo.go.Model.Friend.Profession;
import com.priyo.go.Model.Friend.Thana;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sajid.shahriar on 4/19/17.
 */

public class ProfileInfoUpdateRequest implements Serializable {


    @SerializedName("name")
    private String name;
    @SerializedName("formal_name")
    private String formalName;
    @SerializedName("bengali_name")
    private String bengaliName;
    @SerializedName("email")
    private String email;
    @SerializedName("gender")
    private String gender;
    @SerializedName("dob_date")
    private String dateOfBirthDate;
    @SerializedName("dob_display")
    private String dateOfBirthDisplay;
    @SerializedName("organization_name")
    private String organizationName;
    @SerializedName("description")
    private String description;
    @SerializedName("profession")
    private Profession profession;
    @SerializedName("district")
    private District district;
    @SerializedName("thana")
    private Thana thana;
    @SerializedName("public_access_list")
    private Set<String> publicAccessList = new HashSet<>();

    public ProfileInfoUpdateRequest() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public ProfileInfoUpdateRequest(String name, String formalName, String bengaliName, String email, String gender, String dateOfBirthDate, String dateOfBirthDisplay, String organizationName, String description, Profession profession, District district, Thana thana, Set<String> publicAccessList) {

        this.name = name;
        this.formalName = formalName;
        this.bengaliName = bengaliName;
        this.email = email;
        this.gender = gender;
        this.dateOfBirthDate = dateOfBirthDate;
        this.dateOfBirthDisplay = dateOfBirthDisplay;
        this.organizationName = organizationName;
        this.description = description;
        this.profession = profession;
        this.district = district;
        this.thana = thana;
        this.publicAccessList = publicAccessList;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormalName() {
        return formalName;
    }

    public void setFormalName(String formalName) {
        this.formalName = formalName;
    }

    public String getBengaliName() {
        return bengaliName;
    }

    public void setBengaliName(String bengaliName) {
        this.bengaliName = bengaliName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirthDate() {
        return dateOfBirthDate;
    }

    public void setDateOfBirthDate(String dateOfBirthDate) {
        this.dateOfBirthDate = dateOfBirthDate;
    }

    public String getDateOfBirthDisplay() {
        return dateOfBirthDisplay;
    }

    public void setDateOfBirthDisplay(String dateOfBirthDisplay) {
        this.dateOfBirthDisplay = dateOfBirthDisplay;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Thana getThana() {
        return thana;
    }

    public void setThana(Thana thana) {
        this.thana = thana;
    }

    public Set<String> getPublicAccessList() {
        return publicAccessList;
    }

    public void setPublicAccessList(Set<String> publicAccessList) {
        this.publicAccessList = publicAccessList;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfileInfoUpdateRequest that = (ProfileInfoUpdateRequest) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (formalName != null ? !formalName.equals(that.formalName) : that.formalName != null)
            return false;
        if (bengaliName != null ? !bengaliName.equals(that.bengaliName) : that.bengaliName != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (dateOfBirthDate != null ? !dateOfBirthDate.equals(that.dateOfBirthDate) : that.dateOfBirthDate != null)
            return false;
        if (dateOfBirthDisplay != null ? !dateOfBirthDisplay.equals(that.dateOfBirthDisplay) : that.dateOfBirthDisplay != null)
            return false;
        if (organizationName != null ? !organizationName.equals(that.organizationName) : that.organizationName != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (profession != null ? !profession.equals(that.profession) : that.profession != null)
            return false;
        if (district != null ? !district.equals(that.district) : that.district != null)
            return false;
        if (thana != null ? !thana.equals(that.thana) : that.thana != null) return false;
        return publicAccessList != null ? publicAccessList.equals(that.publicAccessList) : that.publicAccessList == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (formalName != null ? formalName.hashCode() : 0);
        result = 31 * result + (bengaliName != null ? bengaliName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (dateOfBirthDate != null ? dateOfBirthDate.hashCode() : 0);
        result = 31 * result + (dateOfBirthDisplay != null ? dateOfBirthDisplay.hashCode() : 0);
        result = 31 * result + (organizationName != null ? organizationName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (profession != null ? profession.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (thana != null ? thana.hashCode() : 0);
        result = 31 * result + (publicAccessList != null ? publicAccessList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProfileInfoUpdateRequest{" +
                "name='" + name + '\'' +
                ", formalName='" + formalName + '\'' +
                ", bengaliName='" + bengaliName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirthDate='" + dateOfBirthDate + '\'' +
                ", dateOfBirthDisplay='" + dateOfBirthDisplay + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", description='" + description + '\'' +
                ", profession=" + profession +
                ", district=" + district +
                ", thana=" + thana +
                ", publicAccessList=" + publicAccessList +
                '}';
    }
}