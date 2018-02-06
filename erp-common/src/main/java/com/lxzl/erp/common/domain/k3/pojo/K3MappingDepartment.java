package com.lxzl.erp.common.domain.k3.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;



@JsonIgnoreProperties(ignoreUnknown = true)
public class K3MappingDepartment extends BasePO {

	private Integer k3MappingDepartmentId;   //唯一标识
	private Integer erpDepartmentId;   //erp的部门ID
	private String k3DepartmentCode;   //K3部门编码
	private String departmentName;   //部门名称
	private Integer subCompanyId;   //erp的分公司ID
	private String subCompanyName;   //分公司名称


	public Integer getK3MappingDepartmentId(){
		return k3MappingDepartmentId;
	}

	public void setK3MappingDepartmentId(Integer k3MappingDepartmentId){
		this.k3MappingDepartmentId = k3MappingDepartmentId;
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