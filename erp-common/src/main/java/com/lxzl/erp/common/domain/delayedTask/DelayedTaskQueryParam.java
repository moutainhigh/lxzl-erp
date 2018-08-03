package com.lxzl.erp.common.domain.delayedTask;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\7\29 0029 14:47
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DelayedTaskQueryParam  extends BasePageParam {
    private Integer taskType;   //任务类型，1为对账单导出
    private Integer taskStatus;   //任务状态：1排队中；2处理中；3已完成；4已取消；5执行失败
    private String createUserName;   //添加人
    private Date createStartTime;   //起始时间
    private Date createEndTime;   //结束时间
    private String createUser; //添加人ID

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
