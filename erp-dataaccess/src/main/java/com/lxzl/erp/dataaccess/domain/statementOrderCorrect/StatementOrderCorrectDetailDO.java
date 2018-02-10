package com.lxzl.erp.dataaccess.domain.statementOrderCorrect;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class StatementOrderCorrectDetailDO  extends BaseDO {

	private Integer id;
	private Integer statementOrderCorrectId;
	private Integer statementOrderDetailId;
	private Integer statementOrderDetailType;
	private BigDecimal statementCorrectAmount;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getStatementOrderCorrectId(){
		return statementOrderCorrectId;
	}

	public void setStatementOrderCorrectId(Integer statementOrderCorrectId){
		this.statementOrderCorrectId = statementOrderCorrectId;
	}

	public Integer getStatementOrderDetailId(){
		return statementOrderDetailId;
	}

	public void setStatementOrderDetailId(Integer statementOrderDetailId){
		this.statementOrderDetailId = statementOrderDetailId;
	}

	public Integer getStatementOrderDetailType() {
		return statementOrderDetailType;
	}

	public void setStatementOrderDetailType(Integer statementOrderDetailType) {
		this.statementOrderDetailType = statementOrderDetailType;
	}

	public BigDecimal getStatementCorrectAmount(){
		return statementCorrectAmount;
	}

	public void setStatementCorrectAmount(BigDecimal statementCorrectAmount){
		this.statementCorrectAmount = statementCorrectAmount;
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

}