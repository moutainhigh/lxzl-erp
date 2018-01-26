package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingMaterialType extends BasePO {

	private Integer k3MappingMaterialTypeId;   //唯一标识
	private String erpMaterialTypeCode;   //erp配件类型编码
	private String k3MaterialTypeCode;   //K3配件类型编码
	private String materialTypeName;   //配件类型名称


	public Integer getK3MappingMaterialTypeId(){
		return k3MappingMaterialTypeId;
	}

	public void setK3MappingMaterialTypeId(Integer k3MappingMaterialTypeId){
		this.k3MappingMaterialTypeId = k3MappingMaterialTypeId;
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