package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class OrderConfirmChangeLogDO  extends BaseDO {

	private Integer id;
	private Integer orderId;
	private String orderNo;
	private Integer changeReasonType;
	private String changeReason;
	private Integer isRestatementSuccess;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

}