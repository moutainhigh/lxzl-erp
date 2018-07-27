package com.lxzl.erp.core.service.export;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.statement.StatementOrderDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statistics.FinanceStatisticsWeeklyParam;
import com.lxzl.erp.common.domain.statistics.StatisticsSalesmanPageParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author : liuyong
 * @Date : Created in 2018/7/20
 * @Time : Created in 11:42
 */
public interface ExportStatisticsFinanceDataWeeklyService {
    /**
    * 财务周数据统计导出
    * @Author : XiaoLuYu
    * @Date : Created in 2018/7/10 11:44
    */
    ServiceResult<String, String> exportStatisticsFinanceDataWeekly(FinanceStatisticsWeeklyParam financeStatisticsWeeklyParam, HttpServletResponse response);
    ServiceResult<String, String> exportStatisticsFinanceDataMonthly(FinanceStatisticsWeeklyParam financeStatisticsWeeklyParam, HttpServletResponse response);
}
;