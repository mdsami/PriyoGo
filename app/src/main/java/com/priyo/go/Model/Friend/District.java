package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class District implements Resource,Serializable {

    @SerializedName("nodeLabel")
    private String nodeLabel;
    @SerializedName("nodeID")
    private int nodeID;
    @SerializedName("nodeTag")
    private String nodeTag;
    @SerializedName("nodeTitle")
    private String nodeTitle;

    public District() {
    }

    public District(String nodeLabel, int nodeID, String nodeTag, String nodeTitle) {
        this.nodeLabel = nodeLabel;
        this.nodeID = nodeID;
        this.nodeTag = nodeTag;
        this.nodeTitle = nodeTitle;
    }

    @Override
    public String getNodeLabel() {
        return nodeLabel;
    }


    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    @Override
    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    @Override
    public String getNodeTag() {
        return nodeTag;
    }

    public void setNodeTag(String nodeTag) {
        this.nodeTag = nodeTag;
    }

    @Override
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
