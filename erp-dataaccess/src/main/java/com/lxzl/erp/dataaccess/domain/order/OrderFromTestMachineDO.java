package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class OrderFromTestMachineDO  extends BaseDO {

	private Integer id;
	private String testMachineOrderNo;
	private String orderNo;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getTestMachineOrderNo(){
		return testMachineOrderNo;
	}

	public void setTestMachineOrderNo(String testMachineOrderNo){
		this.testMachineOrderNo = testMachineOrderNo;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
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