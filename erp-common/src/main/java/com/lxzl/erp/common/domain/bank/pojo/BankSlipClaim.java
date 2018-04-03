package com.lxzl.erp.common.domain.bank.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.bank.ClaimParam;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.bank.ClaimBankSlipDetailGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipClaim extends BasePO {

	private Integer bankSlipClaimId;   //唯一标识
	@NotNull(message = ErrorCode.BANK_SLIP_DETAIL_ID_NULL,groups = {ClaimBankSlipDetailGroup.class})
	private Integer bankSlipDetailId;   //银行对公流水明细ID
	private String otherSideAccountNo;   //对方账号
	private String customerNo;   //客戶编码
	private BigDecimal claimAmount;   //认领金额
	private Long claimSerialNo;   //认领流水号（时间戳）
	private Integer rechargeStatus;   //充值状态，0-初始化，1-正在充值，2-充值成功，3-充值失败
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private String customerName;   //客户名称
	private Integer customerId;  //客户id

	@Valid
	List<ClaimParam> claimParam;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public List<ClaimParam> getClaimParam() {
		return claimParam;
	}

	public void setClaimParam(List<ClaimParam> claimParam) {
		this.claimParam = claimParam;
	}

	public Integer getBankSlipClaimId(){
		return bankSlipClaimId;
	}

	public void setBankSlipClaimId(Integer bankSlipClaimId){
		this.bankSlipClaimId = bankSlipClaimId;
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