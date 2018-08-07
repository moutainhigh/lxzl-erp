package com.lxzl.erp.common.domain.taskScheduler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.taskScheduler.pojo.HolidayDTO;
import com.lxzl.erp.common.domain.taskScheduler.pojo.TaskExecutor;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/25
 * @Time : Created in 21:41
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class  TaskSchedulerCommitParam {

    @NotBlank(message = ErrorCode.QUARTZ_JOB_NAME_NOT_NULL,groups = {UpdateGroup.class})
    @Pattern(message = ErrorCode.QUARTZ_JOB_NAME_NOT_CN,regexp = "^[A-Za-z0-9]+$",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 31,message = ErrorCode.QUARTZ_JOB_NAME_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
    private String jobName;  //工作名称 —- 无特殊要求建议不传、使用默认值
    @NotBlank(message = ErrorCode.QUARTZ_JOB_GROUP_NOT_NULL,groups = {UpdateGroup.class})
    @Pattern(message = ErrorCode.QUARTZ_JOB_GROUP_NOT_CN,regexp = "^[A-Za-z0-9]+$",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 31,message = ErrorCode.QUARTZ_JOB_GROUP_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
    private String jobGroup;  //工作分组—-无特殊要求建议不传、使用默认值
    @NotBlank(message = ErrorCode.QUARTZ_TRIGGER_NAME_NOT_NULL,groups = {AddGroup.class, UpdateGroup.class})
    @Pattern(message = ErrorCode.QUARTZ_TRIGGER_NAME_NOT_CN,regexp = "^[A-Za-z0-9]+$",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 31,message = ErrorCode.QUARTZ_TRIGGER_NAME_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
    private String triggerName;  //触发器名称
    @NotBlank(message = ErrorCode.QUARTZ_TRIGGER_GROUP_NOT_NULL,groups = {AddGroup.class, UpdateGroup.class})
    @Pattern(message = ErrorCode.QUARTZ_TRIGGER_GROUP_NOT_CN,regexp = "^[A-Za-z0-9]+$",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 31,message = ErrorCode.QUARTZ_TRIGGER_GROUP_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
    private String triggerGroup;  //触发器分组
    @NotBlank(message = ErrorCode.QUARTZ_EXPRESSION_NOT_NULL,groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 120,message = ErrorCode.QUARTZ_CRON_EXPRESSION_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
    private String cronExpression;  //定时任务cron表达式
    @Valid
    private List<HolidayDTO> holidayDTOList;  //指定不需要指定定时任务的日期列表
    @Pattern(message = ErrorCode.QUARTZ_HOLIDAY_NAME_NOT_CN,regexp = "^[A-Za-z0-9]+$",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 31,message = ErrorCode.QUARTZ_HOLIDAY_NAME_IS_LENGTH,groups = {AddGroup.class, UpdateGroup.class})
    private String holidayName;  //当holidayDTOs不为空时必须指定	假期名称
    private String description;  //任务描述
    @NotNull(message = ErrorCode.QUARTZ_TASK_EXECUTOR_NOT_NULL,groups = {AddGroup.class})
    @Valid
    private TaskExecutor taskExecutor;  //任务执行者信息

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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public List<HolidayDTO> getHolidayDTOList() {
        return holidayDTOList;
    }

    public void setHolidayDTOList(List<HolidayDTO> holidayDTOList) {
        this.holidayDTOList = holidayDTOList;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
}
