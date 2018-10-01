package com.lxzl.erp.core.service.export.impl.support;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.export.ColConfig;
import com.lxzl.erp.core.service.export.ExcelExportConfig;
import com.lxzl.erp.core.service.export.impl.ExcelExportServiceImpl;
import com.lxzl.se.common.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        hssSheet.setActive(false);//取消受保护的视图
        HSSFRow hssfRow = hssSheet.createRow(0);
        int count = 0;
        List<ColConfig> colConfigList = config.getConfigList();
        for (int i = 0; i < colConfigList.size(); i++) {
            ColConfig colConfig = colConfigList.get(i);
            hssfRow.createCell(count++).setCellValue(colConfig.getColName());
            hssSheet.setColumnWidth(i, colConfig.getWidth());
        }
        if (CollectionUtil.isNotEmpty(list)) {
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
        }

        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO_8859_1") + ".xls");
        response.setContentType("application/json;charset=utf-8");
        OutputStream stream = response.getOutputStream();
        hssfWorkbook.write(stream);
        stream.flush();
        stream.close();
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
    public static <T> HSSFWorkbook getXSSFWorkbook(T t, ExcelExportConfig config, String sheetName) throws Exception {
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
        if (t != null) {
            HSSFRow newXssfRow = hssSheet.createRow(1);
            for (int j = 0; j < colConfigList.size(); j++) {
                ColConfig colConfig = colConfigList.get(j);
                //开始set值到表里面
                String methodName = "get" + StringUtil.toUpperCaseFirstChar(colConfig.getFieldName());
                Method method = t.getClass().getMethod(methodName);
                Object value = method.invoke(t);
                hssSheet.setColumnWidth(j, colConfig.getWidth());
                Cell cell = newXssfRow.createCell(j);
                cell.setCellValue(String.valueOf(colConfig.getExcelExportView().view(value)));  //"充值订单id"
            }
        }

        return hssfWorkbook;
    }

    public static <T> HSSFWorkbook createHSSFSheetAttachToHSSFWorkbook(List<T> list, ExcelExportConfig config, String sheetName, HSSFWorkbook targetHssfWorkbook) throws Exception {
        if (targetHssfWorkbook == null) {
            targetHssfWorkbook = new HSSFWorkbook();
        }
        HSSFSheet hssSheet = targetHssfWorkbook.createSheet(sheetName);
        HSSFRow hssfRow = hssSheet.createRow(0);
        int count = 0;
        List<ColConfig> colConfigList = config.getConfigList();
        for (int i = 0; i < colConfigList.size(); i++) {
            ColConfig colConfig = colConfigList.get(i);
            hssfRow.createCell(count++).setCellValue(colConfig.getColName());
            hssSheet.setColumnWidth(i, colConfig.getWidth());
        }
        if (CollectionUtil.isNotEmpty(list)) {
            int row = 1;
            for (T t : list) {
                if (t != null) {
                    HSSFRow newXssfRow = hssSheet.createRow(row);
                    for (int j = 0; j < colConfigList.size(); j++) {
                        ColConfig colConfig = colConfigList.get(j);
                        //开始set值到表里面
                        String methodName = "get" + StringUtil.toUpperCaseFirstChar(colConfig.getFieldName());
                        Method method = t.getClass().getMethod(methodName);
                        Object value = method.invoke(t);
                        hssSheet.setColumnWidth(j, colConfig.getWidth());
                        Cell cell = newXssfRow.createCell(j);
                        cell.setCellValue(String.valueOf(colConfig.getExcelExportView().view(value)));
                    }
                    row++;
                }
            }
        }
        return targetHssfWorkbook;
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
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO_8859_1") + ".xls");
        response.setContentType("application/json;charset=utf-8");
        OutputStream stream = response.getOutputStream();
        hssfWorkbook.write(stream);
        stream.flush();
        stream.close();
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        return serviceResult;
    }

    /**
     * 乱码转换
     *
     * @param : param
     * @Author : XiaoLuYu
     * @Date : Created in 2018/5/4 17:46
     * @Return : java.lang.String
     */
    public static String decode(String param) {
        if (param != null) {
            try {
                return URLDecoder.decode(param, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


    public static String formatFileName(String fileName) {
        Date date = new Date();
        String now = new SimpleDateFormat("yyyyMMdd").format(date);
        return fileName + "__" + now;
    }

    /**
     * 导出设计表格
     *
     * @param : chargeRecordList
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/14 17:55
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.Object>
     */
    public static <T> ServiceResult<String, String> export(List<List<T>> list, String fileName, String sheetName, HttpServletResponse response, Integer width) throws Exception {
        ServiceResult<String, String> serviceResult = new ServiceResult<>();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet xssSheet = xssfWorkbook.createSheet(sheetName);

        if (CollectionUtil.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                List<T> rowList = list.get(i);
                XSSFRow newXssfRow = xssSheet.createRow(i);
                for (int j = 0; j < rowList.size(); j++) {
                    xssSheet.setColumnWidth(j, width);
                    Cell cell = newXssfRow.createCell(j);
                    cell.setCellValue(String.valueOf(rowList.get(j)));
                }
            }
        }
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO_8859_1") + ".xlsx");
        response.setContentType("application/json;charset=utf-8");
        OutputStream stream = response.getOutputStream();
        xssfWorkbook.write(stream);
        stream.flush();
        stream.close();
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
    public static <T> XSSFWorkbook getXSSFWorkbook(XSSFWorkbook hssfWorkbook, XSSFSheet hssSheet, List<T> list, ExcelExportConfig config, String sheetName, Integer rowNo, Integer headlineHeight, Integer rowHeight) throws Exception {
        XSSFRow hssfRow = hssSheet.createRow(rowNo);
        hssfRow.setHeightInPoints(headlineHeight);
        int count = 0;
        List<ColConfig> colConfigList = config.getConfigList();
        for (int i = 0; i < colConfigList.size(); i++) {
            ColConfig colConfig = colConfigList.get(i);
            XSSFCell hssfCell = hssfRow.createCell(count++);
            hssfCell.setCellValue(colConfig.getColName());
            hssSheet.setColumnWidth(i, colConfig.getWidth());
            setCellStyle(hssfWorkbook, hssfCell, colConfig.getHeadlineFontColor(), colConfig.getHeadlineBackGroupColor());
        }

        for (int i = 0; i < list.size(); i++) {
            XSSFRow newXssfRow = hssSheet.createRow(rowNo + i + 1);
            newXssfRow.setHeightInPoints(rowHeight);
            T t = list.get(i);
            for (int j = 0; j < colConfigList.size(); j++) {
                ColConfig colConfig = colConfigList.get(j);
                //开始set值到表里面
                String methodName = "get" + StringUtil.toUpperCaseFirstChar(colConfig.getFieldName());
                Method method = t.getClass().getMethod(methodName);
                Object value = method.invoke(t);
                hssSheet.setColumnWidth(j, colConfig.getWidth());
                Cell cell = newXssfRow.createCell(j);
                if (isNumeric(String.valueOf(colConfig.getExcelExportView().view(value)))) {
                    cell.setCellValue(Double.parseDouble(String.valueOf(colConfig.getExcelExportView().view(value))));
                } else {
                    cell.setCellValue(String.valueOf(colConfig.getExcelExportView().view(value)));  //"充值订单id"
                }

                setCellStyle(hssfWorkbook, cell, colConfig.getFontColor(), colConfig.getBackGroupColor());
            }
        }
        return hssfWorkbook;
    }

    //方法四：
    public final static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[-\\\\+]?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    public static void setCellStyle(Workbook hssfWorkbook, Cell cell, short fontColor, short backGroupColor) {
        CellStyle style = hssfWorkbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setWrapText(true);// 自动换行
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(backGroupColor);//背景颜色
        //设置边框

        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setRightBorderColor(HSSFColor.BLACK.index);

        Font font = hssfWorkbook.createFont();
        font.setColor(fontColor); //字体颜色
        font.setFontHeightInPoints((short) 9);
        style.setFont(font);
        cell.setCellStyle(style);
    }


    /**
     * 获取导出对账单的单元格的样式map
     */
    public static Map<String, CellStyle> getImportCheckStatementCellStyleMap(Workbook sxssfWorkbook, ExcelExportConfig excelExportConfig) {
        Map<String, CellStyle> importCheckStatementCellStyleMap = new HashMap<>();
        for (ColConfig colConfig : excelExportConfig.getConfigList()) {
            String fontCellStyleKey = getImportCheckStatementCellStyleKey(colConfig);
            if (!importCheckStatementCellStyleMap.containsKey(fontCellStyleKey)) {
                importCheckStatementCellStyleMap.put(fontCellStyleKey, getCellStyle(sxssfWorkbook, colConfig.getFontColor(), colConfig.getBackGroupColor()));
            }
            String titleKey = getImportCheckStatementCellStyleTitleKey(colConfig);
            if (!importCheckStatementCellStyleMap.containsKey(titleKey)) {
                importCheckStatementCellStyleMap.put(titleKey, getCellStyle(sxssfWorkbook, colConfig.getHeadlineFontColor(), colConfig.getHeadlineBackGroupColor()));
            }
        }
        return importCheckStatementCellStyleMap;
    }


    public static Workbook buildSXSSFWorkbook(Workbook sxssfWorkbook, Map<String, CellStyle> stringCellStyleMap, Sheet sheet, CheckStatementStatisticsDTO checkStatementStatisticsDTO, ExcelExportConfig config, final Integer rowNo, Integer headlineHeight, Integer rowHeight) throws Exception {
        List<ColConfig> colConfigList = config.getConfigList();
        int rowNoTemp = rowNo;
        Row row = sheet.createRow(rowNoTemp);
        row.setHeightInPoints(headlineHeight);
        for (int i = 0; i < colConfigList.size(); i++) {
            ColConfig colConfig = colConfigList.get(i);
            // 初始化导出对账单的cell样式
            Cell cell = row.createCell(i);
            cell.setCellValue(colConfig.getColName());
            sheet.setColumnWidth(i, colConfig.getWidth());
            cell.setCellStyle(stringCellStyleMap.get(getImportCheckStatementCellStyleTitleKey(colConfig)));
        }
        List<BaseCheckStatementDetailDTO> checkStatementDetailDTOS = checkStatementStatisticsDTO.getCheckStatementDetailDTOS();
        rowNoTemp++;
        for (BaseCheckStatementDetailDTO checkStatementDetailDTO : checkStatementDetailDTOS) {
            Row dataRow = sheet.createRow(rowNoTemp++);
            dataRow.setHeightInPoints(rowHeight);
            for (int i = 0; i < colConfigList.size(); i++) {
                ColConfig colConfig = colConfigList.get(i);
                Cell cell = dataRow.createCell(i);
                sheet.setColumnWidth(i, colConfig.getWidth());
                Object value = checkStatementDetailDTO.getImportData().get(colConfig.getFieldName());
                if (value != null) {
                    generateValue(value, cell);
                }
                cell.setCellStyle(stringCellStyleMap.get(getImportCheckStatementCellStyleKey(colConfig)));
            }

        }
        return sxssfWorkbook;
    }

    public static CellStyle getCellStyle(Workbook hssfWorkbook, short fontColor, short backGroupColor) {
        CellStyle style = hssfWorkbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setWrapText(true);// 自动换行
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(backGroupColor);//背景颜色
        //设置边框

        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setRightBorderColor(HSSFColor.BLACK.index);

        Font font = hssfWorkbook.createFont();
        font.setColor(fontColor); //字体颜色
        font.setFontHeightInPoints((short) 9);
        style.setFont(font);
        return style;
    }

    /**
     * 获取导出对账单单元样式的key
     */
    public static String getImportCheckStatementCellStyleKey(ColConfig colConfig) {
        return colConfig.getFontColor() + "_" + colConfig.getBackGroupColor();
    }

    /**
     * 获取导出对账单单元样式title的key
     */
    public static String getImportCheckStatementCellStyleTitleKey(ColConfig colConfig) {
        return "title" + colConfig.getHeadlineFontColor() + "_" + colConfig.getHeadlineBackGroupColor();
    }

    public static void generateValue(Object value, Cell cell) {
        if (isNumeric(value.toString())) {
            cell.setCellValue(Double.parseDouble(value.toString()));
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof RichTextString) {
            cell.setCellValue((RichTextString) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        }
    }
}
