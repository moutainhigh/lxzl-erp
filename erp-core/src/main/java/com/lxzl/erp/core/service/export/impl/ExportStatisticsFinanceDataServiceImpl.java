package com.lxzl.erp.core.service.export.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.StatisticsIntervalType;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statistics.FinanceStatisticsParam;
import com.lxzl.erp.common.domain.statistics.pojo.FinanceStatisticsDataWeeklyExcel;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.export.ExcelExportConfigGroup;
import com.lxzl.erp.core.service.export.ExcelExportService;
import com.lxzl.erp.core.service.export.ExcelMultiSheetConfig;
import com.lxzl.erp.core.service.export.ExportStatisticsFinanceDataService;
import com.lxzl.erp.core.service.export.impl.support.ExcelExportSupport;
import com.lxzl.erp.core.service.statistics.StatisticsService;
import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsRentProductDetail;
import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsReturnProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: liuyong
 * Date: 2018/7/20
 * Time: 15:18
 */

@Service
public class ExportStatisticsFinanceDataServiceImpl implements ExportStatisticsFinanceDataService {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private ExcelExportService excelExportService;

    @Override
    public ServiceResult<String, String> exportStatisticsFinanceData(FinanceStatisticsParam param, HttpServletResponse response){
        ServiceResult<String, String> excelServiceResult = new ServiceResult<>();
        if (param == null || param.getStatisticsInterval() == null) {
            excelServiceResult = excelExportService.export(new ArrayList<FinanceStatisticsDataWeeklyExcel>(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, "财务报表", "sheet1", response);
            excelServiceResult.setErrorCode(ErrorCode.STATISTICS_FINANCE_PARAM_INTERVAL_INVALID);
            return excelServiceResult;
        }
        if (StatisticsIntervalType.STATISTICS_INTERVAL_WEEKLY == param.getStatisticsInterval()) {
            return exportStatisticsFinanceDataWeekly(param, response);
        } else if (StatisticsIntervalType.STATISTICS_INTERVAL_MONTHLY == param.getStatisticsInterval()){
            return exportStatisticsFinanceDataMonthly(param, response);
        } else if (StatisticsIntervalType.STATISTICS_INTERVAL_YEARLY == param.getStatisticsInterval()){
            // TODO
        }
        excelServiceResult = excelExportService.export(new ArrayList<FinanceStatisticsDataWeeklyExcel>(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, "财务报表", "sheet1", response);
        excelServiceResult.setErrorCode(ErrorCode.STATISTICS_FINANCE_WEEKLY_PARAM_INVALID);
        return excelServiceResult;
    }

    @Override
    public ServiceResult<String, String> exportStatisticsFinanceDataWeekly(FinanceStatisticsParam param, HttpServletResponse response) {
        ServiceResult<String, String> excelServiceResult = new ServiceResult<>();

        if (param == null || param.getYear() == null|| param.getMonth() == null || param.getWeekOfMonth() == null) {
            //  年、月、周必填参数不能为空
            excelServiceResult = excelExportService.export(new ArrayList<FinanceStatisticsDataWeeklyExcel>(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, "财务周报", "sheet1", response);
            excelServiceResult.setErrorCode(ErrorCode.STATISTICS_FINANCE_WEEKLY_PARAM_INVALID);
            return excelServiceResult;
        }
        String fileName = "财务周报" + "_" + param.getYear() + "年" + param.getMonth() + "月第" + param.getWeekOfMonth() + "周";
        ServiceResult<String, List<FinanceStatisticsDataWeeklyExcel>> statisticsResult = statisticsService.statisticsFinanceDataWeeklyToExcel(param);
        List<FinanceStatisticsDataWeeklyExcel> financeAllStatisticsDataWeeklyExcelList = statisticsResult.getResult();
        if (CollectionUtil.isNotEmpty(financeAllStatisticsDataWeeklyExcelList)) {
            ExcelMultiSheetConfig sheet1 = new ExcelMultiSheetConfig(financeAllStatisticsDataWeeklyExcelList, ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, "周增量数据");
            ServiceResult<String, List<FinanceStatisticsRentProductDetail>> rentProductDetailResult = statisticsService.statisticsRentProductDetail(param);
            ExcelMultiSheetConfig sheet2 = new ExcelMultiSheetConfig(rentProductDetailResult.getResult(), ExcelExportConfigGroup.statisticsRentProductDetailConfig, "租赁明细");
            ServiceResult<String, List<FinanceStatisticsReturnProductDetail>> returnProductDetailResult = statisticsService.statisticsReturnProductDetail(param);
            ExcelMultiSheetConfig sheet3 = new ExcelMultiSheetConfig(returnProductDetailResult.getResult(), ExcelExportConfigGroup.statisticsReturnProductDetailConfig, "退货明细");
            ServiceResult<String, List<FinanceStatisticsDataWeeklyExcel>> statisticsTotalResult = statisticsService.statisticsFinanceDataWeeklyTotalToExcel(param);
            ExcelMultiSheetConfig sheet4 = new ExcelMultiSheetConfig(statisticsTotalResult.getResult(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, "周累计数据");
            List<ExcelMultiSheetConfig> excelMultiSheetConfigList = new ArrayList<>();
            excelMultiSheetConfigList.add(sheet1);
            excelMultiSheetConfigList.add(sheet2);
            excelMultiSheetConfigList.add(sheet3);
            excelMultiSheetConfigList.add(sheet4);
            excelServiceResult = excelExportService.export(excelMultiSheetConfigList,response,fileName);
            excelServiceResult.setErrorCode(ErrorCode.SUCCESS);
        } else {
            excelServiceResult = excelExportService.export(new ArrayList<FinanceStatisticsDataWeeklyExcel>(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, fileName, "sheet1", response);
            excelServiceResult.setErrorCode(ErrorCode.STATISTICS_FINANCE_WEEKLY_DATA_NOT_EXISTS);
        }
        //excelServiceResult = exprotExcelData(response, fileName, financeAllStatisticsDataWeeklyExcelList);
        return excelServiceResult;
    }

    @Override
    public ServiceResult<String, String> exportStatisticsFinanceDataMonthly(FinanceStatisticsParam param, HttpServletResponse response) {
        ServiceResult<String, String> excelServiceResult = new ServiceResult<>();

        if (param == null || param.getYear() == null|| param.getMonth() == null) {
            //  年、月必填参数不能为空
            excelServiceResult = excelExportService.export(new ArrayList<FinanceStatisticsDataWeeklyExcel>(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, "财务月报", "sheet1", response);
            excelServiceResult.setErrorCode(ErrorCode.STATISTICS_FINANCE_WEEKLY_PARAM_INVALID);
            return excelServiceResult;
        }
        String fileName = "财务月报" + "_" + param.getYear() + "年" + param.getMonth() + "月";
        ServiceResult<String, List<FinanceStatisticsDataWeeklyExcel>> statisticsResult = statisticsService.statisticsFinanceDataMonthlyToExcel(param);
        List<FinanceStatisticsDataWeeklyExcel> financeAllStatisticsDataMonthlyExcelList = statisticsResult.getResult();
        if (CollectionUtil.isNotEmpty(financeAllStatisticsDataMonthlyExcelList)) {
            ExcelMultiSheetConfig sheet1 = new ExcelMultiSheetConfig(financeAllStatisticsDataMonthlyExcelList, ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, "月数据");
            ServiceResult<String, List<FinanceStatisticsRentProductDetail>> rentProductDetailResult = statisticsService.statisticsRentProductDetail(param);
            ExcelMultiSheetConfig sheet2 = new ExcelMultiSheetConfig(rentProductDetailResult.getResult(), ExcelExportConfigGroup.statisticsRentProductDetailConfig, "租赁明细");
            ServiceResult<String, List<FinanceStatisticsReturnProductDetail>> returnProductDetailResult = statisticsService.statisticsReturnProductDetail(param);
            ExcelMultiSheetConfig sheet3 = new ExcelMultiSheetConfig(returnProductDetailResult.getResult(), ExcelExportConfigGroup.statisticsReturnProductDetailConfig, "退货明细");
//            ServiceResult<String, List<FinanceStatisticsDataWeeklyExcel>> statisticsTotalResult = statisticsService.statisticsFinanceDataMonthlyTotalToExcel(param);
//            ExcelMultiSheetConfig sheet4 = new ExcelMultiSheetConfig(statisticsTotalResult.getResult(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, "月累计数据");
            List<ExcelMultiSheetConfig> excelMultiSheetConfigList = new ArrayList<>();
            excelMultiSheetConfigList.add(sheet1);
            excelMultiSheetConfigList.add(sheet2);
            excelMultiSheetConfigList.add(sheet3);
//            excelMultiSheetConfigList.add(sheet4);
            excelServiceResult = excelExportService.export(excelMultiSheetConfigList,response,fileName);
            excelServiceResult.setErrorCode(ErrorCode.SUCCESS);
        } else {
            excelServiceResult = excelExportService.export(new ArrayList<FinanceStatisticsDataWeeklyExcel>(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, fileName, "sheet1", response);
            excelServiceResult.setErrorCode(ErrorCode.STATISTICS_FINANCE_WEEKLY_DATA_NOT_EXISTS);
        }
        //excelServiceResult = exprotExcelData(response, fileName, financeAllStatisticsDataMonthlyExcelList);
        return excelServiceResult;
    }

    private ServiceResult<String, String> exprotExcelData(HttpServletResponse response, String fileName, List<FinanceStatisticsDataWeeklyExcel> financeAllStatisticsDataWeeklyExcelList) {
        ServiceResult<String, String> excelServiceResult;
        if (CollectionUtil.isNotEmpty(financeAllStatisticsDataWeeklyExcelList)) {
            excelServiceResult = excelExportService.export(financeAllStatisticsDataWeeklyExcelList, ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, fileName, "sheet1", response);
            excelServiceResult.setErrorCode(ErrorCode.SUCCESS);
        } else {
            excelServiceResult = excelExportService.export(new ArrayList<FinanceStatisticsDataWeeklyExcel>(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, fileName, "sheet1", response);
            excelServiceResult.setErrorCode(ErrorCode.STATISTICS_FINANCE_WEEKLY_DATA_NOT_EXISTS);
        }
        return excelServiceResult;
    }


}
