package com.lxzl.erp.common.domain.user;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2017/1/4.
 * Time: 14:37.
 */
public class RoleMenuQueryParam extends BasePageParam {

    private Integer roleId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
