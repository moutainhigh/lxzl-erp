package com.lxzl.erp.common.domain.base;

import java.io.Serializable;
import java.util.List;

public class PermissionParam implements Serializable {

    //控制可查看用户的数据权限
    private List<Integer> permissionUserIdList;
    //控制可查看仓库的数据权限
    private List<Integer> permissionWarehouseIdList;
    //控制可查看分公司的数据权限
    private Integer permissionSubCompanyId;
    //控制可查看发货分公司的数据权限
    private Integer permissionSubCompanyIdForDelivery;
    //控制可查看公司的数据权限
    private List<Integer> permissionSubCompanyIdList;

    public List<Integer> getPermissionUserIdList() {
        return permissionUserIdList;
    }

    public void setPermissionUserIdList(List<Integer> permissionUserIdList) { this.permissionUserIdList = permissionUserIdList; }

    public List<Integer> getPermissionWarehouseIdList() {
        return permissionWarehouseIdList;
    }

    public void setPermissionWarehouseIdList(List<Integer> permissionWarehouseIdList) { this.permissionWarehouseIdList = permissionWarehouseIdList; }

    public Integer getPermissionSubCompanyId() {
        return permissionSubCompanyId;
    }

    public void setPermissionSubCompanyId(Integer permissionSubCompanyId) { this.permissionSubCompanyId = permissionSubCompanyId; }

    public List<Integer> getPermissionSubCompanyIdList() { return permissionSubCompanyIdList; }

    public void setPermissionSubCompanyIdList(List<Integer> permissionSubCompanyIdList) { this.permissionSubCompanyIdList = permissionSubCompanyIdList; }

    public Integer getPermissionSubCompanyIdForDelivery() {
        return permissionSubCompanyIdForDelivery;
    }

    public void setPermissionSubCompanyIdForDelivery(Integer permissionSubCompanyIdForDelivery) {
        this.permissionSubCompanyIdForDelivery = permissionSubCompanyIdForDelivery;
    }
}
