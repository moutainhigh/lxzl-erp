package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingBrandDO  extends BaseDO {

	private Integer id;
	private String erpBrandCode;
	private String k3BrandCode;
	private String brandName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getErpBrandCode(){
		return erpBrandCode;
	}

	public void setErpBrandCode(String erpBrandCode){
		this.erpBrandCode = erpBrandCode;
	}

	public String getK3BrandCode(){
		return k3BrandCode;
	}

	public void setK3BrandCode(String k3BrandCode){
		this.k3BrandCode = k3BrandCode;
	}

	public String getBrandName(){
		return brandName;
	}

	public void setBrandName(String brandName){
		this.brandName = brandName;
	}

}