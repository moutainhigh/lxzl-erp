package com.lxzl.erp.common.domain.orderOperationLog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderOperationLog extends BasePO {

	private Integer orderOperationLogId;   //唯一标识
	private String orderNo;   //订单编号
	private Integer orderStatusBefore;   //订单修改前状态，0-待提交,4-审核中,8-待发货,12-处理中,16-已发货,20-确认收货,24-全部归还,28-取消,32-结束
	private Integer orderStatusAfter;   //订单修改后状态，0-待提交,4-审核中,8-待发货,12-处理中,16-已发货,20-确认收货,24-全部归还,28-取消,32-结束
	private String operationBefore;   //修改前操作内容
	private String operationAfter;   //修改后操作内容
	private Integer businessType;   //操作业务编码
	private Date createTime;   //添加时间
	private String createUser;   //添加人


	public Integer getOrderOperationLogId(){
		return orderOperationLogId;
	}

	public void setOrderOperationLogId(Integer orderOperationLogId){
		this.orderOperationLogId = orderOperationLogId;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Integer getOrderStatusBefore(){
		return orderStatusBefore;
	}

	public void setOrderStatusBefore(Integer orderStatusBefore){
		this.orderStatusBefore = orderStatusBefore;
	}

	public Integer getOrderStatusAfter(){
		return orderStatusAfter;
	}

	public void setOrderStatusAfter(Integer orderStatusAfter){
		this.orderStatusAfter = orderStatusAfter;
	}

	public String getOperationBefore(){
		return operationBefore;
	}

	public void setOperationBefore(String operationBefore){
		this.operationBefore = operationBefore;
	}

	public String getOperationAfter(){
		return operationAfter;
	}

	public void setOperationAfter(String operationAfter){
		this.operationAfter = operationAfter;
	}

	public Integer getBusinessType(){
		return businessType;
	}

	public void setBusinessType(Integer businessType){
		this.businessType = businessType;
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

}