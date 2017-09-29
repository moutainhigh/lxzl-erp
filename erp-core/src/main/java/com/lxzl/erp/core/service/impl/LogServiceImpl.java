package com.lxzl.erp.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lxzl.erp.core.service.LogService;
import com.lxzl.erp.dataaccess.dao.hbase.LogHbaseDAO;
import com.lxzl.erp.dataaccess.dao.mysql.LogMysqlDAO;
import com.lxzl.erp.dataaccess.domain.LogDO;
import com.lxzl.se.common.util.validate.ValidateUtil;
import com.lxzl.se.core.service.impl.BaseServiceImpl;

@Service("logService")
public class LogServiceImpl extends BaseServiceImpl implements LogService {

	@Autowired
	private LogMysqlDAO logMysqlDAO;

	@Autowired
	private LogHbaseDAO logHbaseDAO;

	@Override
	public List<LogDO> findUnHandleLogs(Integer taskItemNum, List<String> taskItems, Integer fetchNum) {
		ValidateUtil.notNull(taskItemNum, "taskItemNum不允许为空");
		ValidateUtil.notNull(taskItems, "taskItems不允许为空");
		ValidateUtil.notNull(fetchNum, "fetchNum不允许为空");
		List<Integer> taskItemIds = new ArrayList<Integer>();

		for (String taskItem : taskItems) {
			Integer id = Integer.valueOf(taskItem);
			taskItemIds.add(id);
		}

		return logMysqlDAO.findUnHandleLogsForSchedule(taskItemNum, taskItemIds, fetchNum);
	}

	@Override
	public boolean updateLogs(LogDO[] logs, List<Integer> oldStatusList, Integer newStatus, Integer num) {
		ValidateUtil.notNull(logs, "logs不允许为空");
		ValidateUtil.notNull(oldStatusList, "oldStatusList不允许为空");
		ValidateUtil.notNull(newStatus, "newStatus不允许为空");
		ValidateUtil.notNull(num, "num不允许为空");
		List<Integer> taskItemIds = new ArrayList<Integer>();

		for (int i = 0; i < logs.length; i++) {
			Integer id = logs[i].getId();
			taskItemIds.add(id);
		}

		int result = logMysqlDAO.updateLogs(taskItemIds, oldStatusList, newStatus, num);
		return result > 0;
	}

	@Override
	public boolean dumpLog(Integer id, String content, Integer executeTimes, Integer status, String createUser, Date createTime, String updateUser,
			Date updateTime) {
		LogDO log = new LogDO();
		log.setId(id);
		log.setContent(content);
		log.setExecuteTimes(executeTimes);
		log.setStatus(status);
		log.setCreateUser(createUser);
		log.setCreateTime(createTime);
		log.setUpdateUser(updateUser);
		log.setUpdateTime(updateTime);

		boolean flag = logHbaseDAO.addLog(log);
		return flag;
	}

	@Override
	public LogDO findHistroyLog(Integer id) {
		ValidateUtil.notNull(id, "id不允许为空");
		LogDO log = logHbaseDAO.getLog(id);
		return log;
	}

}
