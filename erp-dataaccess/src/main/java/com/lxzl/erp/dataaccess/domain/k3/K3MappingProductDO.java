package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingProductDO  extends BaseDO {

	private Integer id;
	private String erpProductCode;
	private String k3ProductCode;
	private String productName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getErpProductCode(){
		return erpProductCode;
	}

	public void setErpProductCode(String erpProductCode){
		this.erpProductCode = erpProductCode;
	}

	public String getK3ProductCode(){
		return k3ProductCode;
	}

	public void setK3ProductCode(String k3ProductCode){
		this.k3ProductCode = k3ProductCode;
	}

	public String getProductName(){
		return productName;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

}