package com.lxzl.erp.common.domain.base;

import java.io.Serializable;

public class BasePO implements Serializable {

    private String createUserRealName ;
    private String updateUserRealName ;

    public String getCreateUserRealName() {
        return createUserRealName;
    }

    public void setCreateUserRealName(String createUserRealName) {
        this.createUserRealName = createUserRealName;
    }

    public String getUpdateUserRealName() {
        return updateUserRealName;
    }

    public void setUpdateUserRealName(String updateUserRealName) {
        this.updateUserRealName = updateUserRealName;
    }
}
