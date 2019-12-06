package com.wangyi.test;

import com.wangyi.xmlTool.dataObject.CellEntity;
import com.wangyi.xmlTool.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author:wangyi
 * @Date:2019/12/6
 */
public class ExcelUtilsTest {

    @Test
    public void getMap() {
        File file = new File("C:\\Users\\wbkf5\\Desktop\\xls\\S3203-191-境内汇总数据-人民币.xls");
        Workbook workbok = null;
        try {
            ExcelUtils.checkExcelVaild(file);
            workbok = ExcelUtils.getWorkbok(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取excel文件失败!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("该文件非excel文件类型!");
        }
        if (workbok == null)
            return;
        List<CellEntity> cellEntities = ExcelUtils.parse(workbok, "A1", "Y31");
        for (CellEntity cellEntity : cellEntities) {
            System.out.println(cellEntity);
        }
    }

}
