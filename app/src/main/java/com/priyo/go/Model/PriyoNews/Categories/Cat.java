package com.priyo.go.Model.PriyoNews.Categories;

import java.util.ArrayList;

/**
 * Created by MUKUL on 11/10/16.
 */

public class Cat {

    private String title;
    private String titleInEnglish;
    private String description;
    private String slug;
    private String parentId;
    private Integer weight;
    private String status;
    private Boolean showInMenu;
    private Boolean isDeleted;
    private String deletedAt;
    private String id;

    private ArrayList<Child> children = new ArrayList<Child>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleInEnglish() {
        return titleInEnglish;
    }

    public void setTitleInEnglish(String titleInEnglish) {
        this.titleInEnglish = titleInEnglish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getShowInMenu() {
        return showInMenu;
    }

    public void setShowInMenu(Boolean showInMenu) {
        this.showInMenu = showInMenu;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }


}
