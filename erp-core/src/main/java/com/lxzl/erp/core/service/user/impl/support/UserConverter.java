package com.lxzl.erp.core.service.user.impl.support;


import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.user.UserRoleDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {
    public static User convert(UserDO userDO) {
        User user = new User();
        user.setUserId(userDO.getId());
        BeanUtils.copyProperties(userDO, user);
        user.setRoleList(convertUserRoleDOList(userDO.getRoleDOList()));
        return user;
    }

    public static List<Role> convertUserRoleDOList(List<RoleDO> roleDOList){
        List<Role> roleList = new ArrayList<>();
        if(roleDOList != null && !roleDOList.isEmpty()){
            for(RoleDO roleDO : roleDOList){
                Role role = new Role ();
                role.setRoleId(roleDO.getId());
                BeanUtils.copyProperties(roleDO, role);
                roleList.add(role);
            }
        }
        return roleList;
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
