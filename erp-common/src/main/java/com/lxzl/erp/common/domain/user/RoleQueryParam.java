package com.lxzl.erp.common.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2017/1/4.
 * Time: 14:11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleQueryParam extends BasePageParam {

    private Integer roleId;
    private String roleName;
    private Integer departmentId;
    private Integer subCompanyId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }
}
