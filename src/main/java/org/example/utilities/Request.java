package org.example.utilities;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String path;
    private Map<String, String> queryParams;
    private String method;

    public Request(String path, String method) {
        this.path = path;
        this.method = method;
        this.queryParams = new HashMap<>();
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public void setQueryParam(String key, String value) {
        queryParams.put(key, value);
    }

    public String getValues(String key) {
        return queryParams.getOrDefault(key, "");
    }

    public Map<String, String> getAllQueryParams() {
        return queryParams;
    }
}
