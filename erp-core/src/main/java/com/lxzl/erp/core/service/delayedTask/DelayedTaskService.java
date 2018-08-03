package com.lxzl.erp.core.service.delayedTask;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.delayedTask.DelayedTaskQueryParam;
import com.lxzl.erp.common.domain.delayedTask.pojo.DelayedTask;
/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\7\27 0027 14:39
 */
public interface DelayedTaskService {
    /**
     * 添加延迟任务列表
     * @param delayedTask
     * @return
     */
    ServiceResult<String,DelayedTask> addDelayedTask(DelayedTask delayedTask) throws Exception ;

    ServiceResult<String, Page<DelayedTask>> pageDelayedTask(DelayedTaskQueryParam delayedTaskQueryParam);
}
