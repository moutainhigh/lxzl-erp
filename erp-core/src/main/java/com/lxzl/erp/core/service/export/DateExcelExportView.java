package com.lxzl.erp.core.service.export;


import org.springframework.stereotype.Component;

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
            Object date = formatDate(o);
            return date;
        }
        return "";
    }
    private Object formatDate(Object o) {
        if (o != null) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
            try {
                return sdf2.format(sdf1.parse(o.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
