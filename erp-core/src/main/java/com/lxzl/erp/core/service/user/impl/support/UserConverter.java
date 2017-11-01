package com.lxzl.erp.core.service.user.impl.support;


import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.dataaccess.domain.user.UserDO;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {
    public static User convert(UserDO userDO) {
        User user = new User();
        user.setUserId(userDO.getId());
        user.setUserName(userDO.getUserName());
        user.setRealName(userDO.getRealName());
        user.setEmail(userDO.getEmail());
        user.setPhone(userDO.getPhone());
        user.setIsActivated(userDO.getIsActivated());
        user.setIsDisabled(userDO.getIsDisabled());
        user.setRegisterTime(userDO.getRegisterTime());
        user.setLastLoginTime(userDO.getLastLoginTime());
        user.setLastLoginIp(userDO.getLastLoginIp());
        user.setRemark(userDO.getRemark());
        user.setRoleList(userDO.getRoleList());
        return user;
    }

    public static UserDO convert(User user) {
        UserDO userDO = new UserDO();
        if (user.getUserId() != null) {
            userDO.setId(user.getUserId());
        }
        if (user.getUserName() != null) {
            userDO.setUserName(user.getUserName());
        }
        if (user.getRealName() != null) {
            userDO.setRealName(user.getRealName());
        }
        if (user.getEmail() != null) {
            userDO.setEmail(user.getEmail());
        }
        if (user.getPhone() != null) {
            userDO.setPhone(user.getPhone());
        }
        if (user.getIsActivated() != null) {
            userDO.setIsActivated(user.getIsActivated());
        }
        if (user.getIsDisabled() != null) {
            userDO.setIsDisabled(user.getIsDisabled());
        }
        if (user.getRegisterTime() != null) {
            userDO.setRegisterTime(user.getRegisterTime());
        }
        if (user.getLastLoginTime() != null) {
            userDO.setLastLoginTime(user.getLastLoginTime());
        }
        if (user.getLastLoginIp() != null) {
            userDO.setLastLoginIp(user.getLastLoginIp());
        }
        if (user.getRemark() != null) {
            userDO.setRemark(user.getRemark());
        }
        if (user.getRoleList() != null) {
            userDO.setRoleList(user.getRoleList());
        }
        return userDO;
    }

    public static List<User> convertUserList(List<UserDO> userDOList) {
        List<User> userList = new ArrayList<>();
        if (userDOList != null && userDOList.size() > 0) {
            for (UserDO userDO : userDOList) {
                userList.add(convert(userDO));
            }
        }
        return userList;
    }
}
