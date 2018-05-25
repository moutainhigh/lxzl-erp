package com.lxzl.erp.common.domain.quartz.support;

import com.lxzl.erp.common.domain.quartz.QuartzJob;
import com.lxzl.erp.common.domain.quartz.QuartzRunningJob;
import com.lxzl.se.core.quartz.config.JobRunningInfo;
import com.lxzl.se.core.quartz.config.QuartzParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/22
 */
public class QuartzJobSupport {

    public static QuartzJob convertQuartzParameter2Job(QuartzParameter quartzParameter) {
        if (quartzParameter == null) return null;
        QuartzJob quartzJob = new QuartzJob();
        quartzJob.setSchedName(quartzParameter.getSchedName());
        quartzJob.setJobName(quartzParameter.getJobName());
        quartzJob.setJobGroup(quartzParameter.getJobGroup());
        quartzJob.setJobClassName(quartzParameter.getJobClassName());
        quartzJob.setDescription(quartzParameter.getDescription());
        quartzJob.setRecoveryFlag(quartzParameter.getIsRecovery());
        quartzJob.setTriggerName(quartzParameter.getTriggerName());
        quartzJob.setTriggerGroup(quartzParameter.getTriggerGroup());
        quartzJob.setCronTriggerFlag(quartzParameter.getIsCronTrigger());
        quartzJob.setExpression(quartzParameter.getExpression());
        quartzJob.setPrevFireTime(quartzParameter.getPrevFireTime());
        quartzJob.setNextFireTime(quartzParameter.getNextFireTime());
        quartzJob.setStartAt(quartzParameter.getStartAt());
        quartzJob.setEndAt(quartzParameter.getEndAt());
        quartzJob.setStatus(quartzParameter.getStatus());
        quartzJob.setExtraInfo(quartzParameter.getExtraInfo());
        return quartzJob;
    }

    public static QuartzParameter convertQuartzJob2Parameter(QuartzJob quartzJob) {
        if (quartzJob == null) return null;
        QuartzParameter quartzParameter = new QuartzParameter();
        quartzParameter.setSchedName(quartzJob.getSchedName());
        quartzParameter.setJobName(quartzJob.getJobName());
        quartzParameter.setJobGroup(quartzJob.getJobGroup());
        quartzParameter.setJobClassName(quartzJob.getJobClassName());
        quartzParameter.setDescription(quartzJob.getDescription());
        quartzParameter.setIsRecovery(quartzJob.isRecoveryFlag());
        quartzParameter.setTriggerName(quartzJob.getTriggerName());
        quartzParameter.setTriggerGroup(quartzJob.getTriggerGroup());
        quartzParameter.setIsCronTrigger(quartzJob.isCronTriggerFlag());
        quartzParameter.setExpression(quartzJob.getExpression());
        quartzParameter.setPrevFireTime(quartzJob.getPrevFireTime());
        quartzParameter.setNextFireTime(quartzJob.getNextFireTime());
        quartzParameter.setStartAt(quartzJob.getStartAt());
        quartzParameter.setEndAt(quartzJob.getEndAt());
        quartzParameter.setStatus(quartzJob.getStatus());
        quartzParameter.setExtraInfo(quartzJob.getExtraInfo());
        return quartzParameter;
    }

    public static List<QuartzJob> convertQuartzParameterList2Job(List<QuartzParameter> quartzParameterList) {
        if (quartzParameterList == null) return null;
        List<QuartzJob> newList = new ArrayList<>();
        for (QuartzParameter quartzParameter : quartzParameterList) {
            if (quartzParameter != null) {
                newList.add(convertQuartzParameter2Job(quartzParameter));
            }
        }
        return newList;
    }

    public static List<QuartzParameter> convertQuartzJobList2Parameter(List<QuartzJob> quartzJobList) {
        if (quartzJobList == null) return  null;
        List<QuartzParameter> newList = new ArrayList<>();
        for (QuartzJob quartzJob : quartzJobList) {
            if (quartzJob != null) {
                newList.add(convertQuartzJob2Parameter(quartzJob));
            }
        }
        return newList;
    }

    public static QuartzRunningJob convertJobRunningInfo2QuartzRunningJob(JobRunningInfo jobRunningInfo) {
        if (jobRunningInfo == null) return null;
        QuartzRunningJob quartzRunningJob = new QuartzRunningJob();
        quartzRunningJob.setId(jobRunningInfo.getId());
        quartzRunningJob.setSchedName(jobRunningInfo.getSchedName());
        quartzRunningJob.setJobName(jobRunningInfo.getJobName());
        quartzRunningJob.setJobGroup(jobRunningInfo.getJobGroup());
        quartzRunningJob.setJobClassName(jobRunningInfo.getJobClassName());
        quartzRunningJob.setTriggerName(jobRunningInfo.getTriggerName());
        quartzRunningJob.setTriggerGroup(jobRunningInfo.getTriggerGroup());
        quartzRunningJob.setInstanceName(jobRunningInfo.getInstanceName());
        quartzRunningJob.setIp(jobRunningInfo.getIp());
        quartzRunningJob.setHostName(jobRunningInfo.getHostName());
        quartzRunningJob.setStartTime(jobRunningInfo.getStartTime());
        quartzRunningJob.setEndTime(jobRunningInfo.getEndTime());
        quartzRunningJob.setCost(String.valueOf(jobRunningInfo.getCost()));
        quartzRunningJob.setResult(jobRunningInfo.getResult());
        return quartzRunningJob;
    }
}
