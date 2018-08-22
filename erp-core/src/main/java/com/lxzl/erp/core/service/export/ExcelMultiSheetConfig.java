package com.lxzl.erp.core.service.export;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: liuyong
 * Date: 2018/8/10
 * Time: 17:48
 */
public class ExcelMultiSheetConfig<T> {
        private List<T>  baseData = null;
        private ExcelExportConfig excelExportConfig = null;
        private String sheetName = null;

    public ExcelMultiSheetConfig(List<T> baseData, ExcelExportConfig excelExportConfig, String sheetName) {
        this.baseData = baseData;
        this.excelExportConfig = excelExportConfig;
        this.sheetName = sheetName;
    }

    public List<T> getBaseData() {
            return baseData;
        }

        public void setBaseData(List<T> baseData) {
            this.baseData = baseData;
        }

        public ExcelExportConfig getExcelExportConfig() {
            return excelExportConfig;
        }

        public void setExcelExportConfig(ExcelExportConfig excelExportConfig) {
            this.excelExportConfig = excelExportConfig;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }
}
