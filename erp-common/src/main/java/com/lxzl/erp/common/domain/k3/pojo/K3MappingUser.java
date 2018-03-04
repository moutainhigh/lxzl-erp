package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingUser extends BasePO {

	private Integer k3MappingUserId;   //唯一标识
	private String erpUserCode;   //erp的用户编码
	private String k3UserCode;   //K3用户编码
	private String userRealName;   //用户名称


	public Integer getK3MappingUserId(){
		return k3MappingUserId;
	}

	public void setK3MappingUserId(Integer k3MappingUserId){
		this.k3MappingUserId = k3MappingUserId;
	}

	public String getErpUserCode(){
		return erpUserCode;
	}

	public void setErpUserCode(String erpUserCode){
		this.erpUserCode = erpUserCode;
	}

	public String getK3UserCode(){
		return k3UserCode;
	}

	public void setK3UserCode(String k3UserCode){
		this.k3UserCode = k3UserCode;
	}

	public String getUserRealName(){
		return userRealName;
	}

	public void setUserRealName(String userRealName){
		this.userRealName = userRealName;
	}

}