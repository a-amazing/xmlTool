package com.wangyi.xmlTool.util;

import com.wangyi.xmlTool.dataObject.CellEntity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:wangyi
 * @Date:2019/12/6
 */
public class ExcelUtils {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";
    private static final Pattern UPPER_LETTER = Pattern.compile("^[A-Z]+$");
    private static final Pattern NUMS_PATTERN = Pattern.compile("^[1-9]+[0-9]*$");
    private static final int HAS_DATA_YES = 1;
    private static final int HAS_DATA_NO = 0;

    //判断Excel的版本,获取Workbook
    public static Workbook getWorkbok(File file) throws IOException {
        Workbook wb = null;
        InputStream in = new FileInputStream(file);
        if (file.getName().endsWith(EXCEL_XLS)) {  //Excel 2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    //判断文件是否是excel
    public static void checkExcelVaild(File file) throws Exception {
        if (!file.exists()) {
            throw new Exception("文件不存在");
        }
        if (!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))) {
            throw new Exception("文件不是Excel");
        }
    }

    public static List<CellEntity> parse(Workbook workbook, String start, String end) {
        CellAddress startAddr = new CellAddress(start);
        CellAddress endAddr = new CellAddress(end);
        int startRowNum = startAddr.getRow();
        int startColumnNum = startAddr.getColumn();
        int endRowNum = endAddr.getRow();
        int endColumnNum = endAddr.getColumn();
        Map<Integer, String> columnMap = new HashMap<Integer, String>();
        Map<Integer, String> rowMap = new HashMap<Integer, String>();
        List<CellEntity> cells = new ArrayList<CellEntity>();
        int subReportCode = 1;

        CellEntity cellEntity = null;
        Row row = null;
        Cell cell = null;
        Matcher matcher;
        int cellType;
        String strVal;
        double doubleVal;
        //每次只解析一个sheet
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = startRowNum; i <= endRowNum; i++) {
            row = sheet.getRow(i);
            for (int j = startColumnNum; j <= endColumnNum; j++) {
                cell = row.getCell(j);
                cellType = cell.getCellType();
                if (CellType.STRING.getCode() == cellType) {
                    strVal = cell.getStringCellValue();
                    if (strVal == null || strVal.isEmpty()) {
                        continue;
                    }
                    if (j == startColumnNum) {
                        matcher = NUMS_PATTERN.matcher(strVal);
                        if (matcher.find()) {
                            rowMap.put(cell.getRowIndex(), strVal);
                            continue;
                        }
                    } else {
                        matcher = UPPER_LETTER.matcher(strVal);
                        if (matcher.find()) {
                            columnMap.put(cell.getColumnIndex(), strVal);
                            continue;
                        }
                    }
                    if (strVal.startsWith("附注")) {
                        //进入子表,清空excelRowNum和htmlRowNum关系
                        rowMap = new HashMap<Integer, String>();
                        subReportCode++;
                    }
                    /**if(strVal.contains("报表日期：") || strVal.contains("填表人：") ||
                     strVal.contains("复核人：") || strVal.contains("负责人：")){
                     不专门处理以上字段
                     }*/
                } else if (CellType.NUMERIC.getCode() == cellType) {
                    doubleVal = cell.getNumericCellValue();
                    if (0.00D == doubleVal) {
                        cellEntity = CellEntity.buildCellEntity(cell.getRowIndex(), cell.getColumnIndex(), rowMap.get(cell.getRowIndex()),
                                columnMap.get(cell.getColumnIndex()), HAS_DATA_YES, subReportCode);
                        //记录数据,设为不处理
                    } else if (9.99D == doubleVal) {
                        cellEntity = CellEntity.buildCellEntity(cell.getRowIndex(), cell.getColumnIndex(), rowMap.get(cell.getRowIndex()),
                                columnMap.get(cell.getColumnIndex()), HAS_DATA_NO, subReportCode);
                    }
                    if (cellEntity != null) {
                        cells.add(cellEntity);
                    }
                }
            }
        }

        return cells;
    }

    /**
     * 根据数据库中存储的列号/上传者标记的列号,获取excel文件中对应的rowNum
     * @param rowLetters
     * @return
     */
    /**
     * public static int getRowNum(String rowLetters) {
     * if (rowLetters == null || rowLetters.isEmpty()) {
     * throw new RuntimeException("需要解析的列号为空!");
     * }
     * int rowNum = 0;
     * char c = 0;
     * int multi = 1;
     * for (int i = rowLetters.length() - 1; i > -1; i--) {
     * c = rowLetters.charAt(i);
     * rowNum = rowNum + multi * (c - START_NUM);
     * multi *= 26;
     * }
     * if (rowNum <= 0) {
     * throw new RuntimeException("解析列号异常!");
     * }
     * return rowNum;
     * }
     */


    private ExcelUtils() {
    }
}
