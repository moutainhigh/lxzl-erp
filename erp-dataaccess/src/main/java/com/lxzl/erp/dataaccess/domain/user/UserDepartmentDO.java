package com.lxzl.erp.dataaccess.domain.user;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class UserDepartmentDO  extends BaseDO {

	private Integer id;
	private Integer userId;
	private Integer departmentId;
	private String departmentName;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getUserId(){
		return userId;
	}

	public void setUserId(Integer userId){
		this.userId = userId;
	}

	public Integer getDepartmentId(){
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId){
		this.departmentId = departmentId;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}