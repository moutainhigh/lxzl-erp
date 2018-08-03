package com.lxzl.erp.common.domain.delayedTask.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class DelayedTaskConfigExportStatement extends BasePO {

	private Integer delayedTaskConfigExportStatementId;   //唯一标识
	private String customerNo;   //客户编号
	private Integer dataStatus;   //状态：0不可用；1可用；2删除


	public Integer getDelayedTaskConfigExportStatementId(){
		return delayedTaskConfigExportStatementId;
	}

	public void setDelayedTaskConfigExportStatementId(Integer delayedTaskConfigExportStatementId){
		this.delayedTaskConfigExportStatementId = delayedTaskConfigExportStatementId;
	}

	public String getCustomerNo(){
		return customerNo;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo = customerNo;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

}