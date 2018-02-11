package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingProductDO  extends BaseDO {

	private Integer id;
	private String erpProductCode;
	private String erpSkuCode;
	private String k3ProductCode;
	private String k3SkuCode;
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

	public String getErpSkuCode() {
		return erpSkuCode;
	}

	public void setErpSkuCode(String erpSkuCode) {
		this.erpSkuCode = erpSkuCode;
	}

	public String getK3SkuCode() {
		return k3SkuCode;
	}

	public void setK3SkuCode(String k3SkuCode) {
		this.k3SkuCode = k3SkuCode;
	}
}