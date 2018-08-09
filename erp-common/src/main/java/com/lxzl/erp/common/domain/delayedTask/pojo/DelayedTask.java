package com.lxzl.erp.common.domain.delayedTask.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DelayedTask extends BasePO {

	private Integer delayedTaskId;   //唯一标识
	@NotNull(message = ErrorCode.TASK_TYPE_NOT_NULL,groups = {AddGroup.class})
	private Integer taskType;   //任务类型，1为对账单导出
	private Integer taskStatus;   //任务状态：1排队中；2处理中；3已完成；4已取消；5执行失败
	private Integer queueNumber;   //排队数量
	@NotEmpty(message = ErrorCode.TASK_REQUEST_JSON_NOT_EMPTY , groups = {AddGroup.class})
	private String requestJson;   //请求参数
	private String threadName;   //线程名称
	private Double progressRate;   //处理进度，10%的处理进度时存储为0.0100
	private String fileUrl;   //文件URL
	private String remark;   //备注
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private String createUserRealName;   //添加人姓名
	private String updateUserRealName;   //修改人姓名
	private Boolean isSync;   //是否同步输出，1是同步输出，0是线程异步处理

	public Boolean getSync() {
		return isSync;
	}

	public void setSync(Boolean sync) {
		isSync = sync;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getDelayedTaskId(){
		return delayedTaskId;
	}

	public void setDelayedTaskId(Integer delayedTaskId){
		this.delayedTaskId = delayedTaskId;
	}

	public Integer getTaskType(){
		return taskType;
	}

	public void setTaskType(Integer taskType){
		this.taskType = taskType;
	}

	public Integer getTaskStatus(){
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus){
		this.taskStatus = taskStatus;
	}

	public Integer getQueueNumber(){
		return queueNumber;
	}

	public void setQueueNumber(Integer queueNumber){
		this.queueNumber = queueNumber;
	}

	public String getRequestJson(){
		return requestJson;
	}

	public void setRequestJson(String requestJson){
		this.requestJson = requestJson;
	}

	public String getThreadName(){
		return threadName;
	}

	public void setThreadName(String threadName){
		this.threadName = threadName;
	}

	public Double getProgressRate() {
		return progressRate;
	}

	public void setProgressRate(Double progressRate) {
		this.progressRate = progressRate;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

	@Override
	public String getCreateUserRealName() {
		return createUserRealName;
	}

	@Override
	public void setCreateUserRealName(String createUserRealName) {
		this.createUserRealName = createUserRealName;
	}

	@Override
	public String getUpdateUserRealName() {
		return updateUserRealName;
	}

	@Override
	public void setUpdateUserRealName(String updateUserRealName) {
		this.updateUserRealName = updateUserRealName;
	}
}