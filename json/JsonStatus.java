package com.typing.control.utils.json;

import java.util.List;

public class JsonStatus {
    private boolean isNormal;

    private String message;

    private String prompt;

    private List<JsonType> keys;

    public boolean getIsNormal() {
        return isNormal;
    }

    public String getMessage() {
        return message == null ? "" : (getKeysToString() + "对象中" + message + (prompt == null ? "" : "，应为：" + prompt));
    }

    public JsonStatus setIsNormal(boolean isNormal) {
        this.isNormal = isNormal;
        return this;
    }

    public JsonStatus setMessage(String message) {
        this.message = message;
        return this;
    }

    public JsonStatus setPrompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    public JsonStatus setKeys(List<JsonType> keys) {
        this.keys = keys;
        return this;
    }

    @Override
    public String toString() {
        return "JsonStatus{" +
                "isNormal=" + isNormal +
                ", message='" + message + '\'' +
                '}';
    }

    private String getKeysToString() {
        List<JsonType> keys = this.keys;

        if (keys == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0, l = keys.size(); i < l; i++) {
            JsonType jsonType = keys.get(i);

            if (jsonType.getType().equals("JSON")) {
                if (i != 0) {
                    sb.append(".");
                }

                sb.append(jsonType.getValue());
            } else {
                sb.append("[");
                sb.append(jsonType.getValue());
                sb.append("]");
            }
        }

        return sb.toString();
    }

    public static JsonStatus of(boolean isNormal) {
        return new JsonStatus().setIsNormal(isNormal);
    }

    public static JsonStatus of(boolean isNormal, String message, List<JsonType> keys, String prompt) {
        return new JsonStatus().setIsNormal(isNormal).setMessage(message).setKeys(keys).setPrompt(prompt);
    }

    public static JsonStatus of(boolean isNormal, String message, List<JsonType> keys) {
        return new JsonStatus().setIsNormal(isNormal).setMessage(message).setKeys(keys);
    }
}
