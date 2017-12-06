package com.lxzl.erp.dataaccess.domain.statement;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class StatementOrderDetailDO  extends BaseDO {

	private Integer id;
	private Integer statementOrderId;
	private Integer customerId;
	private Integer orderId;
	private BigDecimal statementDetailAmount;
	private Integer statementDetailStatus;
	private Date statementStartTime;
	private Date statementEndTime;
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

	public BigDecimal getStatementDetailAmount(){
		return statementDetailAmount;
	}

	public void setStatementDetailAmount(BigDecimal statementDetailAmount){
		this.statementDetailAmount = statementDetailAmount;
	}

	public Integer getStatementDetailStatus(){
		return statementDetailStatus;
	}

	public void setStatementDetailStatus(Integer statementDetailStatus){
		this.statementDetailStatus = statementDetailStatus;
	}

	public Date getStatementStartTime(){
		return statementStartTime;
	}

	public void setStatementStartTime(Date statementStartTime){
		this.statementStartTime = statementStartTime;
	}

	public Date getStatementEndTime(){
		return statementEndTime;
	}

	public void setStatementEndTime(Date statementEndTime){
		this.statementEndTime = statementEndTime;
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