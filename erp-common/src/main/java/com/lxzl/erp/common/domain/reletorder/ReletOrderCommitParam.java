package com.lxzl.erp.common.domain.reletorder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.BaseCommitParam;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReletOrderCommitParam extends BaseCommitParam {

    private String reletOrderNo;
    private Integer verifyUser;
    private String commitRemark;

    public String getReletOrderNo() {
        return reletOrderNo;
    }

    public void setReletOrderNo(String reletOrderNo) {
        this.reletOrderNo = reletOrderNo;
    }

    public Integer getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(Integer verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getCommitRemark() {
        return commitRemark;
    }

    public void setCommitRemark(String commitRemark) {
        this.commitRemark = commitRemark;
    }
}
