package com.lxzl.erp.core.service.export.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.core.service.export.ExcelExportConfig;
import com.lxzl.erp.core.service.export.ExcelExportService;
import com.lxzl.erp.core.service.export.impl.support.ExcelExportSupport;
import com.lxzl.erp.dataaccess.domain.bank.BankSlipDetailDO;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
                serviceResult = ExcelExportSupport.export(result.getResult().getItemList(), config, fileName, sheetName, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResult;
    }


    @Override
    public ServiceResult<String, String> export(List<T> list, ExcelExportConfig config,HttpServletResponse response, XSSFWorkbook xssfWorkbook,String fileName,String sheetName,Integer row) {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        try {
            //导出设计表格
            serviceResult = ExcelExportSupport.export(list, config,response,xssfWorkbook,fileName,sheetName,row);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<String, XSSFWorkbook> getXSSFWorkbook(ServiceResult<String, Page<T>> result, ExcelExportConfig config,String sheetName) {
        ServiceResult<String, XSSFWorkbook> serviceResult = new ServiceResult<>();
        try {
            if (ErrorCode.SUCCESS.equals(result.getErrorCode())) {
                //导出设计表格
                XSSFWorkbook xssfWorkbook = ExcelExportSupport.getXSSFWorkbook(result.getResult().getItemList(), config,sheetName);
                serviceResult.setErrorCode(ErrorCode.SUCCESS);
                serviceResult.setResult(xssfWorkbook);
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
            serviceResult = ExcelExportSupport.export(list, config, fileName, sheetName,response);
            return serviceResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResult;
    }
}
