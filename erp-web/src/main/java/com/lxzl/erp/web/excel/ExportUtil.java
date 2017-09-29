package com.lxzl.erp.web.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lxzl.se.common.exception.SystemException;


public class ExportUtil {
	
	public static enum ExcelType {XLS, XLSX}

	public static void exportToOutputStream(List<ExportCellData> cellDataList,ExcelType xlsx, OutputStream outputStream) throws IOException {
		createExport(cellDataList, xlsx).write(outputStream);
    }
	
    public static Workbook createExport(List<ExportCellData> cellDataList,ExcelType xlsx){
    	
    	Workbook workbook;
    	
    	if (ExcelType.XLS.equals(xlsx)) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook() ;
        }
    	
    	CellStyle cellHeadStyle = createHeadStyle(workbook);
    	CellStyle cellDataStyle = createDataStyle(workbook);
    	
    	Sheet sheet = workbook.createSheet();
    	
    	createRowAndCell(cellDataList,sheet,cellHeadStyle,cellDataStyle);
    	
    	return workbook;
    }
    
    
    private static void createRowAndCell(List<ExportCellData> cellDataList,Sheet sheet, CellStyle cellHeadStyle,CellStyle cellDataStyle){
    	for(ExportCellData cellData:cellDataList){
    		Row row =sheet.getRow(cellData.getRow());
    		if(row==null){
    			row = sheet.createRow(cellData.getRow());
    		}
    		if(cellData.getEndRow()!=null&&cellData.getEndCol()!=null){
    			if(cellData.getEndRow()<cellData.getRow()||cellData.getEndCol()<cellData.getCol()){
        			throw new SystemException(String.format("栏位%s(%s,%s)合并行列(%s,%s)错误",cellData.getValue(),cellData.getRow(),cellData.getCol(),cellData.getEndRow(),cellData.getEndCol()));
        		}else{
        			sheet.addMergedRegion(new CellRangeAddress(cellData.getRow(),cellData.getEndRow(),cellData.getCol(),cellData.getEndCol()));
        		}
    		}
    		Cell cell = row.createCell(cellData.getCol());
    		cell.setCellValue(formatToString(cellData.getValue()));
    		if(cellData.getStyle()==1){
    			cell.setCellStyle(cellHeadStyle);
    		}else{
    			cell.setCellStyle(cellDataStyle);
    		}
    	}
    }
    
    private static CellStyle createHeadStyle(Workbook workbook){
    	CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("新宋体");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平    
        style.setFont(font);
      
        return style;
    } 
    
    private static CellStyle createDataStyle(Workbook workbook){
    	 CellStyle style = workbook.createCellStyle();
         Font font = workbook.createFont();
         font.setFontHeightInPoints((short) 12);
         font.setFontName("新宋体");
         style.setFont(font);
        return style;
    }
    
    private static String formatToString(Object object) {
        if (object == null) {
            return "";
        } else if (object instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) object);
        } else {
            return object.toString();
        }
    }
    
    public static void main(String[] args) {
      
    	List<ExportCellData> list = new ArrayList<ExportCellData>();
    	list.add(new ExportCellData(0,0,"a0",1));
    	list.add(new ExportCellData(0,1,"a1"));
    	list.add(new ExportCellData(0,2,"a2"));
    	list.add(new ExportCellData(0,3,"a3"));    	
    	list.add(new ExportCellData(1,3,"a3",0,1,2));
    	
        Workbook workbook = createExport(list,ExcelType.XLSX);
        try {
            workbook.write(new FileOutputStream("e:\\test.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
