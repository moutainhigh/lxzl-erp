package com.lxzl.erp.dataaccess.dao.mysql.statistics;

import com.lxzl.erp.common.domain.statistics.pojo.*;
import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsDealsCountBySubCompany;
import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsRentProductDetail;
import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsReturnProductDetail;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/1/18 14:15
 */

@Repository
public interface StatisticsMapper extends BaseMysqlDAO {

    StatisticsIncome queryIncomeCount(@Param("maps") Map<String, Object> paramMap);

    List<StatisticsIncomeDetail> queryIncome(@Param("maps") Map<String, Object> paramMap);

    UnReceivable queryUnReceivableCount(@Param("maps") Map<String, Object> paramMap);

    List<UnReceivableDetail> queryUnReceivable(@Param("maps") Map<String, Object> paramMap);

    StatisticsUnReceivable queryStatisticsUnReceivableCount(@Param("maps") Map<String, Object> paramMap);

    List<StatisticsUnReceivableDetail> queryStatisticsUnReceivable(@Param("maps") Map<String, Object> paramMap);

    StatisticsHomeByRentLengthType queryHomeByLongRent(@Param("maps") Map<String, Object> paramMap);

    StatisticsHomeByRentLengthType queryHomeByShortRent(@Param("maps") Map<String, Object> paramMap);

    StatisticsHomeByRentLengthType queryHomeByRentLengthType(@Param("maps") Map<String, Object> paramMap);

    List<StatisticsUnReceivableDetailForSubCompany> querySubCompanyInfo();

    AwaitReceivable queryAwaitReceivableCount(@Param("maps") Map<Object, Object> map);

    List<AwaitReceivableDetail> queryAwaitReceivable(@Param("maps") Map<Object, Object> map);

    StatisticsAwaitReceivable queryStatisticsAwaitReceivableCount(@Param("maps") Map<String, Object> maps);

    List<StatisticsAwaitReceivableDetail> queryStatisticsAwaitReceivable(@Param("maps") Map<String, Object> maps);

    StatisticsRentInfo queryRentInfoCount(@Param("maps") Map<String, Object> maps);

    List<StatisticsRentInfoDetail> queryRentInfoDetail(@Param("maps") Map<String, Object> maps);

    StatisticsSalesman querySalesmanCount(@Param("maps") Map<String, Object> maps);

    List<StatisticsSalesmanDetail> querySalesmanDetail(@Param("maps") Map<String, Object> maps);

    List<StatisticsSalesmanDetailExtend> querySalesmanDetailExtend(@Param("maps") Map<String, Object> maps);

    List<StatisticsSalesmanReturnOrder> querySalesmanReturnOrder(@Param("maps") Map<String, Object> maps);

    List<StatisticsSalesmanDetail> querySalesmanDetailByDate(@Param("maps") Map<String, Object> maps);

    List<StatisticsSalesmanDetailExtend> querySalesmanDetailExtendByDate(@Param("maps") Map<String, Object> maps);

    List<StatisticsSalesmanReturnOrder> querySalesmanReturnOrderByDate(@Param("maps") Map<String, Object> maps);

    List<FinanceStatisticsDealsCountBySubCompany> statisticsCustomerCountWeekly(@Param("maps") Map<String, Object> maps);
    List<FinanceStatisticsDealsCountBySubCompany> statisticsRentProductCountWeekly(@Param("maps") Map<String, Object> maps);
    List<FinanceStatisticsDealsCountBySubCompany> statisticsReturnProductCountWeekly(@Param("maps") Map<String, Object> maps);
    List<FinanceStatisticsRentProductDetail> statisticsRentProductDetail(@Param("maps") Map<String, Object> maps);
    List<FinanceStatisticsReturnProductDetail> statisticsReturnProductDetail(@Param("maps") Map<String, Object> maps);
}
