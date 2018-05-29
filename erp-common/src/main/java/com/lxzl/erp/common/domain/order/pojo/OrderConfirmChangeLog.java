package com.lxzl.erp.common.domain.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderConfirmChangeLog extends BasePO {

	private Integer orderConfirmChangeLogId;   //唯一标识
	private Integer orderId;   //订单ID
	private String orderNo;   //订单编号
	private Integer changeReasonType;   //变更原因类型 ，1设备故障，2商品数量超过实际需求，3其他
	private String changeReason;   //变更原因
	private Integer isRestatementSuccess;   //是否重算成功：0-否，1-是
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人


	public Integer getOrderConfirmChangeLogId(){
		return orderConfirmChangeLogId;
	}

	public void setOrderConfirmChangeLogId(Integer orderConfirmChangeLogId){
		this.orderConfirmChangeLogId = orderConfirmChangeLogId;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Integer getChangeReasonType(){
		return changeReasonType;
	}

	public void setChangeReasonType(Integer changeReasonType){
		this.changeReasonType = changeReasonType;
	}

	public String getChangeReason(){
		return changeReason;
	}

	public void setChangeReason(String changeReason){
		this.changeReason = changeReason;
	}

	public Integer getIsRestatementSuccess(){
		return isRestatementSuccess;
	}

	public void setIsRestatementSuccess(Integer isRestatementSuccess){
		this.isRestatementSuccess = isRestatementSuccess;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
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