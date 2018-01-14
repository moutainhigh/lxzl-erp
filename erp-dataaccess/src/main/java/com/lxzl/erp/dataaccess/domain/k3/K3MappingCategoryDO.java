package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingCategoryDO  extends BaseDO {

	private Integer id;
	private String erpCategoryCode;
	private String k3CategoryCode;
	private String categoryName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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