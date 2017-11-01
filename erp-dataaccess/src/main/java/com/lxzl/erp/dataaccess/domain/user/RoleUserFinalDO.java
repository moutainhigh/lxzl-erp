package com.lxzl.erp.dataaccess.domain.user;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

public class RoleUserFinalDO extends BaseDO {

    private Integer id;

    private Integer activeUserId;

    private Integer passiveUserId;

    private Integer dataStatus;

    private String remark;

    @Transient
    private String passiveUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActiveUserId() {
        return activeUserId;
    }

    public void setActiveUserId(Integer activeUserId) {
        this.activeUserId = activeUserId;
    }

    public Integer getPassiveUserId() {
        return passiveUserId;
    }

    public void setPassiveUserId(Integer passiveUserId) {
        this.passiveUserId = passiveUserId;
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

    public String getPassiveUserName() {
        return passiveUserName;
    }

    public void setPassiveUserName(String passiveUserName) {
        this.passiveUserName = passiveUserName;
    }
}