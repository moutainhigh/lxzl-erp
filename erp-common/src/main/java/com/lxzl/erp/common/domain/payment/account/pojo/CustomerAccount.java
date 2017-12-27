package com.lxzl.erp.common.domain.payment.account.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAccount implements Serializable {

	private Integer businessCustomerAccountId;   //唯一标识
	private String customerNo;   //客户ID
	private BigDecimal balanceAmount;   //用户可用余额
	private BigDecimal totalFrozenAmount;   //总冻结金额
	private BigDecimal rentDepositAmount;   //租金押金金额
	private BigDecimal depositAmount;   //押金金额
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private Date updateTime;   //添加时间


	public Integer getBusinessCustomerAccountId(){
		return businessCustomerAccountId;
	}

	public void setBusinessCustomerAccountId(Integer businessCustomerAccountId){
		this.businessCustomerAccountId = businessCustomerAccountId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public BigDecimal getBalanceAmount(){
		return balanceAmount;
	}

	public void setBalanceAmount(BigDecimal balanceAmount){
		this.balanceAmount = balanceAmount;
	}

	public BigDecimal getTotalFrozenAmount(){
		return totalFrozenAmount;
	}

	public void setTotalFrozenAmount(BigDecimal totalFrozenAmount){
		this.totalFrozenAmount = totalFrozenAmount;
	}

	public BigDecimal getDepositAmount(){
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount){
		this.depositAmount = depositAmount;
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

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public BigDecimal getRentDepositAmount() {
		return rentDepositAmount;
	}

	public void setRentDepositAmount(BigDecimal rentDepositAmount) {
		this.rentDepositAmount = rentDepositAmount;
	}
}