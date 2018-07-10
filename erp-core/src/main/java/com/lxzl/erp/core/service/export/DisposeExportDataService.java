package com.lxzl.erp.core.service.export;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.statement.StatementOrderDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statistics.StatisticsSalesmanPageParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/10
 * @Time : Created in 11:42
 */
public interface DisposeExportDataService {
    /**
    * 处理银行流水详情分页导出数据
    * @Author : XiaoLuYu
    * @Date : Created in 2018/7/10 11:44
    */
    ServiceResult<String, String> disposePageBankSlipDetail(BankSlipDetailQueryParam bankSlipDetailQueryParam, HttpServletResponse response);
    /**
     * 处理结算单导出数据
     * @Author : XiaoLuYu
     * @Date : Created in 2018/7/10 11:44
     */
    ServiceResult<String, String> disposeStatementOrderDetail(StatementOrderQueryParam statementOrderQueryParam, HttpServletResponse response);
    /**
     * 处理统计业务员数据
     * @Author : XiaoLuYu
     * @Date : Created in 2018/7/10 11:44
     */
    ServiceResult<String, String> disposeStatisticsSalesmanDetail(StatisticsSalesmanPageParam statisticsSalesmanPageParam, HttpServletResponse response);
    /**
     * 处理结算单分页导出数据
     * @Author : XiaoLuYu
     * @Date : Created in 2018/7/10 11:44
     */
    ServiceResult<String, String> disposePageStatementOrder(StatementOrderDetailQueryParam statementOrderDetailQueryParam, HttpServletResponse response);
    /**
     * 处理动态sql导出数据
     * @Author : XiaoLuYu
     * @Date : Created in 2018/7/10 11:44
     */
    ServiceResult<String, String> disposeDynamicSql(DynamicSqlSelectParam dynamicSqlSelectParam, HttpServletResponse response)  throws Exception ;
}
