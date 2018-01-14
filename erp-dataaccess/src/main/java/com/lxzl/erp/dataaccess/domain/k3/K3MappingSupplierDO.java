package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingSupplierDO  extends BaseDO {

	private Integer id;
	private String erpSupplierCode;
	private String k3SupplierCode;
	private String supplierName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getErpSupplierCode(){
		return erpSupplierCode;
	}

	public void setErpSupplierCode(String erpSupplierCode){
		this.erpSupplierCode = erpSupplierCode;
	}

	public String getK3SupplierCode(){
		return k3SupplierCode;
	}

	public void setK3SupplierCode(String k3SupplierCode){
		this.k3SupplierCode = k3SupplierCode;
	}

	public String getSupplierName(){
		return supplierName;
	}

	public void setSupplierName(String supplierName){
		this.supplierName = supplierName;
	}

}