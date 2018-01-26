package com.lxzl.erp.common.domain.user;


import com.lxzl.erp.common.domain.base.BasePageParam;

public class DepartmentQueryParam extends BasePageParam {

    private Integer departmentId;
    private String departmentName;
    private Integer subCompanyId;
    private Integer parentDepartmentId;
    private String fullDepartmentName;

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

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public Integer getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void setParentDepartmentId(Integer parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId;
    }

    public String getFullDepartmentName() {
        return fullDepartmentName;
    }

    public void setFullDepartmentName(String fullDepartmentName) {
        this.fullDepartmentName = fullDepartmentName;
    }
}
