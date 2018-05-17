package com.lxzl.erp.dataaccess.domain.bank;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class BankSlipDetailOperationLogDO  extends BaseDO {

	private Integer id;
	private Integer bankSlipDetailId;
	private Integer operationType;
	private String operationContent;
	private Integer dataStatus;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getBankSlipDetailId(){
		return bankSlipDetailId;
	}

	public void setBankSlipDetailId(Integer bankSlipDetailId){
		this.bankSlipDetailId = bankSlipDetailId;
	}

	public Integer getOperationType(){
		return operationType;
	}

	public void setOperationType(Integer operationType){
		this.operationType = operationType;
	}

	public String getOperationContent(){
		return operationContent;
	}

	public void setOperationContent(String operationContent){
		this.operationContent = operationContent;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

}