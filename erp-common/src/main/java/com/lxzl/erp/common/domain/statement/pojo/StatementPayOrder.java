package com.lxzl.erp.common.domain.statement.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementPayOrder extends BasePO {

	private Integer statementPayOrderId;   //唯一标识
	private String statementPayOrderNo;   //结算单支付编码
	private Integer statementOrderId;   //结算单ID
	private Integer payType;   //支付方式类型，1余额支付，2微信支付
	private Integer payStatus;   //支付状态，详见paystatus
	private String paymentOrderNo;   //支付系统支付编号
	private BigDecimal payAmount;   //支付金额
	private BigDecimal payRentAmount;   //支付租金金额
	private BigDecimal payRentDepositAmount;   //支付租金押金金额
	private BigDecimal payDepositAmount;   //支付押金金额
	private BigDecimal otherAmount;   //支付其他金额
	private BigDecimal overdueAmount;	// 支付逾期金额
	private Date payTime;   //支付时间，即发起支付时间
	private Date endTime;   //结束时间，即收到返回时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getStatementPayOrderId(){
		return statementPayOrderId;
	}

	public void setStatementPayOrderId(Integer statementPayOrderId){
		this.statementPayOrderId = statementPayOrderId;
	}

	public String getStatementPayOrderNo(){
		return statementPayOrderNo;
	}

	public void setStatementPayOrderNo(String statementPayOrderNo){
		this.statementPayOrderNo = statementPayOrderNo;
	}

	public Integer getStatementOrderId(){
		return statementOrderId;
	}

	public void setStatementOrderId(Integer statementOrderId){
		this.statementOrderId = statementOrderId;
	}

	public Integer getPayType(){
		return payType;
	}

	public void setPayType(Integer payType){
		this.payType = payType;
	}

	public Integer getPayStatus(){
		return payStatus;
	}

	public void setPayStatus(Integer payStatus){
		this.payStatus = payStatus;
	}

	public String getPaymentOrderNo(){
		return paymentOrderNo;
	}

	public void setPaymentOrderNo(String paymentOrderNo){
		this.paymentOrderNo = paymentOrderNo;
	}

	public BigDecimal getPayAmount(){
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount){
		this.payAmount = payAmount;
	}

	public BigDecimal getPayRentAmount(){
		return payRentAmount;
	}

	public void setPayRentAmount(BigDecimal payRentAmount){
		this.payRentAmount = payRentAmount;
	}

	public BigDecimal getPayRentDepositAmount(){
		return payRentDepositAmount;
	}

	public void setPayRentDepositAmount(BigDecimal payRentDepositAmount){
		this.payRentDepositAmount = payRentDepositAmount;
	}

	public BigDecimal getPayDepositAmount(){
		return payDepositAmount;
	}

	public void setPayDepositAmount(BigDecimal payDepositAmount){
		this.payDepositAmount = payDepositAmount;
	}

	public BigDecimal getOtherAmount(){
		return otherAmount;
	}

	public void setOtherAmount(BigDecimal otherAmount){
		this.otherAmount = otherAmount;
	}

	public Date getPayTime(){
		return payTime;
	}

	public void setPayTime(Date payTime){
		this.payTime = payTime;
	}

	public Date getEndTime(){
		return endTime;
	}

	public void setEndTime(Date endTime){
		this.endTime = endTime;
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

	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}
}