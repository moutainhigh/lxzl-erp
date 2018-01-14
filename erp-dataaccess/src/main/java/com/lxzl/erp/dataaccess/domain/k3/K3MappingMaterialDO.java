package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingMaterialDO  extends BaseDO {

	private Integer id;
	private String erpMaterialCode;
	private String k3MaterialCode;
	private String materialName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getErpMaterialCode(){
		return erpMaterialCode;
	}

	public void setErpMaterialCode(String erpMaterialCode){
		this.erpMaterialCode = erpMaterialCode;
	}

	public String getK3MaterialCode(){
		return k3MaterialCode;
	}

	public void setK3MaterialCode(String k3MaterialCode){
		this.k3MaterialCode = k3MaterialCode;
	}

	public String getMaterialName(){
		return materialName;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

}