package com.priyo.go.Model.graph;

import com.google.gson.annotations.SerializedName;
import com.priyo.go.Model.Business.RelationshipNode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sajid.shahriar on 4/12/17.
 */

public class GraphInfo implements Serializable {


    @SerializedName("nodeLabel")
    private String nodeLabel;
    @SerializedName("nodeID")
    private String nodeID;
    @SerializedName("nodeTag")
    private String nodeTag;
    @SerializedName("noteTitle")
    private String nodeTitle;
    @SerializedName("topupOffered")
    private boolean topupOffered;
    @SerializedName("nodeCreateAt")
    private long nodeCreatedAt;
    @SerializedName("nodeUpdatedAt")
    private long nodeUpdatedAt;
    @SerializedName("properties")
    private List<Property> properties;
    @SerializedName("publicAccessList")
    private List<String> publicAccessList;
    @SerializedName("relationships")
    private List<RelationshipNode> relationships;

    public GraphInfo() {
    }

    public GraphInfo(String nodeLabel, String nodeID, String nodeTag, String nodeTitle, boolean topupOffered, long nodeCreatedAt, long nodeUpdatedAt, List<Property> properties, List<String> publicAccessList, List<RelationshipNode> relationships) {

        this.nodeLabel = nodeLabel;
        this.nodeID = nodeID;
        this.nodeTag = nodeTag;
        this.nodeTitle = nodeTitle;
        this.topupOffered = topupOffered;
        this.nodeCreatedAt = nodeCreatedAt;
        this.nodeUpdatedAt = nodeUpdatedAt;
        this.properties = properties;
        this.publicAccessList = publicAccessList;
        this.relationships = relationships;
    }

    public String getNodeLabel() {

        return nodeLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeTag() {
        return nodeTag;
    }

    public void setNodeTag(String nodeTag) {
        this.nodeTag = nodeTag;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public boolean isTopupOffered() {
        return topupOffered;
    }

    public void setTopupOffered(boolean topupOffered) {
        this.topupOffered = topupOffered;
    }

    public long getNodeCreatedAt() {
        return nodeCreatedAt;
    }

    public void setNodeCreatedAt(long nodeCreatedAt) {
        this.nodeCreatedAt = nodeCreatedAt;
    }

    public long getNodeUpdatedAt() {
        return nodeUpdatedAt;
    }

    public void setNodeUpdatedAt(long nodeUpdatedAt) {
        this.nodeUpdatedAt = nodeUpdatedAt;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<String> getPublicAccessList() {
        return publicAccessList;
    }

    public void setPublicAccessList(List<String> publicAccessList) {
        this.publicAccessList = publicAccessList;
    }

    public List<RelationshipNode> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<RelationshipNode> relationships) {
        this.relationships = relationships;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphInfo graphInfo = (GraphInfo) o;

        if (topupOffered != graphInfo.topupOffered) return false;
        if (nodeCreatedAt != graphInfo.nodeCreatedAt) return false;
        if (nodeUpdatedAt != graphInfo.nodeUpdatedAt) return false;
        if (nodeLabel != null ? !nodeLabel.equals(graphInfo.nodeLabel) : graphInfo.nodeLabel != null)
            return false;
        if (nodeID != null ? !nodeID.equals(graphInfo.nodeID) : graphInfo.nodeID != null)
            return false;
        if (nodeTag != null ? !nodeTag.equals(graphInfo.nodeTag) : graphInfo.nodeTag != null)
            return false;
        if (nodeTitle != null ? !nodeTitle.equals(graphInfo.nodeTitle) : graphInfo.nodeTitle != null)
            return false;
        if (properties != null ? !properties.equals(graphInfo.properties) : graphInfo.properties != null)
            return false;
        if (publicAccessList != null ? !publicAccessList.equals(graphInfo.publicAccessList) : graphInfo.publicAccessList != null)
            return false;
        return relationships != null ? relationships.equals(graphInfo.relationships) : graphInfo.relationships == null;

    }

    @Override
    public int hashCode() {
        int result = nodeLabel != null ? nodeLabel.hashCode() : 0;
        result = 31 * result + (nodeID != null ? nodeID.hashCode() : 0);
        result = 31 * result + (nodeTag != null ? nodeTag.hashCode() : 0);
        result = 31 * result + (nodeTitle != null ? nodeTitle.hashCode() : 0);
        result = 31 * result + (topupOffered ? 1 : 0);
        result = 31 * result + (int) (nodeCreatedAt ^ (nodeCreatedAt >>> 32));
        result = 31 * result + (int) (nodeUpdatedAt ^ (nodeUpdatedAt >>> 32));
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        result = 31 * result + (publicAccessList != null ? publicAccessList.hashCode() : 0);
        result = 31 * result + (relationships != null ? relationships.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GraphInfo{" +
                "nodeLabel='" + nodeLabel + '\'' +
                ", nodeID='" + nodeID + '\'' +
                ", nodeTag='" + nodeTag + '\'' +
                ", nodeTitle='" + nodeTitle + '\'' +
                ", topupOffered=" + topupOffered +
                ", nodeCreatedAt=" + nodeCreatedAt +
                ", nodeUpdatedAt=" + nodeUpdatedAt +
                ", properties=" + properties +
                ", publicAccessList=" + publicAccessList +
                ", relationships=" + relationships +
                '}';
    }
}
