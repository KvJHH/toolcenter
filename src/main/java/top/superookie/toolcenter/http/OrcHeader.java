package top.superookie.toolcenter.http;

import org.apache.http.HttpHeaders;

public enum OrcHeader {

    CONTENT_TYPE_JSON(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

    private final String name;
    private final String value;

    OrcHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
