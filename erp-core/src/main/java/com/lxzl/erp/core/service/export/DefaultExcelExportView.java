package com.lxzl.erp.core.service.export;


import org.springframework.stereotype.Component;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/14
 * @Time : Created in 11:32
 */

@Component
public class DefaultExcelExportView implements ExcelExportView {

    private static DefaultExcelExportView defaultExcelExportView = null;
    @Override
    public Object view(Object o) {
        return o;
    }

    private DefaultExcelExportView(){}

    public static DefaultExcelExportView getInstance(){
        if(defaultExcelExportView ==null){
            defaultExcelExportView = new DefaultExcelExportView();
        }
        return defaultExcelExportView;
    }


}
