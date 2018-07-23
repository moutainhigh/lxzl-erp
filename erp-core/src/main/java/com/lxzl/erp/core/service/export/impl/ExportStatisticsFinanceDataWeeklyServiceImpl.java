package com.lxzl.erp.core.service.export.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statistics.FinanceStatisticsWeeklyParam;
import com.lxzl.erp.common.domain.statistics.pojo.FinanceStatisticsDataWeeklyExcel;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.export.ExcelExportConfigGroup;
import com.lxzl.erp.core.service.export.ExcelExportService;
import com.lxzl.erp.core.service.export.ExportStatisticsFinanceDataWeeklyService;
import com.lxzl.erp.core.service.export.impl.support.ExcelExportSupport;
import com.lxzl.erp.core.service.statistics.StatisticsService;
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
public class ExportStatisticsFinanceDataWeeklyServiceImpl implements ExportStatisticsFinanceDataWeeklyService {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private ExcelExportService excelExportService;

    @Override
    public ServiceResult<String, String> exportStatisticsFinanceDataWeekly(FinanceStatisticsWeeklyParam param, HttpServletResponse response) {
        ServiceResult<String, String> excelServiceResult = new ServiceResult<>();

        if (param == null || param.getYear() == null|| param.getMonth() == null || param.getWeekOfMonth() == null) {
            //  年、月、周必填参数不能为空
            excelServiceResult = excelExportService.export(new ArrayList<FinanceStatisticsDataWeeklyExcel>(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, ExcelExportSupport.formatFileName("财务周报"), "sheet1", response);
            excelServiceResult.setErrorCode(ErrorCode.STATISTICS_FINANCE_WEEKLY_PARAM_INVALID);
            return excelServiceResult;
        }
        String fileName = "财务周报" + "_" + param.getYear() + "年" + param.getMonth() + "月第" + param.getWeekOfMonth() + "周";
        List<FinanceStatisticsDataWeeklyExcel> financeAllStatisticsDataWeeklyExcelList = statisticsService.statisticsFinanceDataWeeklyToExcel(param.getYear(), param.getMonth(), param.getWeekOfMonth());
        if (CollectionUtil.isNotEmpty(financeAllStatisticsDataWeeklyExcelList)) {
            excelServiceResult = excelExportService.export(financeAllStatisticsDataWeeklyExcelList, ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, ExcelExportSupport.formatFileName(fileName), "sheet1", response);
            excelServiceResult.setErrorCode(ErrorCode.SUCCESS);
        } else {
            excelServiceResult = excelExportService.export(new ArrayList<FinanceStatisticsDataWeeklyExcel>(), ExcelExportConfigGroup.statisticsFinanceWeeklyConfig, ExcelExportSupport.formatFileName(fileName), "sheet1", response);
            excelServiceResult.setErrorCode(ErrorCode.STATISTICS_FINANCE_WEEKLY_DATA_NOT_EXISTS);
        }
        return excelServiceResult;
    }
}
