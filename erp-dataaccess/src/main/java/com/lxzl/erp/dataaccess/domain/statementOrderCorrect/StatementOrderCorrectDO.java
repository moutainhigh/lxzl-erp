package com.lxzl.erp.dataaccess.domain.statementOrderCorrect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderCorrectDO  extends BaseDO {

	private Integer id;
	private String statementCorrectNo;
	private Integer statementOrderId;
	private BigDecimal statementCorrectAmount;
	private BigDecimal statementCorrectRentAmount;
	private BigDecimal statementCorrectRentDepositAmount;
	private BigDecimal statementCorrectDepositAmount;
	private BigDecimal statementCorrectOtherAmount;
	private BigDecimal statementCorrectOverdueAmount;
	private String statementCorrectReason;
	private Integer statementOrderCorrectStatus;
	private Integer dataStatus;
	private String remark;
	private Date statementCorrectSuccessTime;
	private String statementCorrectFailReason;
	private Integer statementOrderItemId;   //结算单订单项ID
	private Integer statementOrderReferId;   //结算单订单项ID

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getStatementCorrectNo(){
		return statementCorrectNo;
	}

	public void setStatementCorrectNo(String statementCorrectNo){
		this.statementCorrectNo = statementCorrectNo;
	}

	public Integer getStatementOrderId(){
		return statementOrderId;
	}

	public void setStatementOrderId(Integer statementOrderId){
		this.statementOrderId = statementOrderId;
	}

	public BigDecimal getStatementCorrectAmount(){
		return statementCorrectAmount;
	}

	public void setStatementCorrectAmount(BigDecimal statementCorrectAmount){
		this.statementCorrectAmount = statementCorrectAmount;
	}

	public String getStatementCorrectReason(){
		return statementCorrectReason;
	}

	public void setStatementCorrectReason(String statementCorrectReason){
		this.statementCorrectReason = statementCorrectReason;
	}

	public Integer getStatementOrderCorrectStatus(){
		return statementOrderCorrectStatus;
	}

	public void setStatementOrderCorrectStatus(Integer statementOrderCorrectStatus){
		this.statementOrderCorrectStatus = statementOrderCorrectStatus;
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

	public Date getStatementCorrectSuccessTime(){
		return statementCorrectSuccessTime;
	}

	public void setStatementCorrectSuccessTime(Date statementCorrectSuccessTime){
		this.statementCorrectSuccessTime = statementCorrectSuccessTime;
	}

	public String getStatementCorrectFailReason(){
		return statementCorrectFailReason;
	}

	public void setStatementCorrectFailReason(String statementCorrectFailReason){
		this.statementCorrectFailReason = statementCorrectFailReason;
	}

	public BigDecimal getStatementCorrectRentAmount() {
		return statementCorrectRentAmount;
	}

	public void setStatementCorrectRentAmount(BigDecimal statementCorrectRentAmount) {
		this.statementCorrectRentAmount = statementCorrectRentAmount;
	}

	public BigDecimal getStatementCorrectDepositAmount() {
		return statementCorrectDepositAmount;
	}

	public void setStatementCorrectDepositAmount(BigDecimal statementCorrectDepositAmount) {
		this.statementCorrectDepositAmount = statementCorrectDepositAmount;
	}

	public BigDecimal getStatementCorrectOtherAmount() {
		return statementCorrectOtherAmount;
	}

	public void setStatementCorrectOtherAmount(BigDecimal statementCorrectOtherAmount) {
		this.statementCorrectOtherAmount = statementCorrectOtherAmount;
	}

	public BigDecimal getStatementCorrectOverdueAmount() {
		return statementCorrectOverdueAmount;
	}

	public void setStatementCorrectOverdueAmount(BigDecimal statementCorrectOverdueAmount) {
		this.statementCorrectOverdueAmount = statementCorrectOverdueAmount;
	}

	public Integer getStatementOrderItemId() {
		return statementOrderItemId;
	}

	public void setStatementOrderItemId(Integer statementOrderItemId) {
		this.statementOrderItemId = statementOrderItemId;
	}

	public BigDecimal getStatementCorrectRentDepositAmount() {
		return statementCorrectRentDepositAmount;
	}

	public void setStatementCorrectRentDepositAmount(BigDecimal statementCorrectRentDepositAmount) {
		this.statementCorrectRentDepositAmount = statementCorrectRentDepositAmount;
	}

	public Integer getStatementOrderReferId() {
		return statementOrderReferId;
	}

	public void setStatementOrderReferId(Integer statementOrderReferId) {
		this.statementOrderReferId = statementOrderReferId;
	}
}