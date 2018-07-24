package com.lxzl.erp.dataaccess.domain.bank;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.math.BigDecimal;

public class BankSlipClaimDO  extends BaseDO {

	private Integer id;
	private Integer bankSlipDetailId;
	private String otherSideAccountNo;
	private String customerNo;
	private BigDecimal claimAmount;
	private Long claimSerialNo;
	private Integer rechargeStatus;
	private Integer dataStatus;
	private String remark;
	private String customerName;  //客户名称
	private String k3CustomerNo;  //k3客户编码
	private String customerSubCompanyName;  //客户对应的公司名称

	public String getCustomerSubCompanyName() {
		return customerSubCompanyName;
	}

	public void setCustomerSubCompanyName(String customerSubCompanyName) {
		this.customerSubCompanyName = customerSubCompanyName;
	}

	public String getK3CustomerNo() {
		return k3CustomerNo;
	}

	public void setK3CustomerNo(String k3CustomerNo) {
		this.k3CustomerNo = k3CustomerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getBankSlipDetailId(){
		return bankSlipDetailId;
	}

	public void setBankSlipDetailId(Integer bankSlipDetailId){
		this.bankSlipDetailId = bankSlipDetailId;
	}

	public String getOtherSideAccountNo(){
		return otherSideAccountNo;
	}

	public void setOtherSideAccountNo(String otherSideAccountNo){
		this.otherSideAccountNo = otherSideAccountNo;
	}

	public String getCustomerNo(){
		return customerNo;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo = customerNo;
	}

	public BigDecimal getClaimAmount(){
		return claimAmount;
	}

	public void setClaimAmount(BigDecimal claimAmount){
		this.claimAmount = claimAmount;
	}

	public Long getClaimSerialNo(){
		return claimSerialNo;
	}

	public void setClaimSerialNo(Long claimSerialNo){
		this.claimSerialNo = claimSerialNo;
	}

	public Integer getRechargeStatus(){
		return rechargeStatus;
	}

	public void setRechargeStatus(Integer rechargeStatus){
		this.rechargeStatus = rechargeStatus;
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