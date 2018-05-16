package com.lxzl.erp.dataaccess.domain.bank;

import com.lxzl.erp.common.domain.bank.pojo.BankSlipDetail;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BankSlipDetailDO  extends BaseDO {

	private Integer id;
	private String payerName;
	private BigDecimal tradeAmount;
	private String tradeSerialNo;
	private Date tradeTime;
	private String tradeMessage;
	private String otherSideAccountNo;
	private String merchantOrderNo;
	private Integer loanSign;
	private Integer detailStatus;
	private String detailJson;
	private Integer dataStatus;
	private String remark;
	private Integer bankSlipId;
	private Integer subCompanyId;
	private String subCompanyName;
	private Integer isLocalization;
	private Integer ownerSubCompanyId;  //数据归属公司id
	private String ownerSubCompanyName;  //数据归属公司名称
	private Integer bankSlipBankType;  //银行类型

	private List<BankSlipClaimDO> bankSlipClaimDOList;


	public List<BankSlipClaimDO> getBankSlipClaimDOList() {
		return bankSlipClaimDOList;
	}

	public void setBankSlipClaimDOList(List<BankSlipClaimDO> bankSlipClaimDOList) {
		this.bankSlipClaimDOList = bankSlipClaimDOList;
	}

	public String getSubCompanyName() {
		return subCompanyName;
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

	public void setSubCompanyName(String subCompanyName) {
		this.subCompanyName = subCompanyName;
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

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public Integer getBankSlipId(){
		return bankSlipId;
	}

	public void setBankSlipId(Integer bankSlipId){
		this.bankSlipId = bankSlipId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BankSlipDetailDO)) return false;

		BankSlipDetailDO that = (BankSlipDetailDO) o;
		if (getPayerName() != null ? !getPayerName().equals(that.getPayerName()) : that.getPayerName() != null)
			return false;
		if (getTradeAmount() != null ? !getTradeAmount().setScale(2,BigDecimal.ROUND_UP).equals(that.getTradeAmount().setScale(2,BigDecimal.ROUND_UP)) : that.getTradeAmount() != null)
			return false;
		if (getTradeSerialNo() != null ? !getTradeSerialNo().equals(that.getTradeSerialNo()) : that.getTradeSerialNo() != null)
			return false;
		if (getTradeTime() != null ? !getTradeTime().equals(that.getTradeTime()) : that.getTradeTime() != null)
			return false;
		if (getOtherSideAccountNo() != null ? !getOtherSideAccountNo().equals(that.getOtherSideAccountNo()) : that.getOtherSideAccountNo() != null)
			return false;
		if (getMerchantOrderNo() != null ? !getMerchantOrderNo().equals(that.getMerchantOrderNo()) : that.getMerchantOrderNo() != null)
			return false;
		if (getLoanSign() != null ? !getLoanSign().equals(that.getLoanSign()) : that.getLoanSign() != null)
			return false;
		if (getDataStatus() != null ? !getDataStatus().equals(that.getDataStatus()) : that.getDataStatus() != null)
			return false;
		return true;
	}

}