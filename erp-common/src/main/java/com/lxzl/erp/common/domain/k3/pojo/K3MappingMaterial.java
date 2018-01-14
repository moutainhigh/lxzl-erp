package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingMaterial extends BasePO {

	private Integer k3MappingMaterialId;   //唯一标识
	private String erpMaterialCode;   //erp商品编码
	private String k3MaterialCode;   //K3商品编码
	private String materialName;   //商品名称


	public Integer getK3MappingMaterialId(){
		return k3MappingMaterialId;
	}

	public void setK3MappingMaterialId(Integer k3MappingMaterialId){
		this.k3MappingMaterialId = k3MappingMaterialId;
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