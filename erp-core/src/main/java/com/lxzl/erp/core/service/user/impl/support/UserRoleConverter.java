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

    public static RoleDO convertRole(Role role) {
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(role,roleDO);
        roleDO.setId(role.getRoleId());
        return roleDO;
    }

    public static List<Role> convertRoleDOList(List<RoleDO> roleDOList) {
        List<Role> list = new ArrayList<>();
        if (roleDOList != null && roleDOList.size() > 0) {
            for (RoleDO roleDO : roleDOList) {
                list.add(convertRoleDO(roleDO));
            }
        }
        return list;
    }

    public static Role convertRoleDO(RoleDO roleDo) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDo,role);
        role.setRoleId(roleDo.getId());
        return role;
    }

    public static UserRoleDO convertUserRole(UserRole userRole) {
        UserRoleDO userRoleDO = new UserRoleDO();
        if (userRole.getUserId() != null) {
            userRoleDO.setUserId(userRole.getUserId());
        }
        if (userRole.getRemark() != null) {
            userRoleDO.setRemark(userRole.getRemark());
        }
        return userRoleDO;
    }

    public static RoleMenuDO convertRoleMenu(RoleMenu roleMenu) {
        RoleMenuDO roleMenuDO = new RoleMenuDO();

        if (roleMenu.getRoleId() != null) {
            roleMenuDO.setRoleId(roleMenu.getRoleId());
        }

        return roleMenuDO;
    }

    public static Menu convert(SysMenuDO menuDO) {
        Menu menu = new Menu();
        if (menuDO.getId() != null) {
            menu.setMenuId(menuDO.getId());
        }
        if (menuDO.getMenuName() != null) {
            menu.setMenuName(menuDO.getMenuName());
        }
        if (menuDO.getParentMenuId() != null) {
            menu.setParentMenuId(menuDO.getParentMenuId());
        }
        if (menuDO.getMenuOrder() != null) {
            menu.setMenuOrder(menuDO.getMenuOrder());
        }
        if (menuDO.getIsFolder() != null) {
            menu.setIsFolder(menuDO.getIsFolder());
        }
        if (menuDO.getMenuUrl() != null) {
            menu.setMenuUrl(menuDO.getMenuUrl());
        }
        if (menuDO.getMenuIcon() != null) {
            menu.setMenuIcon(menuDO.getMenuIcon());
        }
        if (menuDO.getDataStatus() != null) {
            menu.setDataStatus(menuDO.getDataStatus());
        }
        if (menuDO.getRemark() != null) {
            menu.setRemark(menuDO.getRemark());
        }
        if (menuDO.getIsAvailable() != null) {
            menu.setIsAvailable(menuDO.getIsAvailable());
        }

        if (menuDO.getChildren() != null && menuDO.getChildren().size() > 0) {
            List<Menu> menuList = new ArrayList<>();
            for (SysMenuDO childrenMenu : menuDO.getChildren()) {
                menuList.add(convert(childrenMenu));
            }
            menu.setChildren(menuList);
        }
        if (menuDO.getButtonMaps() != null && menuDO.getButtonMaps().size() > 0) {
            Map<Integer, Menu> buttonMap = new HashMap<>();
            Set set = menuDO.getButtonMaps().keySet();
            for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                Integer key = (Integer) iter.next();
                buttonMap.put(key, convert(menuDO.getButtonMaps().get(key)));
            }
//            menu.setButtonMaps(buttonMap);
        }

        return menu;

    }

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

    public static SysMenuDO reverseConvert(Menu menuDO) {
        SysMenuDO menu = new SysMenuDO();
        if (menuDO.getMenuId() != null) {
            menu.setId(menuDO.getMenuId());
        }
        if (menuDO.getMenuName() != null) {
            menu.setMenuName(menuDO.getMenuName());
        }
        if (menuDO.getParentMenuId() != null) {
            menu.setParentMenuId(menuDO.getParentMenuId());
        }
        if (menuDO.getMenuOrder() != null) {
            menu.setMenuOrder(menuDO.getMenuOrder());
        }
        if (menuDO.getIsFolder() != null) {
            menu.setIsFolder(menuDO.getIsFolder());
        }
        if (menuDO.getMenuUrl() != null) {
            menu.setMenuUrl(menuDO.getMenuUrl());
        }
        if (menuDO.getMenuIcon() != null) {
            menu.setMenuIcon(menuDO.getMenuIcon());
        }
        if (menuDO.getDataStatus() != null) {
            menu.setDataStatus(menuDO.getDataStatus());
        }
        if (menuDO.getRemark() != null) {
            menu.setRemark(menuDO.getRemark());
        }

        return menu;

    }

    public static Menu convert(SysMenuDO menuDO, Menu parentMenu) {
        Menu menu = new Menu();
        if (menuDO.getId() != null) {
            menu.setMenuId(menuDO.getId());
        }
        if (menuDO.getMenuName() != null) {
            menu.setMenuName(menuDO.getMenuName());
        }
        if (menuDO.getParentMenuId() != null) {
            menu.setParentMenuId(menuDO.getParentMenuId());
        }
        if (menuDO.getMenuOrder() != null) {
            menu.setMenuOrder(menuDO.getMenuOrder());
        }
        if (menuDO.getIsFolder() != null) {
            menu.setIsFolder(menuDO.getIsFolder());
        }
        if (menuDO.getMenuUrl() != null) {
            menu.setMenuUrl(menuDO.getMenuUrl());
        }
        if (menuDO.getMenuIcon() != null) {
            menu.setMenuIcon(menuDO.getMenuIcon());
        }
        if (menuDO.getDataStatus() != null) {
            menu.setDataStatus(menuDO.getDataStatus());
        }
        if (menuDO.getRemark() != null) {
            menu.setRemark(menuDO.getRemark());
        }
        if (menuDO.getIsAvailable() != null) {
            menu.setIsAvailable(menuDO.getIsAvailable());
        }

        if (menuDO.getChildren() != null && menuDO.getChildren().size() > 0) {
            List<Menu> menuList = new ArrayList<>();
            for (SysMenuDO childrenMenu : menuDO.getChildren()) {
                menuList.add(convert(childrenMenu, parentMenu));
            }
            menu.setChildren(menuList);
        }
        if (menuDO.getButtonMaps() != null && menuDO.getButtonMaps().size() > 0) {
            Map<Integer, Menu> buttonMap = new HashMap<>();
            Set set = menuDO.getButtonMaps().keySet();
            for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                Integer key = (Integer) iter.next();
                buttonMap.put(key, convert(menuDO.getButtonMaps().get(key), parentMenu));
            }
//            menu.setButtonMaps(buttonMap);
        }

        return menu;

    }


    public static RoleDepartmentDataDO convertRoleDepartmentData(RoleDepartmentData roleDepartmentData) {
        RoleDepartmentDataDO roleDepartmentDataDO = new RoleDepartmentDataDO();
        BeanUtils.copyProperties(roleDepartmentData,roleDepartmentDataDO);
        roleDepartmentDataDO.setId(roleDepartmentData.getRoleDepartmentDataId());
        return roleDepartmentDataDO;
    }
    public static RoleDepartmentData convertRoleDepartmentDataDO(RoleDepartmentDataDO roleDepartmentDataDO) {
        RoleDepartmentData roleDepartmentData = new RoleDepartmentData();
        BeanUtils.copyProperties(roleDepartmentDataDO,roleDepartmentData);
        roleDepartmentData.setRoleDepartmentDataId(roleDepartmentDataDO.getId());
        return roleDepartmentData;
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
                passiveUser.setUserName(roleUserDataDO.getPassiveUserName());
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
                passiveUser.setUserName(roleUserFinalDO.getPassiveUserName());
                userList.add(passiveUser);
            }
            roleUserFinal.setPassiveUserList(userList);
        }
        return roleUserFinal;
    }
}
