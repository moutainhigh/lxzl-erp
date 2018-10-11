package com.lxzl.erp.common.domain.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.CancelGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSysDataPrivilege extends BasePO {

	private Integer userSysDataPrivilegeId;   //
	@NotNull(message = ErrorCode.USER_SYS_DATA_PRIVILEGE_USER_ID_NOT_NULL , groups = {AddGroup.class,CancelGroup.class})
	private Integer userId;   //
	@NotNull(message = ErrorCode.USER_SYS_DATA_PRIVILEGE_role_ID_NOT_NULL , groups = {AddGroup.class})
	private Integer roleId;   //角色id
	@NotNull(message = ErrorCode.USER_SYS_DATA_PRIVILEGE_PRIVILEGE_TYPE_NOT_NULL , groups = {AddGroup.class})
	private Integer privilegeType;   //数据权限类型,1-可查看
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getUserSysDataPrivilegeId(){
		return userSysDataPrivilegeId;
	}

	public void setUserSysDataPrivilegeId(Integer userSysDataPrivilegeId){
		this.userSysDataPrivilegeId = userSysDataPrivilegeId;
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

}