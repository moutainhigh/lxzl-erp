package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class CustomerRiskManagementDO  extends BaseDO {

	private Integer id;
	private Integer customerId;
	private BigDecimal creditAmount;
	private BigDecimal creditAmountUsed;
	private Integer depositCycle;
	private Integer paymentCycle;
	private Integer dataStatus;
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

	public BigDecimal getCreditAmount(){
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount){
		this.creditAmount = creditAmount;
	}

	public BigDecimal getCreditAmountUsed(){
		return creditAmountUsed;
	}

	public void setCreditAmountUsed(BigDecimal creditAmountUsed){
		this.creditAmountUsed = creditAmountUsed;
	}

	public Integer getDepositCycle(){
		return depositCycle;
	}

	public void setDepositCycle(Integer depositCycle){
		this.depositCycle = depositCycle;
	}

	public Integer getPaymentCycle(){
		return paymentCycle;
	}

	public void setPaymentCycle(Integer paymentCycle){
		this.paymentCycle = paymentCycle;
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