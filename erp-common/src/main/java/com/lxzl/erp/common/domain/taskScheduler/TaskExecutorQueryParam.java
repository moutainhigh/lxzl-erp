package com.lxzl.erp.common.domain.taskScheduler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/26
 * @Time : Created in 14:16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskExecutorQueryParam extends BasePageParam {

    @NotBlank(message = ErrorCode.QUARTZ_TASK_EXECUTOR_ID_NOT_NULL,groups = {QueryGroup.class})
    private String id;
    private String triggerName;
    private String triggerGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
