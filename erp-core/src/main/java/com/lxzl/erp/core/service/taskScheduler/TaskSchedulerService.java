package com.lxzl.erp.core.service.taskScheduler;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.taskScheduler.TaskExecutorCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.TaskExecutorQueryParam;
import com.lxzl.erp.common.domain.taskScheduler.TaskSchedulerCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.TriggerCommitParam;
import com.lxzl.erp.common.domain.taskScheduler.pojo.TaskExecutor;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/25
 * @Time : Created in 21:44
 */
public interface TaskSchedulerService {
    /**
    *@描述  初始化任务
    *@param  initTaskSchedulerCommitParam
    *@return  com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
    */ 
    ServiceResult<String,String> initTaskScheduler(TaskSchedulerCommitParam initTaskSchedulerCommitParam);

    /**
     *@描述  更新任务调度者
     *@param  initTaskSchedulerCommitParam
     *@return  com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,String> updateTaskScheduler(TaskSchedulerCommitParam initTaskSchedulerCommitParam);

    /**
     *@描述  暂停任务触发器
     *@param  triggerCommitParam
     *@return  com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,String> pauseTaskTrigger(TriggerCommitParam triggerCommitParam);

    /**
     *@描述  重启任务触发器
     *@param  triggerCommitParam
     *@return  com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,String> resumeTaskTrigger(TriggerCommitParam triggerCommitParam);

    /**
     *@描述  删除任务触发器
     *@param  triggerCommitParam
     *@return  com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,String> deleteTaskTrigger(TriggerCommitParam triggerCommitParam);

    /**
     *@描述  更新任务执行者信息
     *@param  taskExecutorCommitParam
     *@return  com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,String> updateTaskExecutor(TaskExecutorCommitParam taskExecutorCommitParam);

    /**
     *@描述  查询任务执行者分页信息
     *@param  taskExecutorQueryParam
     *@return  com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,Page<TaskExecutor>> pageTaskExecutor(TaskExecutorQueryParam taskExecutorQueryParam);
    /**
     *@描述  获取执行者详情信息
     *@param  taskExecutorQueryParam
     *@return  com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,TaskExecutor> detailTaskExecutor(TaskExecutorQueryParam taskExecutorQueryParam);
}
