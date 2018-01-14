package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingBrand extends BasePO {

	private Integer k3MappingBrandId;   //唯一标识
	private String erpBrandCode;   //erp的品牌编码
	private String k3BrandCode;   //K3品牌编码
	private String brandName;   //品牌名称


	public Integer getK3MappingBrandId(){
		return k3MappingBrandId;
	}

	public void setK3MappingBrandId(Integer k3MappingBrandId){
		this.k3MappingBrandId = k3MappingBrandId;
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