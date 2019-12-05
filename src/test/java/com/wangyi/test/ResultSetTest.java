package com.wangyi.test;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:wangyi
 * @Date:2019/12/2
 */
public class ResultSetTest {
    @Test
    public void testParse() throws SQLException {
        ResultSet rs = null;
        if(rs.next()){
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
        }
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.toArray(new String[0]);
    }
}
