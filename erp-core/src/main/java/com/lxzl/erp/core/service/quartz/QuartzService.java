package com.lxzl.erp.core.service.quartz;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.quartz.QuartzJob;
import com.lxzl.erp.common.domain.quartz.QuartzRunningJob;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/22
 */
public interface QuartzService {

    /**
     * 保存定时任务
     * @param quartzJob
     * @return
     */
    ServiceResult<String, Boolean> saveJob(QuartzJob quartzJob);

    /**
     * 更新定时任务
     * @param quartzJob
     * @return
     */
    ServiceResult<String, Boolean> updateJob(QuartzJob quartzJob);

    /**
     * 创建一个job，如果已存在该job，则更新该job
     * @param quartzJob
     * @return
     */
    ServiceResult<String, Boolean> saveOrUpdateJob(QuartzJob quartzJob);

    /**
     * 暂停一个trigger
     * @param quartzJob
     * @return
     */
    ServiceResult<String, Boolean> pauseTrigger(QuartzJob quartzJob);

    /**
     * 恢复一个trigger
     * @param quartzJob
     * @return
     */
    ServiceResult<String, Boolean> resumeTrigger(QuartzJob quartzJob);

    /**
     * 删除一个job，一个job可能有多个trigger,如果本次删除的trigger是该job唯一的触发器，则该job也会被删除
     * @param quartzJob
     * @return
     */
    ServiceResult<String, Boolean> deleteTrigger(QuartzJob quartzJob);

    /**
     * 获取所有的job
     * @return
     */
    ServiceResult<String, List<QuartzJob>> getAllJobs();

    /**
     * 分页查询所有job
     * @param quartzJob
     * @return
     */
    ServiceResult<String, Page<QuartzJob>> queryAllJobs(QuartzJob quartzJob);

    /**
     * 获取某个sched下面的所有job
     * @param schedName
     * @return
     */
    ServiceResult<String, List<QuartzJob>> getSchedulerJobs(String schedName);

    /**
     * 获取一个job
     * @param quartzJob
     * @return
     */
    ServiceResult<String, QuartzJob> getJob(QuartzJob quartzJob);

    /**
     * 获取一个运行中的job信息
     * @param quartzJob
     * @return
     */
    ServiceResult<String, QuartzRunningJob> getJobRunningInfo(QuartzJob quartzJob);

}
