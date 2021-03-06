package com.lxzl.erp.common.domain.bank.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.bank.AssignGroup;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipDetail extends BasePO {

	@NotNull(message = ErrorCode.BANK_SLIP_DETAIL_ID_NULL,groups = {IdGroup.class, AssignGroup.class})
	private Integer bankSlipDetailId;   //唯一标识
	private String payerName;   //付款人名称
	private BigDecimal tradeAmount;   //交易金额
	private String tradeSerialNo;   //交易流水号
	private Date tradeTime;   //交易日期
	private String tradeMessage;   //交易附言
	private String otherSideAccountNo;   //对方账号
	private String merchantOrderNo;   //商户订单号
	private Integer loanSign;   //借贷标志,1-贷（收入），2-借（支出）
	private Integer detailStatus;   //明细状态，1-未认领，2-已认领，3-已确定，4-忽略
	private String detailJson;   //明细json数据
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Integer bankSlipId;   //银行对公流水表id
	private Integer subCompanyId;  //属地化分公司ID
	private Integer isLocalization;  //是否已属地化,0-否，1-是[总公司时有值]
	private String subCompanyName;  //属地化分公司名称
	private Integer ownerSubCompanyId;  //数据归属公司id
	private String ownerSubCompanyName;  //数据归属公司名称
	private Integer bankSlipBankType;  //银行类型
	private String customerSubCompanyNameStringList;  //所有认领客户对应的公司名称

	public String getCustomerSubCompanyNameStringList() {
		return customerSubCompanyNameStringList;
	}

	public void setCustomerSubCompanyNameStringList(String customerSubCompanyNameStringList) {
		this.customerSubCompanyNameStringList = customerSubCompanyNameStringList;
	}

	private List<BankSlipClaim> bankSlipClaimList;

	public List<BankSlipClaim> getBankSlipClaimList() {
		return bankSlipClaimList;
	}

	public String getSubCompanyName() {
		return subCompanyName;
	}

	public void setSubCompanyName(String subCompanyName) {
		this.subCompanyName = subCompanyName;
	}

	public Integer getOwnerSubCompanyId() {
		return ownerSubCompanyId;
	}

	public void setOwnerSubCompanyId(Integer ownerSubCompanyId) {
		this.ownerSubCompanyId = ownerSubCompanyId;
	}

	public String getOwnerSubCompanyName() {
		return ownerSubCompanyName;
	}

	public void setOwnerSubCompanyName(String ownerSubCompanyName) {
		this.ownerSubCompanyName = ownerSubCompanyName;
	}

	public Integer getBankSlipBankType() {
		return bankSlipBankType;
	}

	public void setBankSlipBankType(Integer bankSlipBankType) {
		this.bankSlipBankType = bankSlipBankType;
	}



	public void setBankSlipClaimList(List<BankSlipClaim> bankSlipClaimList) {
		this.bankSlipClaimList = bankSlipClaimList;
	}

	public Integer getSubCompanyId() {
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId) {
		this.subCompanyId = subCompanyId;
	}

	public Integer getIsLocalization() {
		return isLocalization;
	}

	public void setIsLocalization(Integer isLocalization) {
		this.isLocalization = isLocalization;
	}

	public Integer getBankSlipDetailId(){
		return bankSlipDetailId;
	}

	public void setBankSlipDetailId(Integer bankSlipDetailId){
		this.bankSlipDetailId = bankSlipDetailId;
	}

	public String getPayerName(){
		return payerName;
	}

	public void setPayerName(String payerName){
		this.payerName = payerName;
	}

	public BigDecimal getTradeAmount(){
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount){
		this.tradeAmount = tradeAmount;
	}

	public String getTradeSerialNo(){
		return tradeSerialNo;
	}

	public void setTradeSerialNo(String tradeSerialNo){
		this.tradeSerialNo = tradeSerialNo;
	}

	public Date getTradeTime(){
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime){
		this.tradeTime = tradeTime;
	}

	public String getTradeMessage(){
		return tradeMessage;
	}

	public void setTradeMessage(String tradeMessage){
		this.tradeMessage = tradeMessage;
	}

	public String getOtherSideAccountNo(){
		return otherSideAccountNo;
	}

	public void setOtherSideAccountNo(String otherSideAccountNo){
		this.otherSideAccountNo = otherSideAccountNo;
	}

	public String getMerchantOrderNo(){
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo){
		this.merchantOrderNo = merchantOrderNo;
	}

	public Integer getLoanSign(){
		return loanSign;
	}

	public void setLoanSign(Integer loanSign){
		this.loanSign = loanSign;
	}

	public Integer getDetailStatus(){
		return detailStatus;
	}

	public void setDetailStatus(Integer detailStatus){
		this.detailStatus = detailStatus;
	}

	public String getDetailJson(){
		return detailJson;
	}

	public void setDetailJson(String detailJson){
		this.detailJson = detailJson;
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

	public Integer getBankSlipId(){
		return bankSlipId;
	}

	public void setBankSlipId(Integer bankSlipId){
		this.bankSlipId = bankSlipId;
	}

}