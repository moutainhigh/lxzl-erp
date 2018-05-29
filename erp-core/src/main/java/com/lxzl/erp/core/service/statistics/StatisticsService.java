package com.lxzl.erp.core.service.statistics;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statistics.*;
import com.lxzl.erp.common.domain.statistics.pojo.*;
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

}
