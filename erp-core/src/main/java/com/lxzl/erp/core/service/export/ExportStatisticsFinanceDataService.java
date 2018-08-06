package com.lxzl.erp.core.service.export;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statistics.FinanceStatisticsParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author : liuyong
 * @Date : Created in 2018/7/20
 * @Time : Created in 11:42
 */
public interface ExportStatisticsFinanceDataService {
    /**
    * 财务周数据统计导出
    * @Author : XiaoLuYu
    * @Date : Created in 2018/7/10 11:44
    */
    ServiceResult<String, String> exportStatisticsFinanceDataWeekly(FinanceStatisticsParam financeStatisticsParam, HttpServletResponse response);
    ServiceResult<String, String> exportStatisticsFinanceDataMonthly(FinanceStatisticsParam financeStatisticsParam, HttpServletResponse response);
    ServiceResult<String, String> exportStatisticsFinanceData(FinanceStatisticsParam financeStatisticsParam, HttpServletResponse response);
}
