package com.lxzl.erp.common.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author lk
 * @Description: 未登录状态下修改密码参数类
 * @date 2018/1/21 14:05
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePasswordParam implements Serializable {

    @NotEmpty(message = ErrorCode.USER_NAME_NOT_NULL)
    private String userName;
    @NotEmpty(message = ErrorCode.USER_OLD_PASSWORD_NOT_NULL)
    private String oldPassword;
    @NotEmpty(message = ErrorCode.USER_NEW_PASSWORD_NOT_NULL)
    private String newPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
