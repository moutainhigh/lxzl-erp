package com.lxzl.erp.common.domain.bank.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipDetail extends BasePO {

	@NotNull(message = ErrorCode.BANK_SLIP_DETAIL_ID_NULL,groups = {IdGroup.class})
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