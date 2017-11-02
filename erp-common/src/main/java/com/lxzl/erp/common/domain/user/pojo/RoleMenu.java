package com.lxzl.erp.common.domain.user.pojo;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.system.pojo.Menu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/12/26.
 * Time: 9:21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleMenu implements Serializable {

    private Integer roleId;

    private Role role;

    private List<Menu> menuList;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
