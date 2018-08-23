package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class CustomerRiskLogDO  extends BaseDO {

	private Integer id;
	private Integer customerId;
	private Integer manageCustomerId;
	private BigDecimal oldCreditAmount;
	private BigDecimal oldCreditAmountUsed;
	private BigDecimal newCreditAmount;
	private BigDecimal newCreditAmountUsed;
	private String orderNo;
	private Integer businessType;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public Integer getManageCustomerId(){
		return manageCustomerId;
	}

	public void setManageCustomerId(Integer manageCustomerId){
		this.manageCustomerId = manageCustomerId;
	}

	public BigDecimal getOldCreditAmount(){
		return oldCreditAmount;
	}

	public void setOldCreditAmount(BigDecimal oldCreditAmount){
		this.oldCreditAmount = oldCreditAmount;
	}

	public BigDecimal getOldCreditAmountUsed(){
		return oldCreditAmountUsed;
	}

	public void setOldCreditAmountUsed(BigDecimal oldCreditAmountUsed){
		this.oldCreditAmountUsed = oldCreditAmountUsed;
	}

	public BigDecimal getNewCreditAmount(){
		return newCreditAmount;
	}

	public void setNewCreditAmount(BigDecimal newCreditAmount){
		this.newCreditAmount = newCreditAmount;
	}

	public BigDecimal getNewCreditAmountUsed(){
		return newCreditAmountUsed;
	}

	public void setNewCreditAmountUsed(BigDecimal newCreditAmountUsed){
		this.newCreditAmountUsed = newCreditAmountUsed;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Integer getBusinessType(){
		return businessType;
	}

	public void setBusinessType(Integer businessType){
		this.businessType = businessType;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

}