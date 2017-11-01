package com.lxzl.erp.common.domain.user;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2017/1/4.
 * Time: 14:36.
 */
public class UserRoleQueryParam extends BasePageParam {

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
