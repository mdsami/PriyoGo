package com.priyo.go.Model.node;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sajid.shahriar on 5/4/17.
 */

public class Business implements Comparable<Business>, Serializable {


    private static final long serialVersionUID = -5404162607876331577L;
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

    public Business() {
    }

    public Business(Long nodeID, String nodeLabel, String nodeTitle, String nodeTag, String nodePhoto, String topupOffered) {

        this.nodeID = nodeID;
        this.nodeLabel = nodeLabel;
        this.nodeTitle = nodeTitle;
        this.nodeTag = nodeTag;
        this.nodePhoto = nodePhoto;
        this.topupOffered = topupOffered;
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

        Business business = (Business) o;

        if (nodeID != null ? !nodeID.equals(business.nodeID) : business.nodeID != null)
            return false;
        if (nodeLabel != null ? !nodeLabel.equals(business.nodeLabel) : business.nodeLabel != null)
            return false;
        if (nodeTitle != null ? !nodeTitle.equals(business.nodeTitle) : business.nodeTitle != null)
            return false;
        if (nodeTag != null ? !nodeTag.equals(business.nodeTag) : business.nodeTag != null)
            return false;
        if (nodePhoto != null ? !nodePhoto.equals(business.nodePhoto) : business.nodePhoto != null)
            return false;
        return topupOffered != null ? topupOffered.equals(business.topupOffered) : business.topupOffered == null;

    }

    @Override
    public int hashCode() {
        int result = nodeID != null ? nodeID.hashCode() : 0;
        result = 31 * result + (nodeLabel != null ? nodeLabel.hashCode() : 0);
        result = 31 * result + (nodeTitle != null ? nodeTitle.hashCode() : 0);
        result = 31 * result + (nodeTag != null ? nodeTag.hashCode() : 0);
        result = 31 * result + (nodePhoto != null ? nodePhoto.hashCode() : 0);
        result = 31 * result + (topupOffered != null ? topupOffered.hashCode() : 0);
        return result;
    }


    @Override
    public int compareTo(@NonNull Business o) {
        return this.nodeID.compareTo(o.nodeID);
    }


    @Override
    public String toString() {
        return "Business{" +
                "nodeID=" + nodeID +
                ", nodeLabel='" + nodeLabel + '\'' +
                ", nodeTitle='" + nodeTitle + '\'' +
                ", nodeTag='" + nodeTag + '\'' +
                ", nodePhoto='" + nodePhoto + '\'' +
                ", topupOffered='" + topupOffered + '\'' +
                '}';
    }
}
