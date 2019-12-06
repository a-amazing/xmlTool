package com.wangyi.xmlTool.dataObject;

/**
 * @author:wangyi
 * @Date:2019/12/6
 */
public class CellEntity {
    private String htmlColumn;
    private String htmlRow;
    private int excelColumn;
    private int excelRow;

    private int hasData;//0无数据,1有数据
    private int subReportCode;//根据附注来区分,默认为1,每出现一次附注加1

    public static CellEntity buildCellEntity(int excelRow, int excelColumn, String htmlRow, String htmlColumn, int hasData, int subReportCode) {
        CellEntity cellEntity = new CellEntity();
        cellEntity.setExcelColumn(excelColumn);
        cellEntity.setExcelRow(excelRow);
        cellEntity.setHasData(hasData);
        cellEntity.setHtmlRow(htmlRow);
        cellEntity.setHtmlColumn(htmlColumn);
        cellEntity.setSubReportCode(subReportCode);
        return cellEntity;

    }

    public String getHtmlColumn() {
        return htmlColumn;
    }

    public void setHtmlColumn(String htmlColumn) {
        this.htmlColumn = htmlColumn;
    }

    public String getHtmlRow() {
        return htmlRow;
    }

    public void setHtmlRow(String htmlRow) {
        this.htmlRow = htmlRow;
    }

    public int getExcelColumn() {
        return excelColumn;
    }

    public void setExcelColumn(int excelColumn) {
        this.excelColumn = excelColumn;
    }

    public int getExcelRow() {
        return excelRow;
    }

    public void setExcelRow(int excelRow) {
        this.excelRow = excelRow;
    }

    public int getHasData() {
        return hasData;
    }

    public void setHasData(int hasData) {
        this.hasData = hasData;
    }

    public int getSubReportCode() {
        return subReportCode;
    }

    public void setSubReportCode(int subReportCode) {
        this.subReportCode = subReportCode;
    }

    @Override
    public String toString() {
        return "CellEntity{" +
                "htmlColumn='" + htmlColumn + '\'' +
                ", htmlRow='" + htmlRow + '\'' +
                ", excelColumn=" + excelColumn +
                ", excelRow=" + excelRow +
                ", hasData=" + hasData +
                ", subReportCode=" + subReportCode +
                '}';
    }
}
