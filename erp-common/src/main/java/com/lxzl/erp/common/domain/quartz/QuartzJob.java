package com.lxzl.erp.common.domain.quartz;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.quartz.AddOrUpdateJobGroup;
import com.lxzl.erp.common.domain.validGroup.quartz.JobGroup;
import com.lxzl.erp.common.domain.validGroup.quartz.SchedNameGroup;
import com.lxzl.erp.common.domain.validGroup.quartz.TriggerGroup;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @Auther: huahongbin
 * @Date: 2018/5/22
 */
public class QuartzJob implements Serializable {
    /**
     * sched名称
     */
    @NotNull(message = ErrorCode.QUARTZ_SCHED_NAME_NOT_NULL, groups = {AddOrUpdateJobGroup.class, SchedNameGroup.class})
    private String schedName;

    /**
     * 任务名称
     */
    @NotNull(message = ErrorCode.QUARTZ_JOB_NAME_NOT_NULL, groups = {AddOrUpdateJobGroup.class, JobGroup.class})
    private String jobName;

    /**
     * 任务组
     */
    @NotNull(message = ErrorCode.QUARTZ_JOB_GROUP_NOT_NULL, groups = {AddOrUpdateJobGroup.class, JobGroup.class})
    private String jobGroup;

    /**
     * 任务的具体类名, 可以是全路径的java类, 也可以是被spring管理的简单类名
     */
    @NotNull(message = ErrorCode.QUARTZ_JOB_CLASS_NAME_NOT_NULL, groups = {AddOrUpdateJobGroup.class})
    private String jobClassName;

    /**
     * 任务的描述
     */
    private String description;

    /**
     * 任务中断（如断电）是否是重新执行
     */
    @NotNull(message = ErrorCode.QUARTZ_RECOVERY_FLAG_NOT_NULL, groups = {AddOrUpdateJobGroup.class})
    private boolean recoveryFlag;

    /**
     * 触发器的名称（如：每天2点触发库存对账）
     */
    @NotNull(message = ErrorCode.QUARTZ_TRIGGER_NAME_NOT_NULL, groups = {AddOrUpdateJobGroup.class, TriggerGroup.class})
    private String triggerName;

    /**
     * 触发器分组
     */
    @NotNull(message = ErrorCode.QUARTZ_TRIGGER_GROUP_NOT_NULL, groups = {AddOrUpdateJobGroup.class, TriggerGroup.class})
    private String triggerGroup;

    /**
     * 是否是cron的表达式
     */
    @NotNull(message = ErrorCode.QUARTZ_CRON_TRIGGER_FLAG_NOT_NULL, groups = {AddOrUpdateJobGroup.class})
    private boolean cronTriggerFlag;

    /**
     * 任务触发时间的表达式(isCronTrigger等于true时为cron的表达式，isCronTrigger等于false时为间隔时间，单位秒)
     */
    @NotNull(message = ErrorCode.QUARTZ_EXPRESSION_NOT_NULL, groups = {AddOrUpdateJobGroup.class})
    private String expression;

    /**
     * 上次触发时间
     */
    private Date prevFireTime;

    /**
     * 下次触发时间
     */
    private Date nextFireTime;

    /**
     * 任务开始时间
     */
    private Date startAt;

    /**
     * 任务结束时间
     */
    private Date endAt;

    /**
     * 触发器状态
     */
    private String status;

    /**
     * 自定义扩展信息
     */
    private Map<String, String> extraInfo;

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Date getPrevFireTime() {
        return prevFireTime;
    }

    public void setPrevFireTime(Date prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, String> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public boolean isRecoveryFlag() {
        return recoveryFlag;
    }

    public void setRecoveryFlag(boolean recoveryFlag) {
        this.recoveryFlag = recoveryFlag;
    }

    public boolean isCronTriggerFlag() {
        return cronTriggerFlag;
    }

    public void setCronTriggerFlag(boolean cronTriggerFlag) {
        this.cronTriggerFlag = cronTriggerFlag;
    }
}
