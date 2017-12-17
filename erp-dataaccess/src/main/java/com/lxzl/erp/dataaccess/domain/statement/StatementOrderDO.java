package com.lxzl.erp.dataaccess.domain.statement;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

public class StatementOrderDO  extends BaseDO {

	private Integer id;
	private String statementOrderNo;
	private Integer customerId;
	private Date statementExpectPayTime;
	private BigDecimal statementAmount;
	private BigDecimal statementPaidAmount;
	private Date statementPaidTime;
	private BigDecimal statementOverdueAmount;
	private Integer statementStatus;
	private Date statementStartTime;
	private Date statementEndTime;
	private Integer dataStatus;
	private String remark;
	private List<StatementOrderDetailDO> statementOrderDetailDOList;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getStatementOrderNo(){
		return statementOrderNo;
	}

	public void setStatementOrderNo(String statementOrderNo){
		this.statementOrderNo = statementOrderNo;
	}

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public BigDecimal getStatementAmount(){
		return statementAmount;
	}

	public void setStatementAmount(BigDecimal statementAmount){
		this.statementAmount = statementAmount;
	}

	public Integer getStatementStatus(){
		return statementStatus;
	}

	public void setStatementStatus(Integer statementStatus){
		this.statementStatus = statementStatus;
	}

	public Date getStatementStartTime(){
		return statementStartTime;
	}

	public void setStatementStartTime(Date statementStartTime){
		this.statementStartTime = statementStartTime;
	}

	public Date getStatementEndTime(){
		return statementEndTime;
	}

	public void setStatementEndTime(Date statementEndTime){
		this.statementEndTime = statementEndTime;
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

	public List<StatementOrderDetailDO> getStatementOrderDetailDOList() {
		return statementOrderDetailDOList;
	}

	public void setStatementOrderDetailDOList(List<StatementOrderDetailDO> statementOrderDetailDOList) {
		this.statementOrderDetailDOList = statementOrderDetailDOList;
	}

	public BigDecimal getStatementOverdueAmount() {
		return statementOverdueAmount;
	}

	public void setStatementOverdueAmount(BigDecimal statementOverdueAmount) {
		this.statementOverdueAmount = statementOverdueAmount;
	}

	public Date getStatementExpectPayTime() {
		return statementExpectPayTime;
	}

	public void setStatementExpectPayTime(Date statementExpectPayTime) {
		this.statementExpectPayTime = statementExpectPayTime;
	}

	public BigDecimal getStatementPaidAmount() {
		return statementPaidAmount;
	}

	public void setStatementPaidAmount(BigDecimal statementPaidAmount) {
		this.statementPaidAmount = statementPaidAmount;
	}

	public Date getStatementPaidTime() {
		return statementPaidTime;
	}

	public void setStatementPaidTime(Date statementPaidTime) {
		this.statementPaidTime = statementPaidTime;
	}
}