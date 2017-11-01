package com.lxzl.erp.dataaccess.domain.company;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

public class UserSubCompanyDO extends BaseDO{
    private Long id;

    private Long userId;

    private Long subCompanyId;

    private Integer dataStatus;

    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Long subCompanyId) {
        this.subCompanyId = subCompanyId;
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
        this.remark = remark == null ? null : remark.trim();
    }

}