package com.lxzl.erp.dataaccess.domain.company;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

public class SubCompanyDO extends BaseDO{

    private Integer id;

    private String subCompanyName;

    private Integer dataStatus;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName == null ? null : subCompanyName.trim();
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