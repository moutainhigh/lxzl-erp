package com.lxzl.erp.dataaccess.dao.mysql.statistics;

import com.lxzl.erp.common.domain.statistics.pojo.StatisticsOperateData;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statistics.StatisticsOperateDataDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface StatisticsOperateDataMapper extends BaseMysqlDAO<StatisticsOperateDataDO> {

	List<StatisticsOperateDataDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<StatisticsOperateDataDO> findDataListForSubCompanyDate(@Param("maps") Map<String, Object> maps);

	List<StatisticsOperateDataDO> findDataListForSubCompanyWeekAndMonth(@Param("maps") Map<String, Object> maps);

	void addList(@Param("list") List<StatisticsOperateDataDO> statisticsOperateDataDOList);

	List<StatisticsOperateDataDO> findDataListForSalesmanDate(@Param("maps") Map<String, Object> maps);

	List<StatisticsOperateDataDO> findDataListForSalesmanWeekAndMonth(@Param("maps") Map<String, Object> maps);

	Integer findStatisticsOperateDataCountByParams(@Param("maps") Map<String, Object> maps);

	List<StatisticsOperateDataDO> findStatisticsOperateDataByParams(@Param("maps") Map<String, Object> maps);
}