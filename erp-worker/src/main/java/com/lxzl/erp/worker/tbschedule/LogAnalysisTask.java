package com.lxzl.erp.worker.tbschedule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lxzl.erp.core.service.LogService;
import com.lxzl.erp.dataaccess.domain.LogDO;
import com.lxzl.se.core.tbschedule.task.BaseTask;
import com.taobao.pamirs.schedule.TaskItemDefine;

@Component("logAnalysisTask")
public class LogAnalysisTask extends BaseTask<LogDO> {

	@Autowired
	private LogService logService;

	@Override
	public List<LogDO> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum)
			throws Exception {
		List<String> taskItems = getTaskItems(taskItemList);
		// 查找未处理和处理失败但处理次数小于5次的任务
		List<LogDO> logList = logService.findUnHandleLogs(taskItemNum, taskItems, eachFetchDataNum);
		return logList;
	}

	@Override
	public Object executeTask(LogDO[] tasks, String ownSign) throws Exception {
		Object result = null;
		boolean hasExeception = false;

		try {
			// 更新未处理和处理失败但处理次数小于5次的任务状态变为进行中
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(LogDO.STATUS_UNPROCESS);
			statusList.add(LogDO.STATUS_PROCESSING);
			logService.updateLogs(tasks, statusList, LogDO.STATUS_PROCESSING, 0);

			// 处理任务
			Thread.sleep(60000l);
			result = "success";
		} catch (Exception e) {
			hasExeception = true;
			result = "failure";
			throw e;
		} finally {
			// 更新处理中的任务状态变为成功或失败，并且增加处理次数
			Integer newStatus = hasExeception ? LogDO.STATUS_PROCESSED_FAILED : LogDO.STATUS_PROCESSED_SUCCESS;
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(LogDO.STATUS_PROCESSING);
			logService.updateLogs(tasks, statusList, newStatus, 1);
		}

		return result;
	}

	@Override
	public Comparator<LogDO> getComparator() {
		return new Comparator<LogDO>() {
			@Override
			public int compare(LogDO log1, LogDO log2) {
				return log1.getId().compareTo(log2.getId());
			}

			@Override
			public boolean equals(Object obj) {
				return this == obj;
			}
		};
	}

}
