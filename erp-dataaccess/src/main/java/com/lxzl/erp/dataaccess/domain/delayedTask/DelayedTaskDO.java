package com.lxzl.erp.dataaccess.domain.delayedTask;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;


public class DelayedTaskDO  extends BaseDO {

	private Integer id;
	private Integer taskType;
	private Integer taskStatus;
	private Integer queueNumber;
	private String requestJson;
	private String threadName;
	private Double progressRate;
	private String remark;
	private Integer dataStatus;
	private String fileUrl;
	@Transient
	private String createUserRealName;   //添加人姓名
	@Transient
	private String updateUserRealName;   //修改人姓名

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public String getCreateUserRealName() {
		return createUserRealName;
	}

	public void setCreateUserRealName(String createUserRealName) {
		this.createUserRealName = createUserRealName;
	}

	public String getUpdateUserRealName() {
		return updateUserRealName;
	}

	public void setUpdateUserRealName(String updateUserRealName) {
		this.updateUserRealName = updateUserRealName;
	}
}