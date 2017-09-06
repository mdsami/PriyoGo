package com.priyo.go.database.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 5/4/17.
 */

@Entity
public class NearbyBusiness implements Comparable<NearbyBusiness>, Serializable {


    private static final long serialVersionUID = -821134612273688249L;

    @Id
    private Long id;
    @Unique
    @SerializedName("nodeID")
    private Long nodeID;
    @SerializedName("nodeLabel")
    private String nodeLabel;
    @SerializedName("nodeTitle")
    private String nodeTitle;
    @SerializedName("nodeTag")
    private String nodeTag;
    @SerializedName("nodePhoto")
    private String nodePhoto;
    @SerializedName("topupOffered")
    private String topupOffered;


    @Generated(hash = 452636826)
    public NearbyBusiness() {
    }

    public NearbyBusiness(Long id) {

        this.id = id;
    }

    @Generated(hash = 2084035976)
    public NearbyBusiness(Long id, Long nodeID, String nodeLabel, String nodeTitle, String nodeTag, String nodePhoto, String topupOffered) {
        this.id = id;
        this.nodeID = nodeID;
        this.nodeLabel = nodeLabel;
        this.nodeTitle = nodeTitle;
        this.nodeTag = nodeTag;
        this.nodePhoto = nodePhoto;
        this.topupOffered = topupOffered;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNodeID() {
        return nodeID;
    }

    public void setNodeID(Long nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public String getNodeTag() {
        return nodeTag;
    }

    public void setNodeTag(String nodeTag) {
        this.nodeTag = nodeTag;
    }

    public String getNodePhoto() {
        return nodePhoto;
    }

    public void setNodePhoto(String nodePhoto) {
        this.nodePhoto = nodePhoto;
    }

    public String getTopupOffered() {
        return topupOffered;
    }

    public void setTopupOffered(String topupOffered) {
        this.topupOffered = topupOffered;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NearbyBusiness that = (NearbyBusiness) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (nodeID != null ? !nodeID.equals(that.nodeID) : that.nodeID != null) return false;
        if (nodeLabel != null ? !nodeLabel.equals(that.nodeLabel) : that.nodeLabel != null)
            return false;
        if (nodeTitle != null ? !nodeTitle.equals(that.nodeTitle) : that.nodeTitle != null)
            return false;
        if (nodeTag != null ? !nodeTag.equals(that.nodeTag) : that.nodeTag != null) return false;
        if (nodePhoto != null ? !nodePhoto.equals(that.nodePhoto) : that.nodePhoto != null)
            return false;
        return topupOffered != null ? topupOffered.equals(that.topupOffered) : that.topupOffered == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nodeID != null ? nodeID.hashCode() : 0);
        result = 31 * result + (nodeLabel != null ? nodeLabel.hashCode() : 0);
        result = 31 * result + (nodeTitle != null ? nodeTitle.hashCode() : 0);
        result = 31 * result + (nodeTag != null ? nodeTag.hashCode() : 0);
        result = 31 * result + (nodePhoto != null ? nodePhoto.hashCode() : 0);
        result = 31 * result + (topupOffered != null ? topupOffered.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(@NonNull NearbyBusiness o) {
        return this.nodeID.compareTo(o.nodeID);
    }


    @Override
    public String toString() {
        return "NearbyBusiness{" +
                "id=" + id +
                ", nodeID=" + nodeID +
                ", nodeLabel='" + nodeLabel + '\'' +
                ", nodeTitle='" + nodeTitle + '\'' +
                ", nodeTag='" + nodeTag + '\'' +
                ", nodePhoto='" + nodePhoto + '\'' +
                ", topupOffered='" + topupOffered + '\'' +
                '}';
    }
}
