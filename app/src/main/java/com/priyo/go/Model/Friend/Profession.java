package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Profession implements Serializable, Resource {

    @SerializedName("nodeLabel")
    private String nodeLabel;
    @SerializedName("nodeID")
    private int nodeID;
    @SerializedName("nodeTag")
    private String nodeTag;
    @SerializedName("nodeTitle")
    private String nodeTitle;

    public Profession() {
    }

    public Profession(String nodeLabel, int nodeID, String nodeTag, String nodeTitle) {
        this.nodeLabel = nodeLabel;
        this.nodeID = nodeID;
        this.nodeTag = nodeTag;
        this.nodeTitle = nodeTitle;
    }


    public String getNodeLabel() {
        return nodeLabel;
    }


    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }


    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
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

    @Override
    public String toString() {
        return "District{" +
                "nodeLabel='" + nodeLabel + '\'' +
                ", nodeID=" + nodeID +
                ", nodeTag='" + nodeTag + '\'' +
                ", nodeTitle='" + nodeTitle + '\'' +
                '}';
    }
}
