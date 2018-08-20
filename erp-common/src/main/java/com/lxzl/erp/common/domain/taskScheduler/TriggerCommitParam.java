package com.lxzl.erp.common.domain.taskScheduler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.common.domain.validGroup.TaskTrigger.DeleteTaskTriggerGroup;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/26
 * @Time : Created in 10:22
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TriggerCommitParam {
    @NotBlank(message = ErrorCode.QUARTZ_TRIGGER_NAME_NOT_NULL,groups = { QueryGroup.class,DeleteTaskTriggerGroup.class})
    @Pattern(message = ErrorCode.QUARTZ_TRIGGER_NAME_NOT_CN,regexp = "^[A-Za-z0-9]+$",groups = { QueryGroup.class,DeleteTaskTriggerGroup.class})
    @Length(max = 31,message = ErrorCode.QUARTZ_TRIGGER_NAME_IS_LENGTH,groups = { QueryGroup.class,DeleteTaskTriggerGroup.class})
    private String triggerName;
    @NotBlank(message = ErrorCode.QUARTZ_TRIGGER_GROUP_NOT_NULL,groups = { QueryGroup.class,DeleteTaskTriggerGroup.class})
    @Pattern(message = ErrorCode.QUARTZ_TRIGGER_GROUP_NOT_CN,regexp = "^[A-Za-z0-9]+$",groups = { QueryGroup.class,DeleteTaskTriggerGroup.class})
    @Length(max = 31,message = ErrorCode.QUARTZ_TRIGGER_GROUP_IS_LENGTH,groups = { QueryGroup.class,DeleteTaskTriggerGroup.class})
    private String triggerGroup;
    @NotNull(message = ErrorCode.QUARTZ_TASK_EXECUTOR_ID_NOT_NULL,groups = { DeleteTaskTriggerGroup.class})
    private String taskExecutorId;

    public String getTaskExecutorId() {
        return taskExecutorId;
    }

    public void setTaskExecutorId(String taskExecutorId) {
        this.taskExecutorId = taskExecutorId;
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
}
