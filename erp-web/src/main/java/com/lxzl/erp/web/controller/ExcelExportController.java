package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.domain.statistics.StatisticsSalesmanPageParam;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsSalesman;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.export.ExcelExportConfigGroup;
import com.lxzl.erp.core.service.export.ExcelExportService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.se.common.domain.Result;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/exportExcel")
@Controller
@ControllerLog
public class ExcelExportController {

    @Autowired
    private ResultGenerator resultGenerator;
    @Autowired
    private BankSlipService bankSlipService;
    @Autowired
    private StatementService statementService;
    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = "exportPageBankSlipDetail", method = RequestMethod.POST)
    public Result exportPageBankSlip(BankSlipDetailQueryParam bankSlipDetailQueryParam, HttpServletResponse response) throws Exception {
        if(bankSlipDetailQueryParam.getPayerName() != null){
            bankSlipDetailQueryParam.setPayerName(URLDecoder.decode(bankSlipDetailQueryParam.getPayerName(), "UTF-8"));
        }
        ServiceResult<String, Page<BankSlipDetail>> stringPageServiceResult = bankSlipService.pageBankSlipDetail(bankSlipDetailQueryParam);
        ServiceResult<String, String> serviceResult = excelExportService.export(stringPageServiceResult, ExcelExportConfigGroup.bankSlipDetailConfig, "bankSlipDetail", "sheet1", response);
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

    @RequestMapping(value = "exportStatementOrderPage", method = RequestMethod.POST)
    public Result exportStatementOrderPage(StatementOrderQueryParam statementOrderQueryParam, HttpServletResponse response) throws Exception {
        if(statementOrderQueryParam.getStatementOrderCustomerName() != null){
            statementOrderQueryParam.setStatementOrderCustomerName(URLDecoder.decode(statementOrderQueryParam.getStatementOrderCustomerName(), "UTF-8"));
        }
        if(statementOrderQueryParam.getOwnerName() != null){
            statementOrderQueryParam.setOwnerName(URLDecoder.decode(statementOrderQueryParam.getOwnerName(), "UTF-8"));
        }
        ServiceResult<String, Page<StatementOrder>> serviceResult = statementService.queryStatementOrder(statementOrderQueryParam);
        ServiceResult<String, HSSFWorkbook> result = excelExportService.getHSSFWorkbook(serviceResult, ExcelExportConfigGroup.statementOrderConfig, "sheet1");
        List<StatementOrder> statementOrderList = serviceResult.getResult().getItemList();
        List<StatementOrderDetail> statementOrderDetailList = new ArrayList<>();
        for (StatementOrder statementOrder : statementOrderList) {
            ServiceResult<String, StatementOrder> serviceResult1 = statementService.queryStatementOrderDetail(statementOrder.getStatementOrderNo());
            statementOrderDetailList.addAll(serviceResult1.getResult().getStatementOrderDetailList());
        }

        ServiceResult<String, String> serviceResult1 = excelExportService.export(statementOrderDetailList, ExcelExportConfigGroup.statementOrderDetailConfig, response, result.getResult(), "statementOrder", "sheet1", serviceResult.getResult().getItemList().size()+1);
        return resultGenerator.generate(serviceResult1.getErrorCode(), serviceResult1.getResult());
    }

    @RequestMapping(value = "exportStatisticsSalesmanDetail", method = RequestMethod.POST)
    public Result exportStatisticsSalesmanDetail(StatisticsSalesmanPageParam statisticsSalesmanPageParam, HttpServletResponse response) throws Exception {

        if(statisticsSalesmanPageParam.getSalesmanName() != null){
            statisticsSalesmanPageParam.setSalesmanName(URLDecoder.decode(statisticsSalesmanPageParam.getSalesmanName(), "UTF-8"));
        }
        ServiceResult<String, StatisticsSalesman> result = statisticsService.querySalesman(statisticsSalesmanPageParam);
        ServiceResult<String, String> serviceResult = excelExportService.export(result.getResult().getStatisticsSalesmanDetailPage().getItemList(), ExcelExportConfigGroup.statisticsSalesmanDetailConfig, "statisticsSalesmanDetail", "sheet1", response);
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

}
