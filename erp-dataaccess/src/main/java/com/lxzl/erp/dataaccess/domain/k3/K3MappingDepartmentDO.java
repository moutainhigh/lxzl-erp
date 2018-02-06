package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;



public class K3MappingDepartmentDO  extends BaseDO {

	private Integer id;
	private Integer erpDepartmentId;
	private String k3DepartmentCode;
	private String departmentName;
	private Integer subCompanyId;
	private String subCompanyName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getErpDepartmentId(){
		return erpDepartmentId;
	}

	public void setErpDepartmentId(Integer erpDepartmentId){
		this.erpDepartmentId = erpDepartmentId;
	}

	public String getK3DepartmentCode(){
		return k3DepartmentCode;
	}

	public void setK3DepartmentCode(String k3DepartmentCode){
		this.k3DepartmentCode = k3DepartmentCode;
	}

	public String getDepartmentName(){
		return departmentName;
	}

	public void setDepartmentName(String departmentName){
		this.departmentName = departmentName;
	}

	public Integer getSubCompanyId(){
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId){
		this.subCompanyId = subCompanyId;
	}

	public String getSubCompanyName(){
		return subCompanyName;
	}

	public void setSubCompanyName(String subCompanyName){
		this.subCompanyName = subCompanyName;
	}

}