package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingIndustry extends BasePO {

	private Integer k3MappingIndustryId;   //唯一标识
	private String erpIndustryCode;   //erp的分类编码
	private String k3IndustryCode;   //K3分类编码
	private String industryName;   //分类名称


	public Integer getK3MappingIndustryId(){
		return k3MappingIndustryId;
	}

	public void setK3MappingIndustryId(Integer k3MappingIndustryId){
		this.k3MappingIndustryId = k3MappingIndustryId;
	}

	public String getErpIndustryCode(){
		return erpIndustryCode;
	}

	public void setErpIndustryCode(String erpIndustryCode){
		this.erpIndustryCode = erpIndustryCode;
	}

	public String getK3IndustryCode(){
		return k3IndustryCode;
	}

	public void setK3IndustryCode(String k3IndustryCode){
		this.k3IndustryCode = k3IndustryCode;
	}

	public String getIndustryName(){
		return industryName;
	}

	public void setIndustryName(String industryName){
		this.industryName = industryName;
	}

}