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
	private Integer appleDepositCycle;	// 苹果设备租赁方案
	private Integer applePaymentCycle;
	private Integer newDepositCycle;	// 全新设备租赁方案
	private Integer newPaymentCycle;
	private Integer payMode;			// 其他设备支付方式
	private Integer applePayMode;		// 苹果设备支付方式
	private Integer newPayMode;			// 全新设备支付方式
	private Integer isLimitApple;		// 是否限制苹果
	private Integer isLimitNew;			// 是否限制全新
	private BigDecimal singleLimitPrice;	// 单台限制设备价值
	private Integer returnVisitFrequency; 	// 回访频率
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

	public Integer getAppleDepositCycle() {
		return appleDepositCycle;
	}

	public void setAppleDepositCycle(Integer appleDepositCycle) {
		this.appleDepositCycle = appleDepositCycle;
	}

	public Integer getApplePaymentCycle() {
		return applePaymentCycle;
	}

	public void setApplePaymentCycle(Integer applePaymentCycle) {
		this.applePaymentCycle = applePaymentCycle;
	}

	public Integer getNewDepositCycle() {
		return newDepositCycle;
	}

	public void setNewDepositCycle(Integer newDepositCycle) {
		this.newDepositCycle = newDepositCycle;
	}

	public Integer getNewPaymentCycle() {
		return newPaymentCycle;
	}

	public void setNewPaymentCycle(Integer newPaymentCycle) {
		this.newPaymentCycle = newPaymentCycle;
	}

	public Integer getApplePayMode() {
		return applePayMode;
	}

	public void setApplePayMode(Integer applePayMode) {
		this.applePayMode = applePayMode;
	}

	public Integer getNewPayMode() {
		return newPayMode;
	}

	public void setNewPayMode(Integer newPayMode) {
		this.newPayMode = newPayMode;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public Integer getIsLimitApple() {
		return isLimitApple;
	}

	public void setIsLimitApple(Integer isLimitApple) {
		this.isLimitApple = isLimitApple;
	}

	public Integer getIsLimitNew() {
		return isLimitNew;
	}

	public void setIsLimitNew(Integer isLimitNew) {
		this.isLimitNew = isLimitNew;
	}

	public BigDecimal getSingleLimitPrice() {
		return singleLimitPrice;
	}

	public void setSingleLimitPrice(BigDecimal singleLimitPrice) {
		this.singleLimitPrice = singleLimitPrice;
	}

	public Integer getReturnVisitFrequency() {
		return returnVisitFrequency;
	}

	public void setReturnVisitFrequency(Integer returnVisitFrequency) {
		this.returnVisitFrequency = returnVisitFrequency;
	}
}