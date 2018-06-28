package com.lxzl.erp.core.service.export;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.se.common.domain.Result;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/6/19
 * @Time : Created in 14:46
 */
public interface ExportExcelCustomFormatService {
    /**
    * 自定义对账单导出
    * @Author : XiaoLuYu
    * @Date : Created in 2018/6/19 14:50
    */
    ServiceResult<String, String> queryStatementOrderCheckParam(StatementOrderMonthQueryParam statementOrderMonthQueryParam, HttpServletResponse response) throws Exception;
}
