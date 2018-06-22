package com.lxzl.erp.core.service.export;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/20
 * @Time : Created in 20:21
 */
public class ColConfig {

    private static Integer DEFAULT_WIDTH = 4000;
    private static Short DEFAULT_FONT_COLOR = HSSFColor.BLACK.index;
    private static Short DEFAULT_BACK_GROUP_COLOR = HSSFColor.WHITE.index;
    private static Short HEAD_LINE_DEFAULT_FONT_COLOR = HSSFColor.WHITE.index;
    private static Short HEAD_LINE_DEFAULT_BACK_GROUP_COLOR = HSSFColor.GREY_80_PERCENT.index;
    private String fieldName;
    private String colName;
    private Integer width;
    private ExcelExportView excelExportView;
    private Short fontColor;
    private Short backGroupColor;
    private Short headlineFontColor;
    private Short headlineBackGroupColor;

    public Short getHeadlineFontColor() {
        return headlineFontColor;
    }

    public void setHeadlineFontColor(Short headlineFontColor) {
        this.headlineFontColor = headlineFontColor;
    }

    public Short getHeadlineBackGroupColor() {
        return headlineBackGroupColor;
    }

    public void setHeadlineBackGroupColor(Short headlineBackGroupColor) {
        this.headlineBackGroupColor = headlineBackGroupColor;
    }

    public Short getFontColor() {
        return fontColor;
    }

    public void setFontColor(Short fontColor) {
        this.fontColor = fontColor;
    }

    public Short getBackGroupColor() {
        return backGroupColor;
    }

    public void setBackGroupColor(Short backGroupColor) {
        this.backGroupColor = backGroupColor;
    }

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


    public ColConfig(String fieldName, String colName, Integer width, ExcelExportView excelExportView){
        this(fieldName,colName,width,excelExportView,DEFAULT_FONT_COLOR,DEFAULT_BACK_GROUP_COLOR,HEAD_LINE_DEFAULT_FONT_COLOR,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }

    public ColConfig(String fieldName, String colName, Integer width,Short backGroupColor,Short headlineFontColor,Short headlineBackGroupColor, ExcelExportView excelExportView){
        this(fieldName,colName,width,excelExportView,DEFAULT_FONT_COLOR,backGroupColor,headlineFontColor,headlineBackGroupColor);
    }

    public ColConfig(String fieldName, String colName, Integer width,Short backGroupColor,Short headlineFontColor, ExcelExportView excelExportView){
        this(fieldName,colName,width,excelExportView,DEFAULT_FONT_COLOR,backGroupColor,headlineFontColor,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }

    public ColConfig(String fieldName, String colName, Integer width,Short backGroupColor, ExcelExportView excelExportView){
        this(fieldName,colName,width,excelExportView,DEFAULT_FONT_COLOR,backGroupColor,HEAD_LINE_DEFAULT_FONT_COLOR,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }
    public ColConfig(String fieldName, String colName, Integer width, ExcelExportView excelExportView,Short fontColor,Short backGroupColor,Short headlineFontColor,Short headlineBackGroupColor) {
        this.fieldName = fieldName;
        this.colName = colName;
        this.width = width*256+184;
        this.excelExportView = excelExportView;
        this.fontColor = fontColor;
        this.backGroupColor = backGroupColor;
        this.headlineFontColor = headlineFontColor;
        this.headlineBackGroupColor = headlineBackGroupColor;
    }
    public ColConfig(String fieldName, String colName, ExcelExportView excelExportView) {
        this(fieldName,colName,DEFAULT_WIDTH,excelExportView,DEFAULT_FONT_COLOR,DEFAULT_BACK_GROUP_COLOR,HEAD_LINE_DEFAULT_FONT_COLOR,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }
    public ColConfig(String fieldName, String colName) {
        this(fieldName,colName,DEFAULT_WIDTH,DefaultExcelExportView.getInstance(),DEFAULT_FONT_COLOR,DEFAULT_BACK_GROUP_COLOR,HEAD_LINE_DEFAULT_FONT_COLOR,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }
    public ColConfig(String fieldName, String colName, Integer width) {
        this(fieldName,colName,width,DefaultExcelExportView.getInstance(),DEFAULT_FONT_COLOR,DEFAULT_BACK_GROUP_COLOR,HEAD_LINE_DEFAULT_FONT_COLOR,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }
    public ColConfig(String fieldName, String colName, ExcelExportView excelExportView,Short fontColor,Short backGroupColor) {
        this(fieldName,colName,DEFAULT_WIDTH,excelExportView,fontColor,backGroupColor,HEAD_LINE_DEFAULT_FONT_COLOR,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }
    public ColConfig(String fieldName, String colName,Short fontColor,Short backGroupColor) {
        this(fieldName,colName,DEFAULT_WIDTH,DefaultExcelExportView.getInstance(),fontColor,backGroupColor,HEAD_LINE_DEFAULT_FONT_COLOR,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }
    public ColConfig(String fieldName, String colName, Integer width,Short backGroupColor) {
        this(fieldName,colName,width,DefaultExcelExportView.getInstance(),DEFAULT_FONT_COLOR,backGroupColor,HEAD_LINE_DEFAULT_FONT_COLOR,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }

    public ColConfig(String fieldName, String colName,Short backGroupColor) {
        this(fieldName,colName,DEFAULT_WIDTH,DefaultExcelExportView.getInstance(),DEFAULT_FONT_COLOR,backGroupColor,HEAD_LINE_DEFAULT_FONT_COLOR,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }

    public ColConfig(String fieldName, String colName,Short backGroupColor,ExcelExportView excelExportView) {
        this(fieldName,colName,DEFAULT_WIDTH,excelExportView,DEFAULT_FONT_COLOR,backGroupColor,HEAD_LINE_DEFAULT_FONT_COLOR,HEAD_LINE_DEFAULT_BACK_GROUP_COLOR);
    }
}
