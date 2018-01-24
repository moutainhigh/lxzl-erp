package com.lxzl.erp.dataaccess.domain.statement;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class StatementOrderReturnDetailDO  extends BaseDO {

	private Integer id;
	private Integer statementOrderId;
	private Integer customerId;
	private Integer orderId;
	private Integer orderItemReferId;
	private Integer returnType;
	private BigDecimal returnAmount;
	private Date returnTime;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getStatementOrderId(){
		return statementOrderId;
	}

	public void setStatementOrderId(Integer statementOrderId){
		this.statementOrderId = statementOrderId;
	}

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public Integer getOrderItemReferId(){
		return orderItemReferId;
	}

	public void setOrderItemReferId(Integer orderItemReferId){
		this.orderItemReferId = orderItemReferId;
	}

	public Integer getReturnType(){
		return returnType;
	}

	public void setReturnType(Integer returnType){
		this.returnType = returnType;
	}

	public BigDecimal getReturnAmount(){
		return returnAmount;
	}

	public void setReturnAmount(BigDecimal returnAmount){
		this.returnAmount = returnAmount;
	}

	public Date getReturnTime(){
		return returnTime;
	}

	public void setReturnTime(Date returnTime){
		this.returnTime = returnTime;
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