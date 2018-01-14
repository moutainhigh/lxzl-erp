package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingCustomerDO  extends BaseDO {

	private Integer id;
	private String erpCustomerCode;
	private String k3CustomerCode;
	private String customerName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getErpCustomerCode(){
		return erpCustomerCode;
	}

	public void setErpCustomerCode(String erpCustomerCode){
		this.erpCustomerCode = erpCustomerCode;
	}

	public String getK3CustomerCode(){
		return k3CustomerCode;
	}

	public void setK3CustomerCode(String k3CustomerCode){
		this.k3CustomerCode = k3CustomerCode;
	}

	public String getCustomerName(){
		return customerName;
	}

	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

}