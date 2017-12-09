package com.lxzl.erp.common.domain.statement.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderDetail implements Serializable {

	private Integer statementOrderDetailId;   //唯一标识
	private Integer statementOrderId;   //结算单ID
	private Integer customerId;   //客户ID
	private Integer orderId;   //订单ID
	private BigDecimal statementDetailAmount;   //结算单金额
	private Integer statementDetailStatus;   //结算状态，0未结算，1已结算
	private Date statementStartTime;   //对账开始时间
	private Date statementEndTime;   //对账结束时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getStatementOrderDetailId(){
		return statementOrderDetailId;
	}

	public void setStatementOrderDetailId(Integer statementOrderDetailId){
		this.statementOrderDetailId = statementOrderDetailId;
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

}