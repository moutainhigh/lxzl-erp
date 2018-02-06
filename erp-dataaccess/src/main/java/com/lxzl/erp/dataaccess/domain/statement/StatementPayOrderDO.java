package com.lxzl.erp.dataaccess.domain.statement;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class StatementPayOrderDO  extends BaseDO {

	private Integer id;
	private String statementPayOrderNo;
	private Integer statementOrderId;
	private Integer payType;
	private Integer payStatus;
	private String paymentOrderNo;
	private BigDecimal payAmount;
	private BigDecimal payRentAmount;
	private BigDecimal payRentDepositAmount;
	private BigDecimal payDepositAmount;
	private BigDecimal otherAmount;
	private BigDecimal overdueAmount;
	private Date payTime;
	private Date endTime;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public BigDecimal getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(BigDecimal overdueAmount) {
		this.overdueAmount = overdueAmount;
	}
}