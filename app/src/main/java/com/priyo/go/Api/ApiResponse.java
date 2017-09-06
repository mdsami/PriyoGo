package com.priyo.go.Api;

/**
 * Created by sajid.shahriar on 4/14/17.
 */

public class ApiResponse<T> {

    private int statusCode;
    private String apiCommand;
    private T t;

    public ApiResponse() {
    }

    public ApiResponse(int statusCode, String apiCommand, T t) {

        this.statusCode = statusCode;
        this.apiCommand = apiCommand;
        this.t = t;
    }

    public int getStatusCode() {

        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getApiCommand() {
        return apiCommand;
    }

    public void setApiCommand(String apiCommand) {
        this.apiCommand = apiCommand;
    }

    public T getObject() {
        return t;
    }

    public void setObject(T t) {
        this.t = t;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiResponse<?> that = (ApiResponse<?>) o;

        if (statusCode != that.statusCode) return false;
        if (apiCommand != null ? !apiCommand.equals(that.apiCommand) : that.apiCommand != null)
            return false;
        return t != null ? t.equals(that.t) : that.t == null;

    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + (apiCommand != null ? apiCommand.hashCode() : 0);
        result = 31 * result + (t != null ? t.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "statusCode=" + statusCode +
                ", apiCommand='" + apiCommand + '\'' +
                ", t=" + t +
                '}';
    }
}
