package com.lxzl.erp.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseCommitParam {

    private Integer verifyUserId;
    private String remark;
    private String verifyMatters;
    private List<Integer> imgIdList;

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

    public List<Integer> getImgIdList() {
        return imgIdList;
    }

    public void setImgIdList(List<Integer> imgIdList) {
        this.imgIdList = imgIdList;
    }
}
