package com.wangyi.test;

import com.wangyi.xmlTool.util.PropertiesUtil;
import org.junit.Test;

/**
 * @author:wangyi
 * @Date:2019/12/2
 */
public class PropertiesUtilTest {

    @Test
    public void testGetValue(){
        System.out.println(PropertiesUtil.getValue("filename", "filename"));
    }
}
