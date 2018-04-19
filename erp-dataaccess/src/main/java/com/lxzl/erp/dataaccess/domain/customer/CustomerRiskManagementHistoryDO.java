package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class CustomerRiskManagementHistoryDO  extends BaseDO {

	private Integer id;
	private Integer customerId;
	private String customerNo;
	private BigDecimal creditAmount;
	private BigDecimal creditAmountUsed;
	private Integer depositCycle;
	private Integer paymentCycle;
	private Integer appleDepositCycle;
	private Integer applePaymentCycle;
	private Integer newDepositCycle;
	private Integer newPaymentCycle;
	private Integer payMode;
	private Integer applePayMode;
	private Integer newPayMode;
	private Integer dataStatus;
	private String remark;
	private Integer isLimitApple;
	private Integer isLimitNew;
	private BigDecimal singleLimitPrice;
	private Integer returnVisitFrequency;
	private Integer isFullDeposit;
	private BigDecimal importCreditAmountUsed;

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

	public String getCustomerNo(){
		return customerNo;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo = customerNo;
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

	public Integer getAppleDepositCycle(){
		return appleDepositCycle;
	}

	public void setAppleDepositCycle(Integer appleDepositCycle){
		this.appleDepositCycle = appleDepositCycle;
	}

	public Integer getApplePaymentCycle(){
		return applePaymentCycle;
	}

	public void setApplePaymentCycle(Integer applePaymentCycle){
		this.applePaymentCycle = applePaymentCycle;
	}

	public Integer getNewDepositCycle(){
		return newDepositCycle;
	}

	public void setNewDepositCycle(Integer newDepositCycle){
		this.newDepositCycle = newDepositCycle;
	}

	public Integer getNewPaymentCycle(){
		return newPaymentCycle;
	}

	public void setNewPaymentCycle(Integer newPaymentCycle){
		this.newPaymentCycle = newPaymentCycle;
	}

	public Integer getPayMode(){
		return payMode;
	}

	public void setPayMode(Integer payMode){
		this.payMode = payMode;
	}

	public Integer getApplePayMode(){
		return applePayMode;
	}

	public void setApplePayMode(Integer applePayMode){
		this.applePayMode = applePayMode;
	}

	public Integer getNewPayMode(){
		return newPayMode;
	}

	public void setNewPayMode(Integer newPayMode){
		this.newPayMode = newPayMode;
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

	public Integer getIsLimitApple(){
		return isLimitApple;
	}

	public void setIsLimitApple(Integer isLimitApple){
		this.isLimitApple = isLimitApple;
	}

	public Integer getIsLimitNew(){
		return isLimitNew;
	}

	public void setIsLimitNew(Integer isLimitNew){
		this.isLimitNew = isLimitNew;
	}

	public BigDecimal getSingleLimitPrice(){
		return singleLimitPrice;
	}

	public void setSingleLimitPrice(BigDecimal singleLimitPrice){
		this.singleLimitPrice = singleLimitPrice;
	}

	public Integer getReturnVisitFrequency(){
		return returnVisitFrequency;
	}

	public void setReturnVisitFrequency(Integer returnVisitFrequency){
		this.returnVisitFrequency = returnVisitFrequency;
	}

	public Integer getIsFullDeposit(){
		return isFullDeposit;
	}

	public void setIsFullDeposit(Integer isFullDeposit){
		this.isFullDeposit = isFullDeposit;
	}

	public BigDecimal getImportCreditAmountUsed(){
		return importCreditAmountUsed;
	}

	public void setImportCreditAmountUsed(BigDecimal importCreditAmountUsed){
		this.importCreditAmountUsed = importCreditAmountUsed;
	}

}