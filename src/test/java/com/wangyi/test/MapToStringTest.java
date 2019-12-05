package com.wangyi.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * @author:wangyi
 * @Date:2019/12/5
 */
public class MapToStringTest {
    @Test
    public void printTest() {
        String json = "{\"test\":\"test\",\"arr\":[{\"name\":\"111\"},{\"name\":\"222\"}]}";
        JSONObject jsonObject = JSON.parseObject(json);
        Object temp;
        for (String s : jsonObject.keySet()) {
            if ((temp = jsonObject.get(s)) instanceof JSONArray) {
                System.out.println(((JSONArray) (temp)).toJSONString());
            }
        }
    }
}
