package com.lxzl.erp.dataaccess.domain.user;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/12/26.
 * Time: 9:21.
 */
public class UserRoleDO extends BaseDO {

    private Integer id;

    private Integer userId;

    private Integer isSuperAdmin;

    private Integer roleId;

    private String roleName;

    private Integer dataStatus;

    private Integer remark;

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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getRemark() {
        return remark;
    }

    public void setRemark(Integer remark) {
        this.remark = remark;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(Integer isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }
}
