package com.priyo.go.Model.Business;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileDetailsNode implements Serializable{

    @SerializedName("nodeLabel")
    private String nodeLabel;
    @SerializedName("nodeID")
    private String nodeID;
    @SerializedName("nodeTitle")
    private String nodeTitle;
    @SerializedName("nodeTag")
    private String nodeTag;
    @SerializedName("properties")
    private ArrayList<PropertiesNode> properties ;
    @SerializedName("relationships")
    private ArrayList<RelationshipNode> relationships ;

    public ProfileDetailsNode() {

    }

    public ProfileDetailsNode(String nodeLabel, String nodeID, String nodeTitle, ArrayList<PropertiesNode> properties, ArrayList<RelationshipNode> relationships) {
        this.nodeLabel = nodeLabel;
        this.nodeID = nodeID;
        this.nodeTitle = nodeTitle;
        this.properties = properties;
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

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public ArrayList<PropertiesNode>  getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<PropertiesNode>  properties) {
        this.properties = properties;
    }

    public String getNodeTag() {
        return nodeTag;
    }

    public void setNodeTag(String nodeTag) {
        this.nodeTag = nodeTag;
    }

    public ArrayList<RelationshipNode> getRelationships() {
        return relationships;
    }

    public void setRelationships(ArrayList<RelationshipNode> relationships) {
        this.relationships = relationships;
    }
}
