package com.priyo.go.Model.Business;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RelationshipNode implements Serializable{

    @SerializedName("relationshipLabel")
    private String relationshipLabel;
    @SerializedName("relationshipLabelToDisplay")
    private String relationshipLabelToDisplay;
    @SerializedName("relatedNodeID")
    private String relatedNodeID;
    @SerializedName("relatedNodeLabel")
    private String relatedNodeLabel;
    @SerializedName("relatedNodeTag")
    private String relatedNodeTag;
    @SerializedName("relatedNodeTitle")
    private String relatedNodeTitle;

    public RelationshipNode() {

    }

    public RelationshipNode(String relationshipLabel, String relationshipLabelToDisplay, String relatedNodeID, String relatedNodeLabel, String relatedNodeTag, String relatedNodeTitle) {
        this.relationshipLabel = relationshipLabel;
        this.relationshipLabelToDisplay = relationshipLabelToDisplay;
        this.relatedNodeID = relatedNodeID;
        this.relatedNodeLabel = relatedNodeLabel;
        this.relatedNodeTag = relatedNodeTag;
        this.relatedNodeTitle = relatedNodeTitle;
    }

    public String getRelationshipLabel() {
        return relationshipLabel;
    }

    public void setRelationshipLabel(String relationshipLabel) {
        this.relationshipLabel = relationshipLabel;
    }

    public String getRelationshipLabelToDisplay() {
        return relationshipLabelToDisplay;
    }

    public void setRelationshipLabelToDisplay(String relationshipLabelToDisplay) {
        this.relationshipLabelToDisplay = relationshipLabelToDisplay;
    }

    public String getRelatedNodeID() {
        return relatedNodeID;
    }

    public void setRelatedNodeID(String relatedNodeID) {
        this.relatedNodeID = relatedNodeID;
    }

    public String getRelatedNodeLabel() {
        return relatedNodeLabel;
    }

    public void setRelatedNodeLabel(String relatedNodeLabel) {
        this.relatedNodeLabel = relatedNodeLabel;
    }

    public String getRelatedNodeTag() {
        return relatedNodeTag;
    }

    public void setRelatedNodeTag(String relatedNodeTag) {
        relatedNodeTag = relatedNodeTag;
    }

    public String getRelatedNodeTitle() {
        return relatedNodeTitle;
    }

    public void setRelatedNodeTitle(String relatedNodeTitle) {
        this.relatedNodeTitle = relatedNodeTitle;
    }
}
