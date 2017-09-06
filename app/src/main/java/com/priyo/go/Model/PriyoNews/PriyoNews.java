
package com.priyo.go.Model.PriyoNews;

import android.os.Parcel;
import android.os.Parcelable;

public class PriyoNews implements Parcelable{

    private String title;
    private String image;
    private String description;
    private String answer;
    private String summary;
    private String slug;
    private String categories ;
    private String tags ;
    private String businesses;
    private String locations;
    private String people;
    private String topics;
    private String author;
    private String status;
    private Boolean isDeleted;
    private String deletedAt;
    private String createdAt;
    private String priority;
    private String publishedAt;
    private String updatedAt;
    private String id;

    public PriyoNews() {
    }

    protected PriyoNews(Parcel in) {
        title = in.readString();
        image = in.readString();
        description = in.readString();
        answer = in.readString();
        summary = in.readString();
        slug = in.readString();
        categories = in.readString();
        tags = in.readString();
        businesses = in.readString();
        locations = in.readString();
        people = in.readString();
        topics = in.readString();
        author = in.readString();
        status = in.readString();
        deletedAt = in.readString();
        createdAt = in.readString();
        priority = in.readString();
        publishedAt = in.readString();
        updatedAt = in.readString();
        id = in.readString();
    }

    public static final Creator<PriyoNews> CREATOR = new Creator<PriyoNews>() {
        @Override
        public PriyoNews createFromParcel(Parcel in) {
            return new PriyoNews(in);
        }

        @Override
        public PriyoNews[] newArray(int size) {
            return new PriyoNews[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBusinesses() {
        return businesses;
    }

    public void setBusinesses(String businesses) {
        this.businesses = businesses;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(description);
        parcel.writeString(answer);
        parcel.writeString(summary);
        parcel.writeString(slug);
        parcel.writeString(categories);
        parcel.writeString(tags);
        parcel.writeString(businesses);
        parcel.writeString(locations);
        parcel.writeString(people);
        parcel.writeString(topics);
        parcel.writeString(author);
        parcel.writeString(status);
        parcel.writeString(deletedAt);
        parcel.writeString(createdAt);
        parcel.writeString(priority);
        parcel.writeString(publishedAt);
        parcel.writeString(updatedAt);
        parcel.writeString(id);
    }
}
