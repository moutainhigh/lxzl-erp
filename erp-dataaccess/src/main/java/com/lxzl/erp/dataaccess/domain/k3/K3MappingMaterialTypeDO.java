package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingMaterialTypeDO  extends BaseDO {

	private Integer id;
	private String erpMaterialTypeCode;
	private String k3MaterialTypeCode;
	private String materialTypeName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getErpMaterialTypeCode(){
		return erpMaterialTypeCode;
	}

	public void setErpMaterialTypeCode(String erpMaterialTypeCode){
		this.erpMaterialTypeCode = erpMaterialTypeCode;
	}

	public String getK3MaterialTypeCode(){
		return k3MaterialTypeCode;
	}

	public void setK3MaterialTypeCode(String k3MaterialTypeCode){
		this.k3MaterialTypeCode = k3MaterialTypeCode;
	}

	public String getMaterialTypeName(){
		return materialTypeName;
	}

	public void setMaterialTypeName(String materialTypeName){
		this.materialTypeName = materialTypeName;
	}

}