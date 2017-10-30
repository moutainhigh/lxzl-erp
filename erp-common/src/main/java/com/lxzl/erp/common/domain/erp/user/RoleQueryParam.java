package com.lxzl.erp.common.domain.erp.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2017/1/4.
 * Time: 14:11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleQueryParam extends PageQuery implements Serializable {

    private Integer roleId;
    private String roleName;

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
}
