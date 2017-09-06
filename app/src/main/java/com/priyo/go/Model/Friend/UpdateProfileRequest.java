package com.priyo.go.Model.Friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UpdateProfileRequest implements Serializable {

    @SerializedName("nodeID")
    private String nodeID;
    @SerializedName("label")
    private String label;
    @SerializedName("properties")
    private List<InfoUpdateProfile> properties;

    @SerializedName("publicAccessList")
    private List<String> publicAccessList;




    public UpdateProfileRequest(String nodeID, String label, List<InfoUpdateProfile> properties, List<String> publicAccessList) {
        this.nodeID = nodeID;
        this.label = label;
        this.properties = properties;
        this.publicAccessList = publicAccessList;
    }

    public UpdateProfileRequest(String nodeID, String label,  List<String> publicAccessList) {
        this.nodeID = nodeID;
        this.label = label;
        this.publicAccessList = publicAccessList;
    }

    public String getNodeID() {
        return nodeID;
    }

    public String getLabel() {
        return label;
    }

    public List<InfoUpdateProfile> getProperties() {
        return properties;
    }

    public List<String> getPublicAccessList() {
        return publicAccessList;
    }
}
