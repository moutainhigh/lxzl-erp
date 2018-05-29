package com.lxzl.erp.dataaccess.dao.mysql.statistics;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statistics.StatisticsSalesmanMonthDO;import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface StatisticsSalesmanMonthMapper extends BaseMysqlDAO<StatisticsSalesmanMonthDO> {

	List<StatisticsSalesmanMonthDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    void addList(@Param("list") List<StatisticsSalesmanMonthDO> list);

	StatisticsSalesmanMonthDO findByMonth(@Param("start") Date start);
}