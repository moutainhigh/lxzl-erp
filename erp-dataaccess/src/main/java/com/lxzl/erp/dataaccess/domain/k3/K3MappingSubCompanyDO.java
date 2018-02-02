package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingSubCompanyDO  extends BaseDO {

	private Integer id;
	private String erpSubCompanyCode;
	private String k3SubCompanyCode;
	private String subCompanyName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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