package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.export.ExcelExportConfigGroup;
import com.lxzl.erp.core.service.export.ExcelExportService;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.se.common.domain.Result;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "exportPageBankSlipDetail", method = RequestMethod.POST)
    public Result exportPageBankSlip(BankSlipDetailQueryParam bankSlipDetailQueryParam, HttpServletResponse response) {
        ServiceResult<String,String> serviceResult = excelExportService.export(bankSlipService.pageBankSlipDetail(bankSlipDetailQueryParam), ExcelExportConfigGroup.bankSlipDetailConfig, "bankSlipDetail", "sheet1",response);
        return resultGenerator.generate(serviceResult.getErrorCode());
    }

    @RequestMapping(value = "exportStatementOrderPage", method = RequestMethod.POST)
    public Result exportStatementOrderPage(StatementOrderQueryParam statementOrderQueryParam, HttpServletResponse response) {
        ServiceResult<String, Page<StatementOrder>> serviceResult = statementService.queryStatementOrder(statementOrderQueryParam);
        ServiceResult<String,XSSFWorkbook> result = excelExportService.getXSSFWorkbook(serviceResult, ExcelExportConfigGroup.statementOrderConfig, "sheet1");

        List<StatementOrder> statementOrderList = serviceResult.getResult().getItemList();
        List<StatementOrderDetail> statementOrderDetailList = new ArrayList<>();
        for (StatementOrder statementOrder : statementOrderList) {
            ServiceResult<String, StatementOrder> serviceResult1 = statementService.queryStatementOrderDetail(statementOrder.getStatementOrderNo());
            statementOrderDetailList.addAll(serviceResult1.getResult().getStatementOrderDetailList());
        }

        ServiceResult<String,String> serviceResult1 = excelExportService.export(statementOrderDetailList, ExcelExportConfigGroup.statementOrderDetailConfig, response, result.getResult(), "statementOrder","sheet1",serviceResult.getResult().getItemList().size());
        return resultGenerator.generate(serviceResult1.getErrorCode(),serviceResult1.getResult());
    }
}
