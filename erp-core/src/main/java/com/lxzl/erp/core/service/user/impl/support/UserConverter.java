package com.lxzl.erp.core.service.user.impl.support;


import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.domain.user.pojo.UserDepartment;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.user.UserDepartmentDO;
import com.lxzl.erp.dataaccess.domain.user.UserRoleDO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {
    public static User convert(UserDO userDO) {
        User user = new User();
        user.setUserId(userDO.getId());
        BeanUtils.copyProperties(userDO, user);
        user.setRoleList(userDO.getRoleList());
        user.setUserDepartmentList(convertUserDepartmentDOList(userDO.getUserDepartmentList()));
        user.setUserRoleList(convertUserRoleDOListToRoleList(userDO.getUserRoleList()));

        return user;
    }

    public static List<Role> convertUserRoleDOListToRoleList(List<UserRoleDO> userRoleDOList){
        List<Role> roleList = new ArrayList<>();
        if(userRoleDOList != null && !userRoleDOList.isEmpty()){
            for(UserRoleDO userRoleDO : userRoleDOList){
                Role role = new Role();
                BeanUtils.copyProperties(userRoleDO, role);
                roleList.add(role);
            }
        }

        return roleList;
    }

    public static List<UserDepartment> convertUserDepartmentDOList(List<UserDepartmentDO> userDepartmentDOList) {
        List<UserDepartment> userDepartmentList = new ArrayList<>();
        if (userDepartmentDOList != null && !userDepartmentDOList.isEmpty()) {
            for (UserDepartmentDO userDepartmentDO : userDepartmentDOList) {
                userDepartmentList.add(convertUserDepartmentDO(userDepartmentDO));
            }
        }

        return userDepartmentList;
    }

    public static UserDepartment convertUserDepartmentDO(UserDepartmentDO userDepartmentDO) {
        UserDepartment userDepartment = new UserDepartment();
        BeanUtils.copyProperties(userDepartmentDO, userDepartment);
        return userDepartment;
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
