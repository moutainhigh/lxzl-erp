package com.lxzl.erp.core.service.quartz.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.quartz.QuartzJob;
import com.lxzl.erp.common.domain.quartz.QuartzJobQueryParam;
import com.lxzl.erp.common.domain.quartz.QuartzRunningJob;
import com.lxzl.erp.common.domain.quartz.support.QuartzJobSupport;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.core.service.quartz.QuartzService;
import com.lxzl.se.core.quartz.component.QuartzManager;
import com.lxzl.se.core.quartz.config.JobRunningInfo;
import com.lxzl.se.core.quartz.config.QuartzParameter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/22
 */
@Service("quartzService")
public class QuartzServiceImpl implements QuartzService {
    @Override
    public ServiceResult<String, Boolean> saveJob(QuartzJob quartzJob) {
        ServiceResult<String, Boolean> serviceResult = new ServiceResult<>();
        Boolean result = quartzManager.saveJob(quartzJob.getSchedName(), quartzJob.getJobName(), quartzJob.getJobGroup(), quartzJob.getJobClassName(), quartzJob.getDescription()
                , quartzJob.isRecoveryFlag(), quartzJob.getTriggerName(), quartzJob.getTriggerGroup(), quartzJob.isCronTriggerFlag(), quartzJob.getExpression()
                , quartzJob.getStartAt(), quartzJob.getEndAt(), quartzJob.getExtraInfo());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(result);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Boolean> updateJob(QuartzJob quartzJob) {
        ServiceResult<String, Boolean> serviceResult = new ServiceResult<>();
        Boolean result = quartzManager.updateJob(quartzJob.getSchedName(), quartzJob.getJobName(), quartzJob.getJobGroup(), quartzJob.getJobClassName(), quartzJob.getDescription()
                , quartzJob.isRecoveryFlag(), quartzJob.getTriggerName(), quartzJob.getTriggerGroup(), quartzJob.isCronTriggerFlag(), quartzJob.getExpression()
                , quartzJob.getStartAt(), quartzJob.getEndAt(), quartzJob.getExtraInfo());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(result);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Boolean> saveOrUpdateJob(QuartzJob quartzJob) {
        ServiceResult<String, Boolean> serviceResult = new ServiceResult<>();
        Boolean result = quartzManager.saveOrUpdateJob(quartzJob.getSchedName(), quartzJob.getJobName(), quartzJob.getJobGroup(), quartzJob.getJobClassName(), quartzJob.getDescription()
                , quartzJob.isRecoveryFlag(), quartzJob.getTriggerName(), quartzJob.getTriggerGroup(), quartzJob.isCronTriggerFlag(), quartzJob.getExpression()
                , quartzJob.getStartAt(), quartzJob.getEndAt(), quartzJob.getExtraInfo());

        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(result);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Boolean> pauseTrigger(QuartzJob quartzJob) {
        ServiceResult<String, Boolean> serviceResult = new ServiceResult<>();
        Boolean result = quartzManager.pauseTrigger(quartzJob.getSchedName(), quartzJob.getTriggerName(), quartzJob.getTriggerGroup());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(result);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Boolean> resumeTrigger(QuartzJob quartzJob) {
        ServiceResult<String, Boolean> serviceResult = new ServiceResult<>();
        Boolean result = quartzManager.resumeTrigger(quartzJob.getSchedName(), quartzJob.getTriggerName(), quartzJob.getTriggerGroup());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(result);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Boolean> deleteTrigger(QuartzJob quartzJob) {
        ServiceResult<String, Boolean> serviceResult = new ServiceResult<>();
        Boolean result = quartzManager.deleteTrigger(quartzJob.getSchedName(), quartzJob.getTriggerName(), quartzJob.getTriggerGroup());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(result);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<QuartzJob>> getAllJobs() {
        ServiceResult<String, List<QuartzJob>> serviceResult = new ServiceResult<>();
        List<QuartzParameter> quartzParameterList = quartzManager.getAllJobs();
        List<QuartzJob> quartzJobList = QuartzJobSupport.convertQuartzParameterList2Job(quartzParameterList);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(quartzJobList);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, Page<QuartzJob>> queryAllJobs(QuartzJobQueryParam quartzJobQueryParam) {
        ServiceResult<String, Page<QuartzJob>> serviceResult = new ServiceResult<>();
        List<QuartzParameter> quartzParameterList = quartzManager.getAllJobs();
        List<QuartzParameter> removeList = new ArrayList<>();
        // 过滤查询条件
        if (CollectionUtil.isNotEmpty(quartzParameterList)) {
            if (StringUtils.isNotBlank(quartzJobQueryParam.getSchedName())) {
                for (QuartzParameter quartzParameter : quartzParameterList) {
                    if (!quartzParameter.getSchedName().contains(quartzJobQueryParam.getSchedName())) {
                        removeList.add(quartzParameter);
                    }
                }
                quartzParameterList.removeAll(removeList);
                removeList.clear();
            }
            if (StringUtils.isNotBlank(quartzJobQueryParam.getJobName())) {
                for (QuartzParameter quartzParameter : quartzParameterList) {
                    if (!quartzParameter.getJobName().contains(quartzJobQueryParam.getJobName())) {
                        removeList.add(quartzParameter);
                    }
                }
                quartzParameterList.removeAll(removeList);
                removeList.clear();
            }
            if (StringUtils.isNotBlank(quartzJobQueryParam.getJobGroup())) {
                for (QuartzParameter quartzParameter : quartzParameterList) {
                    if (!quartzParameter.getJobGroup().contains(quartzJobQueryParam.getJobGroup())) {
                        removeList.add(quartzParameter);
                    }
                }
                quartzParameterList.removeAll(removeList);
                removeList.clear();
            }
            if (StringUtils.isNotBlank(quartzJobQueryParam.getTriggerName())) {
                for (QuartzParameter quartzParameter : quartzParameterList) {
                    if (!quartzParameter.getTriggerName().contains(quartzJobQueryParam.getTriggerName())) {
                        removeList.add(quartzParameter);
                    }
                }
                quartzParameterList.removeAll(removeList);
                removeList.clear();
            }
            if (StringUtils.isNotBlank(quartzJobQueryParam.getTriggerGroup())) {
                for (QuartzParameter quartzParameter : quartzParameterList) {
                    if (!quartzParameter.getTriggerGroup().contains(quartzJobQueryParam.getTriggerGroup())) {
                        removeList.add(quartzParameter);
                    }
                }
                quartzParameterList.removeAll(removeList);
                removeList.clear();
            }
        }
        List<QuartzJob> quartzJobList = QuartzJobSupport.convertQuartzParameterList2Job(quartzParameterList);
        Page<QuartzJob> quartzJobPage = new Page<>(quartzJobList, quartzJobList.size(), quartzJobQueryParam.getPageNo(), quartzJobQueryParam.getPageSize());
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(quartzJobPage);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, List<QuartzJob>> getSchedulerJobs(String schedName) {
        ServiceResult<String, List<QuartzJob>> serviceResult = new ServiceResult<>();
        List<QuartzParameter> quartzParameterList = quartzManager.getSchedulerJobs(schedName);
        List<QuartzJob> quartzJobList = QuartzJobSupport.convertQuartzParameterList2Job(quartzParameterList);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(quartzJobList);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, QuartzJob> getJob(QuartzJob quartzJob) {
        ServiceResult<String, QuartzJob> serviceResult = new ServiceResult<>();
        QuartzParameter quartzParameter = quartzManager.getJob(quartzJob.getSchedName(), quartzJob.getJobName(), quartzJob.getJobGroup(), quartzJob.getTriggerName(), quartzJob.getTriggerGroup());
        QuartzJob quartzJobResult = QuartzJobSupport.convertQuartzParameter2Job(quartzParameter);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(quartzJobResult);
        return serviceResult;
    }

    @Override
    public ServiceResult<String, QuartzRunningJob> getJobRunningInfo(QuartzJob quartzJob) {
        ServiceResult<String, QuartzRunningJob> serviceResult = new ServiceResult<>();
        JobRunningInfo jobRunningInfo = quartzManager.getJobRunningInfo(quartzJob.getSchedName(), quartzJob.getJobName(), quartzJob.getJobGroup());
        QuartzRunningJob quartzRunningJob = QuartzJobSupport.convertJobRunningInfo2QuartzRunningJob(jobRunningInfo);
        serviceResult.setErrorCode(ErrorCode.SUCCESS);
        serviceResult.setResult(quartzRunningJob);
        return serviceResult;
    }

    @Autowired
    private QuartzManager quartzManager;
}
