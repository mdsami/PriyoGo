package com.priyo.go.Model.api.graph.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sajid.shahriar on 4/18/17.
 */

public class AddPhoneBookResponse {
    @SerializedName("nodeID")
    private int nodeID;
    @SerializedName("label")
    private String label;
    @SerializedName("message")
    private String message;

    public AddPhoneBookResponse() {
    }

    public AddPhoneBookResponse(int nodeID, String label, String message) {

        this.nodeID = nodeID;
        this.label = label;
        this.message = message;
    }

    public int getNodeID() {

        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddPhoneBookResponse that = (AddPhoneBookResponse) o;

        if (nodeID != that.nodeID) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = nodeID;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AddPhoneBookResponse{" +
                "nodeID=" + nodeID +
                ", label='" + label + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
