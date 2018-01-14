package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingCategory extends BasePO {

	private Integer k3MappingCategoryId;   //唯一标识
	private String erpCategoryCode;   //erp的分类编码
	private String k3CategoryCode;   //K3分类编码
	private String categoryName;   //分类名称


	public Integer getK3MappingCategoryId(){
		return k3MappingCategoryId;
	}

	public void setK3MappingCategoryId(Integer k3MappingCategoryId){
		this.k3MappingCategoryId = k3MappingCategoryId;
	}

	public String getErpCategoryCode(){
		return erpCategoryCode;
	}

	public void setErpCategoryCode(String erpCategoryCode){
		this.erpCategoryCode = erpCategoryCode;
	}

	public String getK3CategoryCode(){
		return k3CategoryCode;
	}

	public void setK3CategoryCode(String k3CategoryCode){
		this.k3CategoryCode = k3CategoryCode;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

}