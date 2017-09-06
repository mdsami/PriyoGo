package com.priyo.go.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 4/19/17.
 */

public class ProfileInfo implements Serializable, Parcelable {


    public static final Creator<ProfileInfo> CREATOR = new Creator<ProfileInfo>() {
        @Override
        public ProfileInfo createFromParcel(Parcel in) {
            return new ProfileInfo(in);
        }

        @Override
        public ProfileInfo[] newArray(int size) {
            return new ProfileInfo[size];
        }
    };
    @SerializedName("_cls")
    private String className;
    @SerializedName("type")
    private String type;
    @SerializedName("created_at")
    private DateInfo createdAt;
    @SerializedName("location")
    private LocationInfo locationInfo;
    @SerializedName("name")
    private String name = "";
    @SerializedName("formal_name")
    private String formalName = "";
    @SerializedName("bengali_name")
    private String bengaliName = "";
    @SerializedName("email")
    private String email = "";
    @SerializedName("gender")
    private String gender = "";
    @SerializedName("cover_photo_uri")
    private String coverPhotoUri = "";
    @SerializedName("photo_uri")
    private String photoUri = "";
    @SerializedName("dob_date")
    private DateInfo dateOfBirthDate = new DateInfo(0);
    @SerializedName("dob_display")
    private String dateOfBirthDisplay = "";
    @SerializedName("organization_name")
    private String organizationName = "";
    @SerializedName("description")
    private String description = "";
    @SerializedName("profession")
    private String profession = "";
    @SerializedName("district")
    private String district = "";
    @SerializedName("thana")
    private String thana = "";

    public ProfileInfo() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public ProfileInfo(String className, String type, DateInfo createdAt, LocationInfo locationInfo, String name, String formalName, String bengaliName, String email, String gender, String coverPhotoUri, String photoUri, DateInfo dateOfBirthDate, String dateOfBirthDisplay, String organizationName, String description, String profession, String district, String thana) {

        this.className = className;
        this.type = type;
        this.createdAt = createdAt;
        this.locationInfo = locationInfo;
        this.name = name;
        this.formalName = formalName;
        this.bengaliName = bengaliName;
        this.email = email;
        this.gender = gender;
        this.coverPhotoUri = coverPhotoUri;
        this.photoUri = photoUri;
        this.dateOfBirthDate = dateOfBirthDate;
        this.dateOfBirthDisplay = dateOfBirthDisplay;
        this.organizationName = organizationName;
        this.description = description;
        this.profession = profession;
        this.district = district;
        this.thana = thana;
    }

    protected ProfileInfo(Parcel in) {
        className = in.readString();
        type = in.readString();
        createdAt = in.readParcelable(DateInfo.class.getClassLoader());
        locationInfo = in.readParcelable(LocationInfo.class.getClassLoader());
        name = in.readString();
        formalName = in.readString();
        bengaliName = in.readString();
        email = in.readString();
        gender = in.readString();
        coverPhotoUri = in.readString();
        photoUri = in.readString();
        dateOfBirthDate = in.readParcelable(DateInfo.class.getClassLoader());
        dateOfBirthDisplay = in.readString();
        organizationName = in.readString();
        description = in.readString();
        profession = in.readString();
        district = in.readString();
        thana = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
        dest.writeString(type);
        dest.writeParcelable(createdAt, flags);
        dest.writeParcelable(locationInfo, flags);
        dest.writeString(name);
        dest.writeString(formalName);
        dest.writeString(bengaliName);
        dest.writeString(email);
        dest.writeString(gender);
        dest.writeString(coverPhotoUri);
        dest.writeString(photoUri);
        dest.writeParcelable(dateOfBirthDate, flags);
        dest.writeString(dateOfBirthDisplay);
        dest.writeString(organizationName);
        dest.writeString(description);
        dest.writeString(profession);
        dest.writeString(district);
        dest.writeString(thana);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getClassName() {

        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DateInfo getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateInfo createdAt) {
        this.createdAt = createdAt;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
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

    public String getCoverPhotoUri() {
        return coverPhotoUri;
    }

    public void setCoverPhotoUri(String coverPhotoUri) {
        this.coverPhotoUri = coverPhotoUri;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public DateInfo getDateOfBirthDate() {
        return dateOfBirthDate;
    }

    public void setDateOfBirthDate(DateInfo dateOfBirthDate) {
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfileInfo that = (ProfileInfo) o;

        if (className != null ? !className.equals(that.className) : that.className != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null)
            return false;
        if (locationInfo != null ? !locationInfo.equals(that.locationInfo) : that.locationInfo != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (formalName != null ? !formalName.equals(that.formalName) : that.formalName != null)
            return false;
        if (bengaliName != null ? !bengaliName.equals(that.bengaliName) : that.bengaliName != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (coverPhotoUri != null ? !coverPhotoUri.equals(that.coverPhotoUri) : that.coverPhotoUri != null)
            return false;
        if (photoUri != null ? !photoUri.equals(that.photoUri) : that.photoUri != null)
            return false;
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
        return thana != null ? thana.equals(that.thana) : that.thana == null;

    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (locationInfo != null ? locationInfo.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (formalName != null ? formalName.hashCode() : 0);
        result = 31 * result + (bengaliName != null ? bengaliName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (coverPhotoUri != null ? coverPhotoUri.hashCode() : 0);
        result = 31 * result + (photoUri != null ? photoUri.hashCode() : 0);
        result = 31 * result + (dateOfBirthDate != null ? dateOfBirthDate.hashCode() : 0);
        result = 31 * result + (dateOfBirthDisplay != null ? dateOfBirthDisplay.hashCode() : 0);
        result = 31 * result + (organizationName != null ? organizationName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (profession != null ? profession.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (thana != null ? thana.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProfileInfo{" +
                "className='" + className + '\'' +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                ", locationInfo=" + locationInfo +
                ", name='" + name + '\'' +
                ", formalName='" + formalName + '\'' +
                ", bengaliName='" + bengaliName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", coverPhotoUri='" + coverPhotoUri + '\'' +
                ", photoUri='" + photoUri + '\'' +
                ", dateOfBirthDate=" + dateOfBirthDate +
                ", dateOfBirthDisplay='" + dateOfBirthDisplay + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", description='" + description + '\'' +
                ", profession='" + profession + '\'' +
                ", district='" + district + '\'' +
                ", thana='" + thana + '\'' +
                '}';
    }
}