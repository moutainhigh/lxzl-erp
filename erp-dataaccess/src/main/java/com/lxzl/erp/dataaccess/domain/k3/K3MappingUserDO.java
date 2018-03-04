package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingUserDO  extends BaseDO {

	private Integer id;
	private String erpUserCode;
	private String k3UserCode;
	private String userRealName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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