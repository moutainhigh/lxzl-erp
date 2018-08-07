package com.lxzl.erp.core.service.export.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.bank.BankSlipDetailQueryParam;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipClaim;
import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.erp.common.domain.dynamicSql.DynamicSqlSelectParam;
import com.lxzl.erp.common.domain.export.FinanceStatementOrderPayDetail;
import com.lxzl.erp.common.domain.statement.StatementOrderDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.domain.statistics.StatisticsSalesmanPageParam;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsSalesman;
import com.lxzl.erp.common.domain.statistics.pojo.StatisticsSalesmanDetail;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.bank.BankSlipService;
import com.lxzl.erp.core.service.dynamicSql.DynamicSqlService;
import com.lxzl.erp.core.service.export.DisposeExportDataService;
import com.lxzl.erp.core.service.export.ExcelExportConfigGroup;
import com.lxzl.erp.core.service.export.ExcelExportService;
import com.lxzl.erp.core.service.export.impl.support.ExcelExportSupport;
import com.lxzl.erp.core.service.statement.StatementService;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.se.common.domain.Result;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/10
 * @Time : Created in 11:43
 */

@Service
public class DisposeExportDataServiceImpl implements DisposeExportDataService {

    @Autowired
    private BankSlipService bankSlipService;
    @Autowired
    private StatementService statementService;
    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private DynamicSqlService dynamicSqlService;
    @Autowired
    private ResultGenerator resultGenerator;
    @Override
    public ServiceResult<String, String> disposePageBankSlipDetail(BankSlipDetailQueryParam bankSlipDetailQueryParam, HttpServletResponse response) {
        ServiceResult<String, String> result = new ServiceResult<>();
        bankSlipDetailQueryParam.setPayerName(ExcelExportSupport.decode(bankSlipDetailQueryParam.getPayerName()));
        ServiceResult<String, Page<BankSlipDetail>> stringPageServiceResult = bankSlipService.pageBankSlipDetail(bankSlipDetailQueryParam);
        if(!ErrorCode.SUCCESS.equals(stringPageServiceResult.getErrorCode())){
            result.setErrorCode(stringPageServiceResult.getErrorCode());
            return result;
        }
        List<BankSlipDetail> bankSlipDetailList = stringPageServiceResult.getResult().getItemList();
        if(CollectionUtil.isNotEmpty(bankSlipDetailList)){
            for (BankSlipDetail bankSlipDetail : bankSlipDetailList) {
                List<BankSlipClaim> bankSlipClaimList = bankSlipDetail.getBankSlipClaimList();
                if(CollectionUtil.isNotEmpty(bankSlipClaimList)){
                    String customerSubCompanyNameStringList = "";
                    for (BankSlipClaim bankSlipClaim : bankSlipClaimList) {
                        customerSubCompanyNameStringList = customerSubCompanyNameStringList +"\r\n"+ bankSlipClaim.getCustomerSubCompanyName();
                    }
                    bankSlipDetail.setCustomerSubCompanyNameStringList(customerSubCompanyNameStringList.trim());  //保存所有已经认领的客户对应的公司
                }
            }
        }
        result = excelExportService.export(bankSlipDetailList, ExcelExportConfigGroup.bankSlipDetailConfig, "资金流水记录", "sheet1", response);
        return result;
    }

    @Override
    public ServiceResult<String, String> disposeStatementOrderDetail(StatementOrderQueryParam statementOrderQueryParam, HttpServletResponse response) {
        ServiceResult<String, String> result = new ServiceResult<>();
        ServiceResult<String, StatementOrder> serviceResult = statementService.queryStatementOrderDetail(statementOrderQueryParam.getStatementOrderNo());
        if(!ErrorCode.SUCCESS.equals(serviceResult.getErrorCode())){
            result.setErrorCode(serviceResult.getErrorCode());
            return result;
        }
        ServiceResult<String, HSSFWorkbook> hSSFWorkbookResult = excelExportService.getHSSFWorkbook(serviceResult, ExcelExportConfigGroup.statementOrderConfig, "sheet1");
        List<StatementOrderDetail> statementOrderDetailList = serviceResult.getResult() == null ? null : serviceResult.getResult().getStatementOrderDetailList();
        ServiceResult<String, String> serviceResult1 = excelExportService.export(statementOrderDetailList, ExcelExportConfigGroup.statementOrderDetailConfig, response, hSSFWorkbookResult.getResult(), "结算单详情", "sheet1", 2);

        return serviceResult1;
    }

    @Override
    public ServiceResult<String, String> disposeStatisticsSalesmanDetail(StatisticsSalesmanPageParam statisticsSalesmanPageParam, HttpServletResponse response) {
        ServiceResult<String, String> result = new ServiceResult<>();
        statisticsSalesmanPageParam.setSalesmanName(ExcelExportSupport.decode(statisticsSalesmanPageParam.getSalesmanName()));
        ServiceResult<String, StatisticsSalesman> statisticsSalesmanResult = statisticsService.querySalesman(statisticsSalesmanPageParam);
        if(!ErrorCode.SUCCESS.equals(statisticsSalesmanResult.getErrorCode())){
            result.setErrorCode(statisticsSalesmanResult.getErrorCode());
            return result;
        }
        List<StatisticsSalesmanDetail> statisticsSalesmanDetailList = null;
        if(statisticsSalesmanResult.getResult() == null || statisticsSalesmanResult.getResult().getStatisticsSalesmanDetailPage() == null){
            statisticsSalesmanDetailList = statisticsSalesmanResult.getResult().getStatisticsSalesmanDetailPage().getItemList();
        }
        ServiceResult<String, String> serviceResult = excelExportService.export(statisticsSalesmanDetailList, ExcelExportConfigGroup.statisticsSalesmanDetailConfig, "销售统计详情", "sheet1", response);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> disposePageStatementOrder(StatementOrderDetailQueryParam statementOrderDetailQueryParam, HttpServletResponse response) {
        ServiceResult<String, String> result = new ServiceResult<>();
        ServiceResult<String, Page<FinanceStatementOrderPayDetail>> financeStatementOrderPayDetailResult = statementService.queryFinanceStatementOrderPayDetail(statementOrderDetailQueryParam);
        if(!ErrorCode.SUCCESS.equals(financeStatementOrderPayDetailResult.getErrorCode())){
            result.setErrorCode(financeStatementOrderPayDetailResult.getErrorCode());
            return result;
        }
        ServiceResult<String, String> serviceResult = excelExportService.export(financeStatementOrderPayDetailResult.getResult().getItemList(), ExcelExportConfigGroup.statementOrderPayDetailConfig,"支付明细", "sheet1", response);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, String> disposeDynamicSql(DynamicSqlSelectParam dynamicSqlSelectParam, HttpServletResponse response) throws Exception {
        ServiceResult<String, String> result = new ServiceResult<>();
        String sql  = URLDecoder.decode(dynamicSqlSelectParam.getSql(),"UTF-8");
        Result dynamicSqlResult = resultGenerator.generate(dynamicSqlService.selectBySql(sql));
        if(!ErrorCode.SUCCESS.equals(dynamicSqlResult.getCode())){
            result.setErrorCode(dynamicSqlResult.getCode());
            return result;
        }
        result = excelExportService.export(dynamicSqlResult,"动态sql","sheet1" , response,5000);
        return result;
    }
}
