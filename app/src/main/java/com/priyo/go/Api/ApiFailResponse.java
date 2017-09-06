package com.priyo.go.Api;

/**
 * Created by sajid.shahriar on 4/22/17.
 */

public class ApiFailResponse {

    private final int statusCode;
    private final String message;

    public ApiFailResponse() {
        this(-1, null);
    }

    public ApiFailResponse(int statusCode, String message) {

        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {

        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiFailResponse that = (ApiFailResponse) o;

        if (statusCode != that.statusCode) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ApiFailResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}
