package com.lxzl.erp.common.domain;

public class BaseCommitParam {

    private Integer verifyUserId;
    private String remark;
    private String verifyMatters;

    public Integer getVerifyUserId() {
        return verifyUserId;
    }

    public void setVerifyUserId(Integer verifyUserId) {
        this.verifyUserId = verifyUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVerifyMatters() { return verifyMatters; }

    public void setVerifyMatters(String verifyMatters) { this.verifyMatters = verifyMatters; }
}
