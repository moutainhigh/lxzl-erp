package com.lxzl.erp.core.service;

import java.util.Date;
import java.util.List;

import com.lxzl.erp.dataaccess.domain.LogDO;
import com.lxzl.se.core.service.BaseService;

public interface LogService extends BaseService {

	List<LogDO> findUnHandleLogs(Integer taskItemNum, List<String> taskItems, Integer fetchNum);
	
	boolean updateLogs(LogDO[] logs, List<Integer> oldStatusList, Integer newStatus, Integer num);
	
	boolean dumpLog(Integer id, String content, Integer executeTimes, Integer status, String createUser, Date createTime, String updateUser, Date updateTime);
	
	LogDO findHistroyLog(Integer id);
}
