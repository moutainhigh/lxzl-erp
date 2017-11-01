package com.lxzl.erp.common.domain.user;


import com.lxzl.erp.common.domain.base.BasePageParam;

public class UserQueryParam extends BasePageParam {

    private Integer userId;
    private String realName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

}
