package com.lxzl.erp.common.domain.taskScheduler.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/7/26
 * @Time : Created in 14:06
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskExecutor {

    @NotBlank(message = ErrorCode.QUARTZ_TASK_EXECUTOR_REQUEST_URL_NOT_NULL,groups = {AddGroup.class})
    private String requestUrl; //请求的url
    @NotNull(message = ErrorCode.QUARTZ_TASK_EXECUTOR_SYSTEM_TYPE_NOT_NULL,groups = {AddGroup.class})
    private Integer systemType;  //系统类型
    @NotNull(message = ErrorCode.QUARTZ_TASK_EXECUTOR_JOB_TYPE_NOT_NULL,groups = {AddGroup.class})
    private Integer jobType;  //定时任务job类型1:rest、2:mq、3:redis
    private String requestBody; //请求的主体内容[json对象字符串]

    private String triggerName;  //触发器名称
    private String triggerGroup;  //触发器分组
    private String cronExpression;  //定时任务cron表达式
    private Long id;  //ID
    private Long createBy;  //创建人的id
    private Long updateBy;  //最后更新人标识，记录用户的id
    private Date createDate;  //创建日期
    private Date updateDate;  //更新日期
    private Integer available;  //可用标志
    private Integer isDeleted;  //逻辑删除标志(1:删除)
    private String jobGroup;  //任务工作分组
    private String jobName;  //定时任务cron表达式
    private Integer retryCount;  //重试次数 默认3次
    private Integer retryInterval;  //重试间隔 默认8秒
    private String executeResult;  //执行的结果
    private String taskExecutorNo;  //任务执行者编号 唯一
    private String requestMethod;  //请求的方法统一使用post
    private String jobCallbackClassName;  //工作任务异步回调的完整类名
    private String asyn;  //是否为异步执行 0:同步 1:异步 默认使用异步方式执行
    private String remark;  //备注

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }

    public String getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    public String getTaskExecutorNo() {
        return taskExecutorNo;
    }

    public void setTaskExecutorNo(String taskExecutorNo) {
        this.taskExecutorNo = taskExecutorNo;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getJobCallbackClassName() {
        return jobCallbackClassName;
    }

    public void setJobCallbackClassName(String jobCallbackClassName) {
        this.jobCallbackClassName = jobCallbackClassName;
    }

    public String getAsyn() {
        return asyn;
    }

    public void setAsyn(String asyn) {
        this.asyn = asyn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
