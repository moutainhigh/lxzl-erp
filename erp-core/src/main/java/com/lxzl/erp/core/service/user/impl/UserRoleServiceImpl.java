package com.lxzl.erp.core.service.user.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.erp.system.Menu;
import com.lxzl.erp.common.domain.erp.user.*;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.impl.support.UserRoleConverter;
import com.lxzl.erp.dataaccess.dao.mysql.system.SysMenuMysqlDAO;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMenuMysqlDAO;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMysqlDAO;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMysqlDAO;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserRoleMysqlDAO;
import com.lxzl.erp.dataaccess.domain.system.SysMenuDO;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import com.lxzl.erp.dataaccess.domain.user.RoleMenuDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.user.UserRoleDO;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/12/26.
 * Time: 9:04.
 */
@Service("RoleService")
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private SysMenuMysqlDAO sysMenuMysqlDAO;

    @Autowired
    private UserRoleMysqlDAO userRoleMysqlDAO;

    @Autowired
    private RoleMenuMysqlDAO roleMenuMysqlDAO;

    @Autowired
    private RoleMysqlDAO roleMysqlDAO;

    @Autowired
    private UserMysqlDAO userMysqlDAO;

    @Autowired(required = false)
    private HttpSession session;

    /**
     * 权限控制
     * 1、如果访问的URL在数据库中没有配置，则默认有权限
     */
    @Override
    public ServiceResult<String, Boolean> allowAccess(Integer userId, String path) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();

        UserDO userDO = userMysqlDAO.findByUserId(userId);
        if (userDO == null) {
            result.setResult(false);
            return result;
        }

        List<RoleDO> roleList = roleMysqlDAO.findByUserId(userId);
        if (roleList != null && roleList.size() > 0) {
            for (RoleDO roleDO : roleList) {
                if (CommonConstant.COMMON_CONSTANT_YES.equals(roleDO.getIsSuperAdmin())) {
                    result.setResult(true);
                    return result;
                }
            }
        } else {
            result.setResult(false);
            return result;
        }

        List<Integer> userRoleList = userRoleMysqlDAO.findRoleListByUserId(userId);
        if (userRoleList == null || userRoleList.size() == 0) {
            result.setResult(false);
            return result;
        }

        List<SysMenuDO> sysMenuDOList = sysMenuMysqlDAO.findByMenuURL(path);
        if (sysMenuDOList == null || sysMenuDOList.size() == 0) {
            result.setResult(true);
            return result;
        }

        List<Integer> roleMenuList = roleMenuMysqlDAO.findMenuListByRoleSet(userRoleList);
        if (roleMenuList == null || roleMenuList.size() == 0) {
            result.setResult(false);
            return result;
        }

        boolean isPass = false;
        lop:
        for (Integer roleMenuId : roleMenuList) {
            for (SysMenuDO sysMenuDO : sysMenuDOList) {
                if (roleMenuId.equals(sysMenuDO.getId())) {
                    isPass = true;
                    break lop;
                }
            }
        }

        result.setResult(isPass);
        return result;
    }

    @Override
    public boolean isSuperAdmin(Integer userId) {
        List<RoleDO> roleList = roleMysqlDAO.findByUserId(userId);
        if (roleList != null && roleList.size() > 0) {
            for (RoleDO roleDO : roleList) {
                if (CommonConstant.COMMON_CONSTANT_YES.equals(roleDO.getIsSuperAdmin())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 添加权限
     */
    @Override
    public ServiceResult<String, Integer> addRole(Role role) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        String verifyCode = verifyOpRole(role);
        if(!ErrorCode.SUCCESS.equals(verifyCode)){
            result.setErrorCode(verifyCode);
            return result;
        }
        RoleDO roleDO = UserRoleConverter.convertRole(role);
        roleDO.setDataStatus(CommonConstant.COMMON_CONSTANT_OP_CREATE);
        if (loginUser != null) {
            roleDO.setCreateUser(loginUser.getUserId().toString());
            roleDO.setUpdateUser(loginUser.getUserId().toString());
        }
        roleDO.setCreateTime(new Date());
        roleDO.setUpdateTime(new Date());
        roleMysqlDAO.save(roleDO);

        result.setResult(roleDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 修改权限
     */
    @Override
    public ServiceResult<String, Integer> updateRole(Role role) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        String verifyCode = verifyOpRole(role);
        if(!ErrorCode.SUCCESS.equals(verifyCode)){
            result.setErrorCode(verifyCode);
            return result;
        }
        RoleDO roleDO = UserRoleConverter.convertRole(role);
        if (loginUser != null) {
            roleDO.setUpdateUser(loginUser.getUserId().toString());
        }
        roleDO.setUpdateTime(new Date());
        roleMysqlDAO.update(roleDO);

        result.setResult(roleDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 删除权限，如果权限下面有人，不能删除
     */
    @Override
    public ServiceResult<String, Integer> deleteRole(Integer id) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);

        List<UserRoleDO> userRoleList = userRoleMysqlDAO.findListByRoleId(id);
        if (userRoleList != null && userRoleList.size() > 0) {
            result.setErrorCode(ErrorCode.ROLE_HAVE_USER);
            return result;
        }
        RoleDO managementRoleDO = roleMysqlDAO.findByMapId(id);
        if(managementRoleDO == null){
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        RoleDO roleDO = new RoleDO();
        roleDO.setId(id);
        if (loginUser != null) {
            roleDO.setUpdateUser(loginUser.getUserId().toString());
        }
        roleDO.setUpdateTime(new Date());
        roleDO.setDataStatus(CommonConstant.COMMON_CONSTANT_OP_DELETE);
        roleMysqlDAO.update(roleDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<Role>> getRoleList(RoleQueryParam param) {
        ServiceResult<String, Page<Role>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());
        Map<String, Object> params = new HashMap<>();

        if (StringUtil.isNotBlank(param.getRoleName())) {
            params.put("roleName", param.getRoleName());
        }
        params.put("start", pageQuery.getStart());
        params.put("pageSize", pageQuery.getPageSize());
        List<RoleDO> list = roleMysqlDAO.findList(params);
        Integer count = roleMysqlDAO.findListCount(params);

        Page<Role> page = new Page<>(UserRoleConverter.convertRoleDOList(list), count, param.getPageNo(), param.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> saveUserRole(UserRole userRole) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        // 超级管理员不用赋权限
        ServiceResult<String, Integer> result = new ServiceResult<>();
        List<Integer> dbRecordList = userRoleMysqlDAO.findRoleListByUserId(userRole.getUserId());
        if (userRole.getRoleList() != null && userRole.getRoleList().size() > 0) {
            List<Integer> thisRoleList = new ArrayList<>();
            for (Role role : userRole.getRoleList()) {
                thisRoleList.add(role.getRoleId());
                UserRoleDO dbRecord = userRoleMysqlDAO.findUserRole(userRole.getUserId(), role.getRoleId());
                if (dbRecord != null) {
                    continue;
                }

                UserRoleDO userRoleDO = UserRoleConverter.convertUserRole(userRole);
                userRoleDO.setRoleId(role.getRoleId());
                userRoleDO.setDataStatus(CommonConstant.COMMON_CONSTANT_OP_CREATE);
                if (loginUser != null) {
                    userRoleDO.setCreateUser(loginUser.getUserId().toString());
                    userRoleDO.setUpdateUser(loginUser.getUserId().toString());
                }
                userRoleDO.setCreateTime(new Date());
                userRoleDO.setUpdateTime(new Date());
                userRoleMysqlDAO.save(userRoleDO);
            }
            dbRecordList.removeAll(thisRoleList);

        }
            for (Integer roleId : dbRecordList) {
                UserRoleDO userRoleDO = userRoleMysqlDAO.findUserRole(userRole.getUserId(), roleId);
                userRoleDO.setDataStatus(CommonConstant.COMMON_CONSTANT_OP_DELETE);
                if (loginUser != null) {
                    userRoleDO.setUpdateUser(loginUser.getUserId().toString());
                }
                userRoleDO.setUpdateTime(new Date());
                userRoleMysqlDAO.update(userRoleDO);
            }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(userRole.getUserId());
        return result;
    }

    @Override
    public ServiceResult<String, UserRole> getUserRoleList(UserRoleQueryParam param) {
        ServiceResult<String, UserRole> result = new ServiceResult<>();
        List<Role> roleList = new ArrayList<>();

        List<Integer> roleIdList = userRoleMysqlDAO.findRoleListByUserId(param.getUserId());
        for (Integer roleId : roleIdList) {
            roleList.add(UserRoleConverter.convertRoleDO(roleMysqlDAO.findByMapId(roleId)));
        }

        UserRole userRole = new UserRole();
        userRole.setUserId(param.getUserId());
        userRole.setRoleList(roleList);

        result.setResult(userRole);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> saveRoleMenu(RoleMenu roleMenu) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);

        roleMenuMysqlDAO.deleteMenuByRoleId(roleMenu.getRoleId());
        if (roleMenu.getMenuList() != null && roleMenu.getMenuList().size() > 0) {
            for (Menu menu : roleMenu.getMenuList()) {
                RoleMenuDO dbRecord = roleMenuMysqlDAO.findRoleMenu(roleMenu.getRoleId(), menu.getMenuId());
                if (dbRecord != null) {
                    continue;
                }
                RoleMenuDO roleMenuDO = UserRoleConverter.convertRoleMenu(roleMenu);

                roleMenuDO.setMenuId(menu.getMenuId());
                roleMenuDO.setDataStatus(CommonConstant.COMMON_CONSTANT_OP_CREATE);
                if (loginUser != null) {
                    roleMenuDO.setCreateUser(loginUser.getUserId().toString());
                    roleMenuDO.setUpdateUser(loginUser.getUserId().toString());
                }
                roleMenuDO.setCreateTime(new Date());
                roleMenuDO.setUpdateTime(new Date());
                roleMenuMysqlDAO.save(roleMenuDO);
                saveParentMenu(roleMenu.getRoleId(), menu.getMenuId());
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private void saveParentMenu(Integer roleId, Integer menuId) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        SysMenuDO sysMenuDO = sysMenuMysqlDAO.findByMenuId(menuId);
        if (sysMenuDO != null && !CommonConstant.SUPER_MENU_ID.equals(sysMenuDO.getParentMenuId())) {
            RoleMenuDO dbRecord = roleMenuMysqlDAO.findRoleMenu(roleId, sysMenuDO.getParentMenuId());
            if (dbRecord == null) {
                RoleMenuDO roleMenuDO = new RoleMenuDO();
                roleMenuDO.setRoleId(roleId);
                roleMenuDO.setMenuId(sysMenuDO.getParentMenuId());

                roleMenuDO.setDataStatus(CommonConstant.COMMON_CONSTANT_OP_CREATE);
                if (loginUser != null) {
                    roleMenuDO.setCreateUser(loginUser.getUserId().toString());
                    roleMenuDO.setUpdateUser(loginUser.getUserId().toString());
                }
                roleMenuDO.setCreateTime(new Date());
                roleMenuDO.setUpdateTime(new Date());
                roleMenuMysqlDAO.save(roleMenuDO);
            }
            saveParentMenu(roleId, sysMenuDO.getParentMenuId());
        }
    }

    @Override
    public ServiceResult<String, RoleMenu> getRoleMenuList(RoleMenuQueryParam param) {
        ServiceResult<String, RoleMenu> result = new ServiceResult<>();
        List<Integer> roleSet = new ArrayList<>();
        roleSet.add(param.getRoleId());
        Map<String, Object> maps = new HashMap<>();
        maps.put("roleSet", roleSet);
        List<SysMenuDO> menuDOList = sysMenuMysqlDAO.findAllMenu(maps);

        List<SysMenuDO> nodeList = UserRoleConverter.convertTree(menuDOList);
        List<Menu> resultList = new ArrayList<>();
        for (SysMenuDO node1 : nodeList) {
            resultList.add(UserRoleConverter.convert(node1));
        }

        RoleDO roleDO = roleMysqlDAO.findByMapId(param.getRoleId());
        RoleMenu roleMenu = new RoleMenu();
        if (roleDO != null) {
            roleMenu.setRoleId(roleDO.getId());
            roleMenu.setRole(UserRoleConverter.convertRoleDO(roleDO));
        }
        roleMenu.setMenuList(resultList);

        result.setResult(roleMenu);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private String verifyOpRole(Role role) {
        if (role == null || StringUtil.isBlank(role.getRoleName())) {
            return ErrorCode.ROLE_NAME_NOT_NULL;
        }
        return ErrorCode.SUCCESS;
    }

}
