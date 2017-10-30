package com.lxzl.erp.core.service.user;


import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.erp.user.*;
import com.lxzl.se.core.service.BaseService;

/**
 * Created by IntelliJ IDEA.
 * ManagementUser: gaochao
 * Date: 2016/12/26.
 * Time: 8:47.
 */
public interface UserRoleService extends BaseService {
    boolean isSuperAdmin(Integer userId);
    ServiceResult<String, Boolean> allowAccess(Integer userId, String path);
    ServiceResult<String, Integer> addRole(Role role);
    ServiceResult<String, Integer> updateRole(Role role);
    ServiceResult<String, Integer> deleteRole(Integer id);
    ServiceResult<String, Page<Role>> getRoleList(RoleQueryParam param);
    ServiceResult<String, Integer> saveUserRole(UserRole userRole);
    ServiceResult<String, UserRole> getUserRoleList(UserRoleQueryParam param);
    ServiceResult<String, Integer> saveRoleMenu(RoleMenu roleMenu);
    ServiceResult<String, RoleMenu> getRoleMenuList(RoleMenuQueryParam param);
}
