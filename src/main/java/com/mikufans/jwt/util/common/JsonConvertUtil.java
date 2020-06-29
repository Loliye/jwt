package com.mikufans.jwt.util.common;

import com.alibaba.fastjson.JSONObject;

public class JsonConvertUtil
{
    private JsonConvertUtil() {}

    public static <T> T jsonToObject(String pojo, Class<T> cls)
    {
        return JSONObject.parseObject(pojo, cls);
    }

    public static <T> String objectToJson(T t)
    {
        return JSONObject.toJSONString(t);
    }
}
