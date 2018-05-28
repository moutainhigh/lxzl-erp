package com.lxzl.erp.common.domain.reletorder;

import com.lxzl.erp.common.domain.user.pojo.User;

import java.io.Serializable;
import java.util.List;

public class ReletOrderCreateResult implements Serializable{

    private String reletOrderNo;  //续租单编号

    private Integer isNeedVerify;  //是否需要审核

    private List<User> verifyUsers;  //审核人列表

    public String getReletOrderNo() {
        return reletOrderNo;
    }

    public void setReletOrderNo(String reletOrderNo) {
        this.reletOrderNo = reletOrderNo;
    }

    public Integer getIsNeedVerify() {
        return isNeedVerify;
    }

    public void setIsNeedVerify(Integer isNeedVerify) {
        this.isNeedVerify = isNeedVerify;
    }

    public List<User> getVerifyUsers() {
        return verifyUsers;
    }

    public void setVerifyUsers(List<User> verifyUsers) {
        this.verifyUsers = verifyUsers;
    }
}
