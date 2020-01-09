package com.typing.control.utils.json;

public class JsonType {
    public static JsonType JSON = type("JSON");
    public static JsonType ARRAY = type("ARRAY");

    private String type;

    private String value;

    public JsonType setType(JsonType type) {
        this.type = type.getType();
        return this;
    }

    public JsonType setType(String type) {
        this.type = type;
        return this;
    }

    public JsonType setValue(String value) {
        this.value = value;
        return this;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    protected static JsonType type(String type) {
        return new JsonType().setType(type);
    }
}