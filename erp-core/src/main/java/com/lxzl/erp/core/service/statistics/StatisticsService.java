package com.lxzl.erp.core.service.statistics;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statistics.*;
import com.lxzl.erp.common.domain.statistics.pojo.*;
import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsDataMeta;
import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsRentProductDetail;
import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsReturnProductDetail;
import com.lxzl.se.core.service.BaseService;

import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-24 16:25
 */
public interface StatisticsService extends BaseService {

    /**
     * 查询首页数据信息
     *
     * @return 首页数据信息
     */
    ServiceResult<String, StatisticsIndexInfo> queryIndexInfo();

    ServiceResult<String, StatisticsIncome> queryIncome(StatisticsIncomePageParam statisticsIncomePageParam);

    ServiceResult<String, UnReceivable> queryUnReceivable(UnReceivablePageParam unReceivablePageParam);

    ServiceResult<String, StatisticsUnReceivable> queryStatisticsUnReceivable(StatisticsUnReceivablePageParam statisticsUnReceivablePageParam);

    ServiceResult<String, StatisticsUnReceivableForSubCompany> queryStatisticsUnReceivableForSubCompany();

    ServiceResult<String, StatisticsHomeByRentLengthType> queryLongRent(HomeRentParam homeRentParam);

    ServiceResult<String, StatisticsHomeByRentLengthType> queryShortRent(HomeRentParam homeRentParam);

    ServiceResult<String, List<StatisticsHomeByRentLengthType>> queryLongRentByTime(HomeRentByTimeParam homeRentByTimeParam);

    ServiceResult<String, List<StatisticsHomeByRentLengthType>> queryShortRentByTime(HomeRentByTimeParam homeRentByTimeParam);

    ServiceResult queryAwaitReceivable(AwaitReceivablePageParam awaitReceivablePageParam);

    ServiceResult queryStatisticsAwaitReceivable(StatisticsAwaitReceivablePageParam statisticsAwaitReceivablePageParam);

    /**
     * 查询业务员提成数据
     */
    ServiceResult<String, StatisticsSalesman> querySalesman(StatisticsSalesmanPageParam statisticsSalesmanPageParam);

    /**
     * 长短租详细查询
     */
    ServiceResult<String, StatisticsRentInfo> queryRentInfo(StatisticsRentInfoPageParam statisticsRentInfoPageParam);

    /**
     * 确认业务员提成统计月结信息
     * @param statisticsSalesmanMonth
     * @return
     */
    ServiceResult<String,String> updateStatisticsSalesmanMonth(StatisticsSalesmanMonth statisticsSalesmanMonth);

    /**
     * 创建业务员提成统计月结信息
     * @param date
     * @return
     */
    ServiceResult<String,String> createStatisticsSalesmanMonth(Date date);

    ServiceResult<String,Boolean> statisticsFinanceDataWeeklyNow();
    ServiceResult<String, Boolean> reStatisticsFinanceDataWeekly(FinanceStatisticsParam paramVo);
    ServiceResult<String, Boolean> reStatisticsFinanceDataMonthLy(FinanceStatisticsParam paramVo);
    ServiceResult<String, Boolean> reStatisticsFinanceData(FinanceStatisticsParam paramVo);
    ServiceResult<String, List<FinanceStatisticsDataWeeklyExcel>> statisticsFinanceDataWeeklyToExcel(FinanceStatisticsParam paramVo);
    ServiceResult<String, List<FinanceStatisticsDataWeeklyExcel>> statisticsFinanceDataMonthlyToExcel(FinanceStatisticsParam paramVo);
    ServiceResult<String, List<FinanceStatisticsDataWeeklyExcel>> statisticsFinanceDataWeeklyTotalToExcel(FinanceStatisticsParam paramVo);
    ServiceResult<String, List<FinanceStatisticsDataWeeklyExcel>> statisticsFinanceDataMonthlyTotalToExcel(FinanceStatisticsParam paramVo);
    ServiceResult<String, List<FinanceStatisticsDataWeeklyExcel>> findStatisticsFinanceDataDetail(FinanceStatisticsParam paramVo);
    ServiceResult<String, Page<FinanceStatisticsDataMeta>> findAllStatisticsFinanceDataMeta(FinanceStatisticsParam paramVo);
    ServiceResult<String, List<FinanceStatisticsRentProductDetail>> statisticsRentProductDetail(FinanceStatisticsParam paramVo);
    ServiceResult<String, List<FinanceStatisticsReturnProductDetail>> statisticsReturnProductDetail(FinanceStatisticsParam paramVo);

    /**
     * 生成经营数据记录（手动）
     * @param date
     * @return
     */
    ServiceResult<String,String> createStatisticsOperateData(Date date);
    /**
     * 查询日经营数据
     * @param statisticsOperateDataPageParam
     * @return
     */
    ServiceResult<String,Page<StatisticsOperateData>> queryStatisticsOperateDataForDay(StatisticsOperateDataPageParam statisticsOperateDataPageParam);
    /**
     * 查询周经营数据
     * @param statisticsOperateDataPageParam
     * @return
     */
    ServiceResult<String,Page<StatisticsOperateData>> queryStatisticsOperateDataForWeek(StatisticsOperateDataPageParam statisticsOperateDataPageParam);
    /**
     * 查询月经营数据
     * @param statisticsOperateDataPageParam
     * @return
     */
    ServiceResult<String,Page<StatisticsOperateData>> queryStatisticsOperateDataForMonth(StatisticsOperateDataPageParam statisticsOperateDataPageParam);

    /**
     * 生成经营数据记录(定时任务调度)
     * @param date
     * @return
     */
    ServiceResult<String,String> createStatisticsOperateDataForTime();
}
