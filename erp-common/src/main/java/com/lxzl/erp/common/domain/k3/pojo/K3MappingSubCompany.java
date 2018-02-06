package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingSubCompany extends BasePO {

	private Integer k3MappingSubCompanyId;   //唯一标识
	private String erpSubCompanyCode;   //erp的分公司编码
	private String k3SubCompanyCode;   //K3分公司编码
	private String subCompanyName;   //分公司名称


	public Integer getK3MappingSubCompanyId(){
		return k3MappingSubCompanyId;
	}

	public void setK3MappingSubCompanyId(Integer k3MappingSubCompanyId){
		this.k3MappingSubCompanyId = k3MappingSubCompanyId;
	}

	public String getErpSubCompanyCode(){
		return erpSubCompanyCode;
	}

	public void setErpSubCompanyCode(String erpSubCompanyCode){
		this.erpSubCompanyCode = erpSubCompanyCode;
	}

	public String getK3SubCompanyCode(){
		return k3SubCompanyCode;
	}

	public void setK3SubCompanyCode(String k3SubCompanyCode){
		this.k3SubCompanyCode = k3SubCompanyCode;
	}

	public String getSubCompanyName(){
		return subCompanyName;
	}

	public void setSubCompanyName(String subCompanyName){
		this.subCompanyName = subCompanyName;
	}

}