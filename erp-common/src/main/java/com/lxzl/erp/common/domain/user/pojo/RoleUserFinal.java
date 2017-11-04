package com.lxzl.erp.common.domain.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleUserFinal implements Serializable {

    private Integer roleUserFinalId;

    private Integer activeUserId;

    private List<User> passiveUserList;

    private Integer dataStatus;

    private String remark;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    public Integer getRoleUserFinalId() {
        return roleUserFinalId;
    }

    public void setRoleUserFinalId(Integer roleUserFinalId) {
        this.roleUserFinalId = roleUserFinalId;
    }

    public Integer getActiveUserId() {
        return activeUserId;
    }

    public void setActiveUserId(Integer activeUserId) {
        this.activeUserId = activeUserId;
    }

    public List<User> getPassiveUserList() {
        return passiveUserList;
    }

    public void setPassiveUserList(List<User> passiveUserList) {
        this.passiveUserList = passiveUserList;
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