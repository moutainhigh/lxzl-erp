package com.lxzl.erp.core.service.export;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/14
 * @Time : Created in 11:32
 */

@Component
public class DateExcelExportView implements ExcelExportView {

    private static DateExcelExportView defaultExcelExportView = null;
    @Override
    public Object view(Object o) {
        return transitionDate(o);
    }

    private DateExcelExportView(){}

    public static DateExcelExportView getInstance(){
        if(defaultExcelExportView ==null){
            defaultExcelExportView = new DateExcelExportView();
        }
        return defaultExcelExportView;
    }
    private Object transitionDate(Object o) {
        if(o != null){
            Object date = ExcelExportConfigGroup.formatDate(o);
            return date;
        }
        return "";
    }

}
