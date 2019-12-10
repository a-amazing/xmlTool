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
 * EXCEL解析和导出工具类
 * @author:wangyi
 * @Date:2019/12/6
 */
public class ExcelUtils {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";
    private static final Pattern UPPER_LETTER = Pattern.compile("^[A-Z]+$");
    //private static final Pattern NUMS_PATTERN = Pattern.compile("^[1-9]+[0-9]*$");
    private static final Pattern SEQ_PATTERN = Pattern.compile("^[1-9][0-9]*[\\.]{1}[0]{1}$");
    private static final String REPORT_CELL_INCLUDED_VALUE = "0.0";
    private static final String REPORT_CELL_EXCLUDED_VALUE = "9.9";
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

    /**
     * 传入workbok,起始excel坐标,结束excel坐标
     * @param workbook
     * @param start 起始excel坐标
     * @param end 结束excel坐标
     * @return
     */
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
                if (row == null) {
                    break;
                }
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                cellType = cell.getCellType();
                //字符串内容,匹配大写字母,存入map作为html坐标
                if (CellType.STRING.getCode() == cellType) {
                    strVal = cell.getStringCellValue();
                    if (strVal == null || strVal.isEmpty()) {
                        continue;
                    }
                    if (j != startColumnNum) {
                        matcher = UPPER_LETTER.matcher(strVal);
                        if (matcher.find()) {
                            //如果已存在,说明解析到了新的附录表,清空excel坐标和html坐标映射关系,subReportCode子表标志+1
                            if (columnMap.containsValue(strVal)) {
                                columnMap = new HashMap<>();
                                rowMap = new HashMap<>();
                                subReportCode++;
                            }
                            columnMap.put(cell.getColumnIndex(), strVal);
                        }
                    }
                    /**if (strVal.startsWith("附注")) {
                     //进入子表,清空excelRowNum和htmlRowNum关系
                     rowMap = new HashMap<Integer, String>();
                     subReportCode++;
                     }*/
                    /**if(strVal.contains("报表日期：") || strVal.contains("填表人：") ||
                     strVal.contains("复核人：") || strVal.contains("负责人：")){
                     不专门处理以上字段
                     }*/
                    //如果是数字类型,0.0代表需要填写的值,9.9代表不需要填写的值
                } else if (CellType.NUMERIC.getCode() == cellType) {
                    doubleVal = cell.getNumericCellValue();
                    strVal = String.valueOf(doubleVal);
                    if (j == startColumnNum) {
                        //判断是否为序号
                        matcher = SEQ_PATTERN.matcher(strVal);
                        if (matcher.find()) {
                            rowMap.put(cell.getRowIndex(), strVal.substring(0, strVal.indexOf(".")));
                            continue;
                        }
                    }
                    if (REPORT_CELL_INCLUDED_VALUE.equals(strVal)) {
                        cellEntity = CellEntity.buildCellEntity(cell.getRowIndex(), cell.getColumnIndex(), rowMap.get(cell.getRowIndex()),
                                columnMap.get(cell.getColumnIndex()), HAS_DATA_YES, subReportCode);
                        cells.add(cellEntity);
                        //记录数据,设为不处理
                    } else if (REPORT_CELL_EXCLUDED_VALUE.equals(strVal)) {
                        cellEntity = CellEntity.buildCellEntity(cell.getRowIndex(), cell.getColumnIndex(), rowMap.get(cell.getRowIndex()),
                                columnMap.get(cell.getColumnIndex()), HAS_DATA_NO, subReportCode);
                        cells.add(cellEntity);
                    }
                    //如果是公式,说明肯定是需要填写的内容
                } else if (CellType.FORMULA.getCode() == cellType) {
                    cellEntity = CellEntity.buildCellEntity(cell.getRowIndex(), cell.getColumnIndex(), rowMap.get(cell.getRowIndex()),
                            columnMap.get(cell.getColumnIndex()), HAS_DATA_YES, subReportCode);
                    cells.add(cellEntity);
                }
            }
        }

        return cells;
    }

    private ExcelUtils() {
    }
}
