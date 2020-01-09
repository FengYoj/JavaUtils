package com.typing.control.utils.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JsonUtils {

    /**
     * 判断对象相等
     * @param initiative 检验对象
     * @param passive 被检验对象
     * @return 检验状态
     */
    public static JsonStatus equalJson(JSONObject initiative, JSONObject passive) {
        return equalJson(initiative, passive, null);
    }

    /**
     * 判断数组相等
     * @param initiative 检验数组
     * @param passive 被检验数组
     * @return 检验状态
     */
    public static JsonStatus equalArray(JSONArray initiative, JSONArray passive) {
        return equalArray(initiative, passive, null);
    }

    /**
     * 判断对象相等
     * @param initiative 检验对象
     * @param passive 被检验对象
     * @param keys 属性名列表
     * @return 检验状态
     */
    public static JsonStatus equalJson(JSONObject initiative, JSONObject passive, List<JsonType> keys) {
        if (initiative.size() != passive.size()) {
            return getSizeError(initiative, passive, keys);
        }

        for (String key: initiative.keySet()) {
            if (!passive.containsKey(key)) {
                return JsonStatus.of(false, "少了" + key + "属性", keys);
            }

            JsonStatus jsonStatus = equal(initiative.get(key), passive.get(key), keys, key, JsonType.JSON);

            if (!jsonStatus.getIsNormal()) {
                return jsonStatus;
            }
        }

        return JsonStatus.of(true);
    }

    /**
     * 判断数组相等
     * @param initiative 检验数组
     * @param passive 被检验数组
     * @param keys 属性名列表
     * @return 检验状态
     */
    public static JsonStatus equalArray(JSONArray initiative, JSONArray passive, List<JsonType> keys) {
        if (initiative.size() != passive.size()) {
            return JsonStatus.of(false, "数量大小不一致", keys, String.valueOf(initiative.size()));
        }

        for (int i = 0, s = initiative.size(); i < s; i++) {
            JsonStatus jsonStatus = equal(initiative.get(i), passive.get(i), keys, String.valueOf(i), JsonType.ARRAY);

            if (!jsonStatus.getIsNormal()) {
                return jsonStatus;
            }
        }

        return JsonStatus.of(true);
    }

    /**
     * 判断相等
     * @param initiative_value 检验值
     * @param passive_value 被检验值
     * @param keys 属性名列表
     * @param key 属性名
     * @param type 属性类型
     * @return 检验状态
     */
    private static JsonStatus equal(Object initiative_value, Object passive_value, List<JsonType> keys, String key, JsonType type) {
        String initiative_type = initiative_value.getClass().getTypeName();
        String passive_type = passive_value.getClass().getTypeName();

        if (!initiative_type.equals(passive_type)) {
            return JsonStatus.of(false, "类型不一致", addKey(keys, key, type), initiative_type);
        }

        // 判断数据类型名称
        switch(initiative_type.substring((initiative_type.lastIndexOf(".") + 1))) {
            case "JSONObject":
                return equalJson((JSONObject) initiative_value, (JSONObject) passive_value, addKey(keys, key, type));
            case "JSONArray":
                return equalArray((JSONArray) initiative_value, (JSONArray) passive_value, addKey(keys, key, type));
            case "String":
                if (((String) passive_value).isBlank()) {
                    return JsonStatus.of(false, "属性类型不能为空", addKey(keys, key, type));
                }
        }

        return JsonStatus.of(true);
    }

    /**
     * 获取数量大小的错误
     * @param initiative 检验对象
     * @param passive 被检验对象
     * @param key 父级 key 名称
     * @return 错误状态对象
     */
    private static JsonStatus getSizeError(JSONObject initiative, JSONObject passive, List<JsonType> keys) {
        // 属性相对多的
        Set<String> many;
        // 属性相对少的
        JSONObject less;
        // 比较属性多了还是少了
        boolean isMany = initiative.size() < passive.size();

        if (isMany) {
            many = passive.keySet();
            less = initiative;
        } else {
            many = initiative.keySet();
            less = passive;
        }

        // 循环遍历属性多的对象
        for (String keyName : many) {
            // 判断 key 是否存在
            if (!less.containsKey(keyName)) {
                return JsonStatus.of(false, (isMany ? "多" : "少") + "了 " + keyName + " 属性", keys);
            }
        }

        return JsonStatus.of(false, "属性数量大小不一致", keys);
    }

    /**
     * 添加属性名称
     * @param keys 属性名数组
     * @param key 属性名
     * @param type 属性类型
     * @return 属性名数组
     */
    private static List<JsonType> addKey(List<JsonType> keys, String key, JsonType type) {
        // 数组为空时创建新的数组
        if (keys == null) {
            keys = new ArrayList<>();
        }

        JsonType jsonType = new JsonType().setValue(key).setType(type);

        keys.add(jsonType);

        return keys;
    }
}
