package com.lxzl.erp.dataaccess.dao.mysql.statistics;

import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsDataMeta;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statistics.FinanceStatisticsDataWeeklyDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface FinanceStatisticsDataWeeklyMapper extends BaseMysqlDAO<FinanceStatisticsDataWeeklyDO> {

	List<FinanceStatisticsDataWeeklyDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer saveList(@Param("list") List<FinanceStatisticsDataWeeklyDO> financeStatisticsDataWeeklyDOList);
	List<FinanceStatisticsDataWeeklyDO> findByWhenCause(@Param("maps") Map<String, Object> paramMap);
	Integer deleteWhenCause(@Param("maps") Map<String, Object> paramMap);
	Integer logicalDeleteWhenCause(@Param("maps") Map<String, Object> paramMap);
	Integer countFinanceStatisticsDataMetaPage(@Param("maps")Map<String, Object> maps);
	List<FinanceStatisticsDataMeta> listFinanceStatisticsDataMetaPage(@Param("maps")Map<String, Object> maps);
}