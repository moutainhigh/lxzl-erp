package com.lxzl.erp.dataaccess.domain.user;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

/**
 * 用户权限do
 */
public class UserPrevDO extends BaseDO {

	private Integer id;
	/**
	 *  用户id
	 */
	private Integer userId;

	private Integer roleId;

	/**
	 *  权限类型：1：手机号码权限
	 */
	private Integer prevType;

	/**
	 *
	 */
    private Integer dataStatus;

	/**
	 *
	 */
	private String remark;

	/**
	 * 用户名
	 */
	private String userName;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getId() {

		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPrevType() {
		return prevType;
	}

	public void setPrevType(Integer prevType) {
		this.prevType = prevType;
	}

	public Integer getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
