package com.lxzl.erp.dataaccess.dao.mysql.delayedTask;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.delayedTask.DelayedTaskDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface DelayedTaskMapper extends BaseMysqlDAO<DelayedTaskDO> {

	List<DelayedTaskDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	List<DelayedTaskDO> findByUserId(@Param("currentUserId") String currentUserId);

	DelayedTaskDO findQueueLast();

	Integer findProcessingCount();

    void subQueueNumber1();
	void subQueueNumber2();

	Integer findDelayedTaskCountByParams(@Param("maps") Map<String, Object> maps);

	List<DelayedTaskDO> findDelayedTaskByParams(@Param("maps") Map<String, Object> maps);
}