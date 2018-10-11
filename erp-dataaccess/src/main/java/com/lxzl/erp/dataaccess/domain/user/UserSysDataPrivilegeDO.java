package com.lxzl.erp.dataaccess.domain.user;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class UserSysDataPrivilegeDO  extends BaseDO {

	private Integer id;
	private Integer userId;
	private Integer roleId;
	private Integer privilegeType;
	private Integer dataStatus;

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

	public Integer getRoleId(){
		return roleId;
	}

	public void setRoleId(Integer roleId){
		this.roleId = roleId;
	}

	public Integer getPrivilegeType(){
		return privilegeType;
	}

	public void setPrivilegeType(Integer privilegeType){
		this.privilegeType = privilegeType;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

}