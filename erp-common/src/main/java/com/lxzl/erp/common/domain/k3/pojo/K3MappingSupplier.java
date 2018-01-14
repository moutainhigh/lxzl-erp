package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingSupplier extends BasePO {

	private Integer k3MappingSupplierId;   //唯一标识
	private String erpSupplierCode;   //erp的供应商编码
	private String k3SupplierCode;   //K3供应商编码
	private String supplierName;   //供应商名称


	public Integer getK3MappingSupplierId(){
		return k3MappingSupplierId;
	}

	public void setK3MappingSupplierId(Integer k3MappingSupplierId){
		this.k3MappingSupplierId = k3MappingSupplierId;
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