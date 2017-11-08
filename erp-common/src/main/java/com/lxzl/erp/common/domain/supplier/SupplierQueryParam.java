package com.lxzl.erp.common.domain.supplier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierQueryParam extends PageQuery implements Serializable {

	private Integer supplierId;   //字典ID，唯一
	private String supplierName;   //供应商名称


	public Integer getSupplierId(){
		return supplierId;
	}

	public void setSupplierId(Integer supplierId){
		this.supplierId = supplierId;
	}

	public String getSupplierName(){
		return supplierName;
	}

	public void setSupplierName(String supplierName){
		this.supplierName = supplierName;
	}


}