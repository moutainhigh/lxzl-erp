package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingIndustryDO  extends BaseDO {

	private Integer id;
	private String erpIndustryCode;
	private String k3IndustryCode;
	private String industryName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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