package com.lxzl.erp.core.service.user.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.system.pojo.Menu;
import com.lxzl.erp.common.domain.user.pojo.*;
import com.lxzl.erp.dataaccess.domain.system.SysMenuDO;
import com.lxzl.erp.dataaccess.domain.user.*;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2017/1/4.
 * Time: 10:20.
 */
public class UserRoleConverter {

    public static List<SysMenuDO> convertTree(List<SysMenuDO> menuDOList) {
        List<SysMenuDO> nodeList = new ArrayList<>();
        if (menuDOList != null) {
            for (SysMenuDO node1 : menuDOList) {
                if (node1.getParentMenuId().equals(CommonConstant.SUPER_MENU_ID)) {
                    nodeList.add(node1);
                }
                for (SysMenuDO t : menuDOList) {
                    if (t.getParentMenuId().equals(node1.getId())) {
                        if (node1.getChildren() == null) {
                            List<SysMenuDO> myChildren = new ArrayList<SysMenuDO>();
                            myChildren.add(t);
                            node1.setChildren(myChildren);
                        } else {
                            node1.getChildren().add(t);
                        }

                        if (node1.getButtonMaps() == null) {
                            if (t.getIsFolder() == 2) {
                                Map<Integer, SysMenuDO> map = new HashMap<>();
                                map.put(t.getId(), t);
                                node1.setButtonMaps(map);
                            }
                        } else {
                            node1.getButtonMaps().put(t.getId(), t);
                        }
                    }
                }
            }
        }

        return nodeList;
    }

    public static RoleDepartmentData convertRoleDepartmentDataDOList(List<RoleDepartmentDataDO> roleDepartmentDataDOList) {
        RoleDepartmentData roleDepartmentData = null;
        if (roleDepartmentDataDOList != null && roleDepartmentDataDOList.size() > 0) {
            roleDepartmentData = new RoleDepartmentData();
            roleDepartmentData.setRoleId(roleDepartmentDataDOList.get(0).getRoleId());
            List<Department> departmentList = new ArrayList<>();
            for (RoleDepartmentDataDO roleDepartmentDataDO : roleDepartmentDataDOList) {
                Department department = new Department();
                department.setDepartmentId(roleDepartmentDataDO.getDepartmentId());
                department.setDepartmentName(roleDepartmentDataDO.getDepartmentName());
                departmentList.add(department);
            }
            roleDepartmentData.setDepartmentList(departmentList);
        }
        return roleDepartmentData;
    }

    public static RoleUserData convertRoleUserDataDOList(List<RoleUserDataDO> roleUserDataDOList) {
        RoleUserData roleUserData = null;
        if (roleUserDataDOList != null && roleUserDataDOList.size() > 0) {
            roleUserData = new RoleUserData();
            roleUserData.setActiveUserId(roleUserDataDOList.get(0).getActiveUserId());
            List<User> userList = new ArrayList<>();
            for (RoleUserDataDO roleUserDataDO : roleUserDataDOList) {
                User passiveUser = new User();
                passiveUser.setUserId(roleUserDataDO.getPassiveUserId());
                passiveUser.setRealName(roleUserDataDO.getPassiveUserName());
                userList.add(passiveUser);
            }
            roleUserData.setPassiveUserList(userList);
        }
        return roleUserData;
    }

    public static RoleUserFinal convertRoleUserFinalDOList(List<RoleUserFinalDO> roleUserFinalDOList) {
        RoleUserFinal roleUserFinal = null;
        if (roleUserFinalDOList != null && roleUserFinalDOList.size() > 0) {
            roleUserFinal = new RoleUserFinal();
            roleUserFinal.setActiveUserId(roleUserFinalDOList.get(0).getActiveUserId());
            List<User> userList = new ArrayList<>();
            for (RoleUserFinalDO roleUserFinalDO : roleUserFinalDOList) {
                User passiveUser = new User();
                passiveUser.setUserId(roleUserFinalDO.getPassiveUserId());
                passiveUser.setRealName(roleUserFinalDO.getPassiveUserName());
                userList.add(passiveUser);
            }
            roleUserFinal.setPassiveUserList(userList);
        }
        return roleUserFinal;
    }
}
