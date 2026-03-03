package org.example.utilities;

public class Response {
    private String body;
    private String contentType;
    private int statusCode;

    public Response() {
        this.statusCode = 200;
        this.contentType = "text/html";
        this.body = "";
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
