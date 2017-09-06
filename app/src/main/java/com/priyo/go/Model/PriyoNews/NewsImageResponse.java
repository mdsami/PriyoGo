
package com.priyo.go.Model.PriyoNews;

public class NewsImageResponse {

    private String hostName="";
    private String original="";
    private String thumbnail="";
    private String medium="";
    private String large;
    private String cacheHost="";
    private String width="";
    private String height="";


    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getCacheHost() {
        return cacheHost;
    }

    public void setCacheHost(String cacheHost) {
        this.cacheHost = cacheHost;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
