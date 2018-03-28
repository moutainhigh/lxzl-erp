package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.IdGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRiskManagementHistory extends BasePO {

	@NotNull(message = ErrorCode.ID_NOT_NULL , groups = {IdGroup.class})
	private Integer customerRiskManagementHistoryId;   //唯一标识
	private Integer customerId;   //客户ID
	private String customerNo;   //客户编号
	private BigDecimal creditAmount;   //授信额度
	private BigDecimal creditAmountUsed;   //已用授信额度
	private Integer depositCycle;   //押金期数
	private Integer paymentCycle;   //付款期数
	private Integer appleDepositCycle;   //苹果设备押金期数
	private Integer applePaymentCycle;   //苹果设备付款期数
	private Integer newDepositCycle;   //全新押金期数
	private Integer newPaymentCycle;   //全新付款期数
	private Integer payMode;   //其他设备支付方式
	private Integer applePayMode;   //苹果设备付款方式
	private Integer newPayMode;   //全新付款方式
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Integer isLimitApple;   //是否限制苹果，1是，0否
	private Integer isLimitNew;   //是否限制全新，1是，0否
	private BigDecimal singleLimitPrice;   //单台限制价值
	private Integer returnVisitFrequency;   //回访频率（单位，月）
	private Integer isFullDeposit;   //是否是全额押金客户
	private BigDecimal importCreditAmountUsed;   //导入已用授信额度
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getCustomerRiskManagementHistoryId(){
		return customerRiskManagementHistoryId;
	}

	public void setCustomerRiskManagementHistoryId(Integer customerRiskManagementHistoryId){
		this.customerRiskManagementHistoryId = customerRiskManagementHistoryId;
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