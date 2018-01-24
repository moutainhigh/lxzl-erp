package com.lxzl.erp.common.domain.statement.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderReturnDetail extends BasePO {

	private Integer statementOrderReturnDetailId;   //唯一标识
	private Integer statementOrderId;   //结算单ID
	private Integer customerId;   //客户ID
	private Integer orderId;   //订单ID
	private Integer orderItemReferId;   //订单项ID
	private Integer returnType;   //退还类型，1退租金押金，2退押金。
	private BigDecimal returnAmount;   //退还租金押金金额
	private Date returnTime;   //退还租金押金时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getStatementOrderReturnDetailId(){
		return statementOrderReturnDetailId;
	}

	public void setStatementOrderReturnDetailId(Integer statementOrderReturnDetailId){
		this.statementOrderReturnDetailId = statementOrderReturnDetailId;
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