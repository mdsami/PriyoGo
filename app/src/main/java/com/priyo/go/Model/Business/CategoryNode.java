package com.priyo.go.Model.Business;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryNode implements Comparable<CategoryNode> ,Serializable{

    @SerializedName("nodeLabel")
    private String nodeLabel;
    @SerializedName("nodeID")
    private String nodeID;
    @SerializedName("nodeTitle")
    private String nodeTitle;
    @SerializedName("nodeTag")
    private String nodeTag;

    public CategoryNode() {

    }

    public CategoryNode(String nodeLabel, String nodeID, String nodeTitle, String nodeTag) {
        this.nodeLabel = nodeLabel;
        this.nodeID = nodeID;
        this.nodeTitle = nodeTitle;
        this.nodeTag = nodeTag;
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

    @Override
    public String toString() {
        return "FriendNode{" +
                "name=" + nodeLabel +
                ", phoneNumber='" + nodeID +
                ", photo=" + nodeTitle +
                ", info=" + nodeTag +
                '}';
    }

    @Override
    public int compareTo(CategoryNode another) {
        return nodeID.compareTo(another.nodeID);
    }
}
