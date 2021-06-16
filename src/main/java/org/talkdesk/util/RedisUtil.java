package org.talkdesk.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

@Slf4j
public class RedisUtil {
    //用来保存因Redis 不起作用时的数据
    private Map mydata = new HashMap();

    //======判断key是否存在=============
    public boolean exists(String key) {
        return this.mydata.containsKey(key);
    }

    //==========设置key的值=============
    public void set(String key, Object value) {
        try {
            this.mydata.put(key, toJson(value));
        } catch (Exception e0) {
            log.info(e0.getMessage());
        }
    }

    //===============得到key的值============
    public String get(String key) {
        String value = null;
        if (this.mydata.containsKey(key)) {
            value = String.valueOf(this.mydata.get(key));
        }
        return value;
    }

    public <T> T get(String key, Class<T> clazz) {
        String value = null;

            if(this.mydata.containsKey(key))
            {
                value = String.valueOf(this.mydata.get(key));
            }
        return value == null ? null : fromJson(value, clazz);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSONObject.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        JSON.parseObject(json, clazz);
        return JSON.parseObject(json,clazz);
    }

}
