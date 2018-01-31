package com.lxzl.erp.dataaccess.domain.statementOrderCorrect;

import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class StatementOrderCorrectDO  extends BaseDO {

	private Integer id;
	private String statementCorrectNo;
	private Integer statementOrderId;
	private BigDecimal statementCorrectAmount;
	private String statementCorrectReason;
	private Integer statementOrderCorrectStatus;
	private Integer dataStatus;
	private String remark;
	private Date statementCorrectSuccessTime;
	private String statementCorrectFailReason;
	private Integer statementOrderDetailId;   //结算单项ID

	private StatementOrderDetailDO statementOrderDetailDO;

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

	public Integer getStatementOrderDetailId() {
		return statementOrderDetailId;
	}

	public void setStatementOrderDetailId(Integer statementOrderDetailId) {
		this.statementOrderDetailId = statementOrderDetailId;
	}

	public StatementOrderDetailDO getStatementOrderDetailDO() {
		return statementOrderDetailDO;
	}

	public void setStatementOrderDetailDO(StatementOrderDetailDO statementOrderDetailDO) {
		this.statementOrderDetailDO = statementOrderDetailDO;
	}
}