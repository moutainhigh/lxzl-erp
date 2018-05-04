package com.lxzl.erp.core.service.export.impl.support;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.export.ColConfig;
import com.lxzl.erp.core.service.export.ExcelExportConfig;
import com.lxzl.erp.core.service.export.impl.ExcelExportServiceImpl;
import com.lxzl.se.common.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public static <T> ServiceResult<String, String> export(List<T> list, ExcelExportConfig config, String fileName, String sheetName, HttpServletResponse response) throws Exception {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssSheet = hssfWorkbook.createSheet(sheetName);
        HSSFRow hssfRow = hssSheet.createRow(0);
        int count = 0;
        List<ColConfig> colConfigList = config.getConfigList();
        for (int i = 0; i < colConfigList.size(); i++) {
            ColConfig colConfig = colConfigList.get(i);
            hssfRow.createCell(count++).setCellValue(colConfig.getColName());
            hssSheet.setColumnWidth(i, colConfig.getWidth());
        }

        for (int i = 0; i < list.size(); i++) {
            HSSFRow newXssfRow = hssSheet.createRow(i + 1);
            Object o = list.get(i);
            for (int j = 0; j < colConfigList.size(); j++) {
                ColConfig colConfig = colConfigList.get(j);
                //开始set值到表里面
                String methodName = "get" + StringUtil.toUpperCaseFirstChar(colConfig.getFieldName());
                Method method = o.getClass().getMethod(methodName);
                Object value = method.invoke(o);
                hssSheet.setColumnWidth(j, colConfig.getWidth());
                Cell cell = newXssfRow.createCell(j);
                cell.setCellValue(String.valueOf(colConfig.getExcelExportView().view(value)));  //"充值订单id"
            }
        }

        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO_8859_1") + ".xls");
        response.setContentType("application/json;charset=utf-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        hssfWorkbook.write(outputStream);
        OutputStream stream = response.getOutputStream();
        outputStream.flush();
        outputStream.close();
        hssfWorkbook.write(stream);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }


    /**
     * 导出设计表格
     *
     * @param : chargeRecordList
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/14 17:55
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     */
    public static <T> HSSFWorkbook getXSSFWorkbook(List<T> list, ExcelExportConfig config, String sheetName) throws Exception {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssSheet = hssfWorkbook.createSheet(sheetName);
        HSSFRow hssfRow = hssSheet.createRow(0);
        int count = 0;
        List<ColConfig> colConfigList = config.getConfigList();
        for (int i = 0; i < colConfigList.size(); i++) {
            ColConfig colConfig = colConfigList.get(i);
            hssfRow.createCell(count++).setCellValue(colConfig.getColName());
            hssSheet.setColumnWidth(i, colConfig.getWidth());
        }

        for (int i = 0; i < list.size(); i++) {
            HSSFRow newXssfRow = hssSheet.createRow(i + 1);
            Object o = list.get(i);
            for (int j = 0; j < colConfigList.size(); j++) {
                ColConfig colConfig = colConfigList.get(j);
                //开始set值到表里面
                String methodName = "get" + StringUtil.toUpperCaseFirstChar(colConfig.getFieldName());
                Method method = o.getClass().getMethod(methodName);
                Object value = method.invoke(o);
                hssSheet.setColumnWidth(j, colConfig.getWidth());
                Cell cell = newXssfRow.createCell(j);
                cell.setCellValue(String.valueOf(colConfig.getExcelExportView().view(value)));  //"充值订单id"
            }
        }
        return hssfWorkbook;
    }

    /**
     * 导出带list数据表格
     *
     * @param : chargeRecordList
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/14 17:55
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     */
    public static <T> ServiceResult<String, String> export(List<T> list, ExcelExportConfig config, HttpServletResponse response, HSSFWorkbook hssfWorkbook, String fileName, String sheetName, Integer row) throws Exception {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        HSSFSheet hssSheet = hssfWorkbook.getSheet(sheetName);
        HSSFRow hssfRow = hssSheet.createRow(row);
        int count = 0;
        List<ColConfig> colConfigList = config.getConfigList();
        for (ColConfig colConfig : colConfigList) {
            hssfRow.createCell(count++).setCellValue(colConfig.getColName());
        }
        if (CollectionUtil.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                HSSFRow newHssfRow = hssSheet.createRow(i + row + 1);
                Object o = list.get(i);
                for (int j = 0; j < colConfigList.size(); j++) {
                    ColConfig colConfig = colConfigList.get(j);
                    //开始set值到表里面
                    String methodName = "get" + StringUtil.toUpperCaseFirstChar(colConfig.getFieldName());
                    Method method = o.getClass().getMethod(methodName);
                    Object value = method.invoke(o);
                    if (j == 0) {
                        hssSheet.setColumnWidth(j, colConfig.getWidth());
                    }
                    Cell cell = newHssfRow.createCell(j);
                    cell.setCellValue(String.valueOf(colConfig.getExcelExportView().view(value)));  //"充值订单id"
                }
            }
        }

        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
        response.setContentType("application/json;charset=utf-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        hssfWorkbook.write(outputStream);
        OutputStream stream = response.getOutputStream();
        outputStream.flush();
        outputStream.close();
        hssfWorkbook.write(stream);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }
    /**
    * 乱码转换
    * @Author : XiaoLuYu
    * @Date : Created in 2018/5/4 17:46
    * @param : param
    * @Return : java.lang.String
    */
    public static String decode(String param){
        if(param != null){
            try {
                return URLDecoder.decode(param, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


    public static String formatFileName(String fileName){
        Date date = new Date();
        String now = new SimpleDateFormat("yyyyMMdd").format(date);
        return fileName+"__"+now;
    }

}
