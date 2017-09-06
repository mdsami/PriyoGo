package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

public class PushNode{

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("msg")
    private String msg;
    @SerializedName("url")
    private String url;
    @SerializedName("type")
    private String type;
    @SerializedName("updatedAt")
    private long updatedAt;
    @SerializedName("isOpen")
    private boolean isOpen;

    public PushNode() {

    }

    public PushNode(String title, String msg, String url, String type, long updatedAt, boolean isOpen) {
        this.title = title;
        this.msg = msg;
        this.url = url;
        this.type = type;
        this.updatedAt = updatedAt;
        this.isOpen = isOpen;
    }

    public PushNode(String id, String title, String msg, String url, String type, long updatedAt, boolean isOpen) {
        this.id = id;
        this.title = title;
        this.msg = msg;
        this.url = url;
        this.type = type;
        this.updatedAt = updatedAt;
        this.isOpen = isOpen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public String toString() {
        return "PushNode{" +
                "title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", updatedAt=" + updatedAt +
                ", isOpen=" + isOpen +
                '}';
    }

}
