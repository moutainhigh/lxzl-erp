package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingCustomer extends BasePO {

	private Integer k3MappingCustomerId;   //唯一标识
	private String erpCustomerCode;   //erp的客户编码
	private String k3CustomerCode;   //K3客户编码
	private String customerName;   //客户名称


	public Integer getK3MappingCustomerId(){
		return k3MappingCustomerId;
	}

	public void setK3MappingCustomerId(Integer k3MappingCustomerId){
		this.k3MappingCustomerId = k3MappingCustomerId;
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