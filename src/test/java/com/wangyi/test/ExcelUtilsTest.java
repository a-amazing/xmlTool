package com.wangyi.test;

import com.wangyi.xmlTool.dataObject.CellEntity;
import com.wangyi.xmlTool.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
        File file = new File("C:\\Users\\wbkf5\\Desktop\\xls\\S3204-191-境内汇总数据-人民币.xls");
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
        List<CellEntity> cellEntities = ExcelUtils.parse(workbok, "A1", "P60");
        System.out.println(cellEntities.size());
        for (CellEntity cellEntity : cellEntities) {
            System.out.println(cellEntity);
        }
    }

    @Test
    public void test2() {
        File file = new File("C:\\Users\\wbkf5\\Desktop\\xls\\S3204-191-境内汇总数据-人民币.xls");
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
        if (workbok == null) {
            return;
        }
        Sheet sheet = workbok.getSheetAt(0);
        Row row = null;
        Cell cell = null;
        String val = null;
        for (int i = 7; i < 31; i++) {
            row = sheet.getRow(i);
            cell = row.getCell(0);
            System.out.println("是否字符类型:" + (CellType.STRING.getCode() == cell.getCellType()?"是":"否"));
            if(CellType.STRING.getCode() == cell.getCellType()){
                val = cell.getStringCellValue();
            }else if(CellType.NUMERIC.getCode() == cell.getColumnIndex()){
                val = String.valueOf(cell.getNumericCellValue());
            }
            System.out.println(val);
        }

    }

}
