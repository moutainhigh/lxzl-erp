package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRiskLog extends BasePO {

	private Integer customerRiskLogId;   //唯一标识
	private Integer customerId;   //客户ID
	private Integer manageCustomerId;   //关联客户ID
	private BigDecimal oldCreditAmount;   //原授信额度
	private BigDecimal oldCreditAmountUsed;   //原已用授信额度
	private BigDecimal newCreditAmount;   //现授信额度
	private BigDecimal newCreditAmountUsed;   //现已用授信额度
	private String orderNo;   //订单编号
	private Integer businessType;   //操作业务编码
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人


	public Integer getCustomerRiskLogId(){
		return customerRiskLogId;
	}

	public void setCustomerRiskLogId(Integer customerRiskLogId){
		this.customerRiskLogId = customerRiskLogId;
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

}