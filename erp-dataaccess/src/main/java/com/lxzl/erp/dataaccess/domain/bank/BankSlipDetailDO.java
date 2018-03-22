package com.lxzl.erp.dataaccess.domain.bank;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

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

}