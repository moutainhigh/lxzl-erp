package com.lxzl.erp.core.service.export.impl.support;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.core.service.export.ColConfig;
import com.lxzl.erp.core.service.export.ExcelExportConfig;
import com.lxzl.erp.core.service.export.impl.ExcelExportServiceImpl;
import com.lxzl.se.common.util.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/20
 * @Time : Created in 19:20
 */
public class ExcelExportSupport<T> {
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportServiceImpl.class);

    /**
     * 导出设计表格
     *
     * @param : chargeRecordList
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/14 17:55
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     */
    public static  <T> ServiceResult<String, String> export(List<T> list, ExcelExportConfig config, String fileName, String sheetName, HttpServletResponse response) throws Exception {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet xssSheet = xssfWorkbook.createSheet(sheetName);
        XSSFRow xssfRow = xssSheet.createRow(0);
        int count = 0;
        List<ColConfig> colConfigList = config.getConfigList();
        for (ColConfig colConfig : colConfigList) {
            xssfRow.createCell(count++).setCellValue(colConfig.getColName());
        }

        for (int i = 0; i < list.size(); i++) {
            XSSFRow newXssfRow = xssSheet.createRow(i + 1);
            Object o = list.get(i);
            for (int j = 0; j < colConfigList.size(); j++) {
                ColConfig colConfig = colConfigList.get(j);
                //开始set值到表里面
                String methodName = "get" + StringUtil.toUpperCaseFirstChar(colConfig.getFieldName());
                Method method = o.getClass().getMethod(methodName);
                Object value = method.invoke(o);
                if (j == 0) {
                    xssSheet.setColumnWidth(j, colConfig.getWidth());
                }
                Cell cell = newXssfRow.createCell(j);
                cell.setCellValue(String.valueOf(colConfig.getExcelExportView().view(value)));  //"充值订单id"
            }
        }

        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
        response.setContentType("application/json;charset=utf-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xssfWorkbook.write(outputStream);
        OutputStream stream = response.getOutputStream();
        outputStream.flush();
        outputStream.close();
        xssfWorkbook.write(stream);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }
}
