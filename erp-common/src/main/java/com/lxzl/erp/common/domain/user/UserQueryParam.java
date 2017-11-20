package com.lxzl.erp.common.domain.user;


import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.List;

public class UserQueryParam extends BasePageParam {

    private Integer userId;
    private String realName;
    private List<Integer> userIdList;
    private Integer roleId;
    private Integer departmentId;
    private Integer subCompanyId;
    private List<Integer> departmentIdList;
    private List<Integer> subCompanyIdList;
    private Integer departmentType;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<Integer> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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

    public List<Integer> getDepartmentIdList() {
        return departmentIdList;
    }

    public void setDepartmentIdList(List<Integer> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }

    public List<Integer> getSubCompanyIdList() {
        return subCompanyIdList;
    }

    public void setSubCompanyIdList(List<Integer> subCompanyIdList) {
        this.subCompanyIdList = subCompanyIdList;
    }

    public Integer getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(Integer departmentType) {
        this.departmentType = departmentType;
    }
}
