package com.lxzl.erp.common.domain.user.pojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDepartment implements Serializable {

	private Integer userDepartmentId;
	private Integer userId;
	private Integer departmentId;
	private String departmentName;
	private Integer dataStatus;
	private String remark;
	private Date createTime;
	private String createUser;
	private Date updateTime;
	private String updateUser;

	public Integer getUserDepartmentId(){
		return userDepartmentId;
	}

	public void setUserDepartmentId(Integer userDepartmentId){
		this.userDepartmentId = userDepartmentId;
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

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}