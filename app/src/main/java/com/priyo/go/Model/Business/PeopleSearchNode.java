package com.priyo.go.Model.Business;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PeopleSearchNode implements Comparable<PeopleSearchNode> ,Serializable{

    @SerializedName("nodeLabel")
    private String nodeLabel;
    @SerializedName("nodeID")
    private String nodeID;
    @SerializedName("nodeTitle")
    private String nodeTitle;
    @SerializedName("nodeTag")
    private String nodeTag;
    @SerializedName("titleInEnglish")
    private String titleInEnglish;

    public PeopleSearchNode() {

    }



    public String getNodeLabel() {
        return nodeLabel;
    }

    public PeopleSearchNode(String nodeLabel, String nodeID, String nodeTitle, String nodeTag, String titleInEnglish) {
        this.nodeLabel = nodeLabel;
        this.nodeID = nodeID;
        this.nodeTitle = nodeTitle;
        this.nodeTag = nodeTag;
        this.titleInEnglish = titleInEnglish;
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

    public String getTitleInEnglish() {
        return titleInEnglish;
    }

    public void setTitleInEnglish(String titleInEnglish) {
        this.titleInEnglish = titleInEnglish;
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
    public int compareTo(PeopleSearchNode another) {
        return nodeID.compareTo(another.nodeID);
    }
}
