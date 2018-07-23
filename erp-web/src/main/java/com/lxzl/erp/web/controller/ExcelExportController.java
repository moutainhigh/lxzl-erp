package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.statement.StatementOrderDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statistics.FinanceStatisticsWeeklyParam;
import com.lxzl.erp.common.domain.statistics.StatisticsSalesmanPageParam;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.export.DisposeExportDataService;
import com.lxzl.erp.core.service.export.ExportExcelCustomFormatService;
import com.lxzl.erp.core.service.export.ExportStatisticsFinanceDataWeeklyService;
import com.lxzl.se.common.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/exportExcel")
@Controller
@ControllerLog
public class ExcelExportController {

    @Autowired
    private ResultGenerator resultGenerator;
    @Autowired
    private DisposeExportDataService disposeExportDataService;
    @Autowired
    private ExportExcelCustomFormatService exportExcelCustomFormatService;
    @Autowired
    private ExportStatisticsFinanceDataWeeklyService exportStatisticsFinanceDataWeeklyService;

    @RequestMapping(value = "exportPageBankSlipDetail", method = RequestMethod.POST)
    public Result exportPageBankSlip(BankSlipDetailQueryParam bankSlipDetailQueryParam, HttpServletResponse response) throws Exception {
        return resultGenerator.generate(disposeExportDataService.disposePageBankSlipDetail(bankSlipDetailQueryParam,response).getErrorCode());
    }

    @RequestMapping(value = "exportStatementOrderDetail", method = RequestMethod.POST)
    public Result exportStatementOrderDetail(StatementOrderQueryParam statementOrderQueryParam, HttpServletResponse response) throws Exception {
        return resultGenerator.generate(disposeExportDataService.disposeStatementOrderDetail(statementOrderQueryParam, response).getErrorCode());
    }

    @RequestMapping(value = "exportStatisticsSalesmanDetail", method = RequestMethod.POST)
    public Result exportStatisticsSalesmanDetail(StatisticsSalesmanPageParam statisticsSalesmanPageParam, HttpServletResponse response) throws Exception {
        return resultGenerator.generate(disposeExportDataService.disposeStatisticsSalesmanDetail(statisticsSalesmanPageParam, response).getErrorCode());
    }

    @RequestMapping(value = "exportPageStatementOrder", method = RequestMethod.POST)
    public Result exportPageStatementOrder(StatementOrderDetailQueryParam statementOrderDetailQueryParam, HttpServletResponse response) throws Exception {
        return resultGenerator.generate(disposeExportDataService.disposePageStatementOrder(statementOrderDetailQueryParam, response).getErrorCode());
    }

    @RequestMapping(value = "exportDynamicSql", method = RequestMethod.POST)
    public Result exportDynamicSql(DynamicSqlSelectParam dynamicSqlSelectParam, HttpServletResponse response) throws Exception {
        return resultGenerator.generate(disposeExportDataService.disposeDynamicSql(dynamicSqlSelectParam, response).getErrorCode());
    }

    @RequestMapping(value = "exportStatementOrderCheck", method = RequestMethod.POST)
    public Result exportDynamicSql(StatementOrderMonthQueryParam statementOrderMonthQueryParam, HttpServletResponse response) throws Exception {
        ServiceResult<String, String> serviceResult = exportExcelCustomFormatService.queryStatementOrderCheckParam(statementOrderMonthQueryParam, response);
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

    @RequestMapping(value = "exportStatisticsFinanceDataWeekly", method = RequestMethod.POST)
    public Result exportStatisticsFinanceDataWeekly(FinanceStatisticsWeeklyParam param, HttpServletResponse response) throws Exception {
        ServiceResult<String, String> serviceResult = exportStatisticsFinanceDataWeeklyService.exportStatisticsFinanceDataWeekly(param, response);
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

}
