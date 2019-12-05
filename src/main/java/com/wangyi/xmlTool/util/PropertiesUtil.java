package com.wangyi.xmlTool.util;

import java.util.ResourceBundle;

/**
 * @author:wangyi
 * @Date:2019/12/2
 */
public class PropertiesUtil {
    public static String getValue(String path, String key) {
        //config为属性文件名，放在包com.test.config下，如果是放在src下，直接用config即可
        ResourceBundle resource = ResourceBundle.getBundle(path);
        String value = resource.getString(key);
        return value;
    }
}
