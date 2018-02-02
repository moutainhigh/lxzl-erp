package com.lxzl.erp.dataaccess.dao.mysql.statistics;

import com.lxzl.erp.common.domain.statistics.pojo.*;
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

    StatisticsHomeByRentLengthType queryHomeByRentLengthType(@Param("maps") Map<String, Object> paramMap);

    StatisticsUnReceivableForSubCompany queryStatisticsUnReceivableCountForSubCompany();

    List<StatisticsUnReceivableDetailForSubCompany> queryStatisticsUnReceivableForSubCompany();
}
