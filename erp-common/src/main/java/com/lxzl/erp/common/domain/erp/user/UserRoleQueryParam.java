package com.lxzl.erp.common.domain.erp.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2017/1/4.
 * Time: 14:36.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleQueryParam extends PageQuery implements Serializable {

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
