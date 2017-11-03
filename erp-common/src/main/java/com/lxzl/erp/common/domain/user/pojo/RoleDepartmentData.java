package com.lxzl.erp.common.domain.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.validGroup.AddGroup;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDepartmentData implements Serializable {


    private Integer roleDepartmentDataId;

    @NotNull(message = ErrorCode.ROLE_ID_NOT_NULL , groups = {AddGroup.class})
    private Integer roleId;
    private List<Department> departmentList;

    private Integer dataStatus;

    private String remark;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;


    public Integer getRoleDepartmentDataId() {
        return roleDepartmentDataId;
    }

    public void setRoleDepartmentDataId(Integer roleDepartmentDataId) {
        this.roleDepartmentDataId = roleDepartmentDataId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}