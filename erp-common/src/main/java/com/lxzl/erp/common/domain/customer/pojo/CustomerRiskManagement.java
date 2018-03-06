package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRiskManagement extends BasePO {

	private Integer customerRiskManagementId;   //唯一标识

	private Integer customerId;
	@NotEmpty(message = ErrorCode.CUSTOMER_NO_NOT_NULL , groups = {UpdateGroup.class})
	private String customerNo;   //用户编号
	@NotNull(message = ErrorCode.CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_NOT_NULL , groups = {UpdateGroup.class})
	@Max(value = 10000000,message = ErrorCode.CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_ERROR , groups = {UpdateGroup.class})
	@Min(value = 0,message = ErrorCode.CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_ERROR , groups = {UpdateGroup.class})
	private BigDecimal creditAmount;   //授信额度
	private BigDecimal creditAmountUsed;   //已用授信额度
	private Integer depositCycle;   //押金期数
	private Integer paymentCycle;   //付款期数
	private Integer appleDepositCycle;	// 苹果设备租赁方案
	private Integer applePaymentCycle;	// 苹果设备付款期数
	private Integer newDepositCycle;	// 全新押金期数
	private Integer newPaymentCycle;	// 全新设备租赁方案
	private Integer payMode;			// 其他设备支付方式
	private Integer applePayMode;		// 苹果设备支付方式
	private Integer newPayMode;			// 全新设备支付方式
	private Integer isLimitApple;		// 是否限制苹果
	private Integer isLimitNew;			// 是否限制全新
	private BigDecimal singleLimitPrice;	// 单台限制设备价值
	@NotNull(message = ErrorCode.CUSTOMER_RETURN_VISIT_FREQUENCY_NOT_NULL , groups = {UpdateGroup.class})
	@Max(value = 12,message = ErrorCode.CUSTOMER_RETURN_VISIT_FREQUENCY_ERROR , groups = {UpdateGroup.class})
	@Min(value = 0,message = ErrorCode.CUSTOMER_RETURN_VISIT_FREQUENCY_ERROR , groups = {UpdateGroup.class})
	private Integer returnVisitFrequency; 	// 回访频率
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人
	@NotNull(message = ErrorCode.CUSTOMER_IS_FULL_DEPOSIT_NOT_NULL , groups = {UpdateGroup.class})
	@In(value = {CommonConstant.NO,CommonConstant.YES}, message = ErrorCode.CUSTOMER_IS_FULL_DEPOSIT_ERROR)
	private Integer isFullDeposit;   //是否是全额押金客户

	private String customerName;

	public Integer getCustomerRiskManagementId(){
		return customerRiskManagementId;
	}

	public void setCustomerRiskManagementId(Integer customerRiskManagementId){
		this.customerRiskManagementId = customerRiskManagementId;
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

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public Integer getIsFullDeposit() {
		return isFullDeposit;
	}

	public void setIsFullDeposit(Integer isFullDeposit) {
		this.isFullDeposit = isFullDeposit;
	}
}