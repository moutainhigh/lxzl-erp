package com.lxzl.erp.dataaccess.domain.user;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/12/24.
 * Time: 13:54.
 */
public class RoleDO extends BaseDO {

    private Integer id;
    private String roleName;
    private Integer departmentId;
    private String departmentName;
    private String roleDesc;
    private Integer isSuperAdmin;
    private Integer dataStatus;
    private String remark;


    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Integer getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(Integer isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
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
