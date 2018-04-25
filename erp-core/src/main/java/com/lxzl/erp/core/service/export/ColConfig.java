package com.lxzl.erp.core.service.export;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/20
 * @Time : Created in 20:21
 */
public class ColConfig {

    private static Integer DEFAULT_WIDTH = 4000;
    private String fieldName;
    private String colName;
    private Integer width = DEFAULT_WIDTH;
    private ExcelExportView excelExportView;



    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public ExcelExportView getExcelExportView() {
        return excelExportView;
    }

    public void setExcelExportView(ExcelExportView excelExportView) {
        this.excelExportView = excelExportView;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ColConfig(String fieldName, String colName, Integer width, ExcelExportView excelExportView) {
        this.fieldName = fieldName;
        this.colName = colName;
        this.width = width;
        this.excelExportView = excelExportView;
    }
    public ColConfig(String fieldName, String colName, ExcelExportView excelExportView) {
        this.fieldName = fieldName;
        this.colName = colName;
        this.excelExportView = excelExportView;
        new ColConfig(fieldName,colName,DEFAULT_WIDTH,excelExportView);
    }
    public ColConfig(String fieldName, String colName) {
        this.fieldName = fieldName;
        this.colName = colName;
        this.excelExportView = DefaultExcelExportView.getInstance();
        new ColConfig(fieldName,colName,DEFAULT_WIDTH,excelExportView);
    }
    public ColConfig(String fieldName, String colName, Integer width) {
        this.fieldName = fieldName;
        this.colName = colName;
        this.excelExportView = DefaultExcelExportView.getInstance();
        new ColConfig(fieldName,colName,width,excelExportView);
    }
}
