package com.lxzl.erp.dataaccess.domain.delayedTask;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class DelayedTaskConfigExportStatementDO  extends BaseDO {

	private Integer id;
	private String customerNo;
	private Integer dataStatus;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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