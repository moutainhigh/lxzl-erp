package com.lxzl.erp.common.domain.erp.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2017/1/4.
 * Time: 14:37.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleMenuQueryParam extends PageQuery implements Serializable {

    private Integer roleId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
