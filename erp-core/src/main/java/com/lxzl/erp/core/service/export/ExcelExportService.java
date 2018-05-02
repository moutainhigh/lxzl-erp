package com.lxzl.erp.core.service.export;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/14
 * @Time : Created in 15:58
 */
public interface ExcelExportService<T> {
    ServiceResult<String,String> export(ServiceResult<String, Page<T>> result,ExcelExportConfig config, String fileName, String sheetName,HttpServletResponse response);
    ServiceResult<String,String> export(List<T> list, ExcelExportConfig config,HttpServletResponse response,XSSFWorkbook xssfWorkbook,String fileName,String sheetName,Integer row);
    ServiceResult<String, XSSFWorkbook> getXSSFWorkbook(ServiceResult<String, Page<T>> result, ExcelExportConfig config,String sheetName);
}
