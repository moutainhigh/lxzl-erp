package com.lxzl.erp.core.service.export;


import com.lxzl.erp.common.util.BigDecimalUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/14
 * @Time : Created in 11:32
 */

@Component
public class AmountExcelExportView implements ExcelExportView {

    private static AmountExcelExportView defaultExcelExportView = null;
    @Override
    public Object view(Object o) {
        return formatBigDecimal(o);
    }

    private AmountExcelExportView(){}

    public static AmountExcelExportView getInstance(){
        if(defaultExcelExportView ==null){
            defaultExcelExportView = new AmountExcelExportView();
        }
        return defaultExcelExportView;
    }
    private Object formatBigDecimal(Object o) {
        if(o != null){
            BigDecimal receive = new BigDecimal(o.toString());
            receive = BigDecimalUtil.round(receive,BigDecimalUtil.STANDARD_SCALE);
            return receive;
        }
        return "0.00";
    }

}
