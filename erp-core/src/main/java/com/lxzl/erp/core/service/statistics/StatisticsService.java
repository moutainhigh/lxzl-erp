package com.lxzl.erp.core.service.statistics;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.statistics.HomeRentParam;
import com.lxzl.erp.common.domain.statistics.StatisticsIncomePageParam;
import com.lxzl.erp.common.domain.statistics.StatisticsUnReceivablePageParam;
import com.lxzl.erp.common.domain.statistics.UnReceivablePageParam;
import com.lxzl.erp.common.domain.statistics.pojo.*;
import com.lxzl.se.core.service.BaseService;

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

}
