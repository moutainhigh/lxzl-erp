package com.lxzl.erp.core.service.export.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.core.service.export.ExcelExportConfig;
import com.lxzl.erp.core.service.export.ExcelExportService;
import com.lxzl.erp.core.service.export.impl.support.ExcelExportSupport;
import com.lxzl.se.common.domain.Result;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/14
 * @Time : Created in 16:43
 */
@Service
public class ExcelExportServiceImpl<T> implements ExcelExportService<T> {

    @Override
    public ServiceResult<String, String> export(ServiceResult<String, Page<T>> result, ExcelExportConfig config, String fileName, String sheetName, HttpServletResponse response) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        try {
            if (ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                //导出设计表格
                serviceResult = ExcelExportSupport.export(result.getResult().getItemList(), config, ExcelExportSupport.formatFileName(fileName), sheetName, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResult;
    }


    @Override
    public ServiceResult<String, String> export(List<T> list, ExcelExportConfig config, HttpServletResponse response, HSSFWorkbook hssfWorkbook, String fileName, String sheetName, Integer row) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        try {
            //导出设计表格
            serviceResult = ExcelExportSupport.export(list, config,response,hssfWorkbook,ExcelExportSupport.formatFileName(fileName),sheetName,row);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<String, HSSFWorkbook> getHSSFWorkbook(ServiceResult<String, Page<T>> result, ExcelExportConfig config,String sheetName) {
        ServiceResult<String, HSSFWorkbook> serviceResult = new ServiceResult<>();
        try {
            if (ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                //导出设计表格
                HSSFWorkbook hssfWorkbook = ExcelExportSupport.getXSSFWorkbook(result.getResult(), config,sheetName);
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                serviceResult.setResult(hssfWorkbook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> export(List<T> list, ExcelExportConfig config, String fileName, String sheetName, HttpServletResponse response) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        try {
            //导出设计表格
            serviceResult = ExcelExportSupport.export(list, config, ExcelExportSupport.formatFileName(fileName), sheetName,response);
            return serviceResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> export(Result result, String fileName, String sheetName, HttpServletResponse response, Integer width) {
        ServiceResult<String,String> serviceResult = new ServiceResult<>();
        Object data = result.getResultMap().get("data");
        List<List<Object>> list = null;
        if(data != null){
            list = (List<List<Object>>) data;
        }

        try {
            //导出设计表格
            serviceResult = ExcelExportSupport.export(list, ExcelExportSupport.formatFileName(fileName), sheetName,response,width);
            return serviceResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<String, XSSFWorkbook> getXSSFWorkbook(XSSFWorkbook hssfWorkbook, XSSFSheet hssSheet, List<T> list, ExcelExportConfig config, String sheetName, Integer rowNo, Integer headlineHeight, Integer rowHeight) {
        ServiceResult<String, XSSFWorkbook> serviceResult = new ServiceResult<>();
        try {
            //导出设计表格
            XSSFWorkbook newHSSFWorkbook = ExcelExportSupport.getXSSFWorkbook(hssfWorkbook,hssSheet,list, config,sheetName,rowNo,headlineHeight,rowHeight);
            serviceResult.setErrorCode(ErrorCode.SUCCESS);
            serviceResult.setResult(newHSSFWorkbook);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResult;
    }

}
