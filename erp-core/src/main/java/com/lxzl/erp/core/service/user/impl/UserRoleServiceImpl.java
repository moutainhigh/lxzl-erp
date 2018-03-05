package com.lxzl.erp.core.service.user.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.system.pojo.Menu;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.erp.common.domain.user.RoleMenuQueryParam;
import com.lxzl.erp.common.domain.user.RoleQueryParam;
import com.lxzl.erp.common.domain.user.UserRoleQueryParam;
import com.lxzl.erp.common.domain.user.pojo.*;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.company.impl.support.DepartmentConverter;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.impl.support.UserRoleConverter;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.company.SubCompanyMapper;
import com.lxzl.erp.dataaccess.dao.mysql.system.SysMenuMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.*;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;
import com.lxzl.erp.dataaccess.domain.company.SubCompanyDO;
import com.lxzl.erp.dataaccess.domain.system.SysMenuDO;
import com.lxzl.erp.dataaccess.domain.user.*;
import com.lxzl.se.common.util.StringUtil;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
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
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleDepartmentDataMapper roleDepartmentDataMapper;
    @Autowired
    private RoleUserDataMapper roleUserDataMapper;
    @Autowired
    private RoleUserFinalMapper roleUserFinalMapper;

    @Autowired
    private SubCompanyMapper subCompanyMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private UserSupport userSupport;

    /**
     * 权限控制
     * 1、如果访问的URL在数据库中没有配置，则默认有权限
     */
    @Override
    public ServiceResult<String, Boolean> allowAccess(Integer userId, String path) {
        ServiceResult<String, Boolean> result = new ServiceResult<>();

        UserDO userDO = userMapper.findByUserId(userId);
        if (userDO == null) {
            result.setResult(false);
            return result;
        }

        List<RoleDO> roleList = roleMapper.findByUserId(userId);
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

        List<Integer> userRoleList = userRoleMapper.findRoleIdListByUserId(userId);
        if (userRoleList == null || userRoleList.size() == 0) {
            result.setResult(false);
            return result;
        }

        List<SysMenuDO> sysMenuDOList = sysMenuMapper.findByMenuURL(path);
        if (sysMenuDOList == null || sysMenuDOList.size() == 0) {
            result.setResult(true);
            return result;
        }

        List<Integer> roleMenuList = roleMenuMapper.findMenuListByRoleSet(userRoleList);
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
        List<RoleDO> roleList = roleMapper.findByUserId(userId);
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
        String verifyCode = verifyOpRole(role);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }
        String currentUserId = userSupport.getCurrentUserId().toString();
        RoleDO roleDO = ConverterUtil.convert(role, RoleDO.class);
        roleDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        roleDO.setCreateUser(currentUserId);
        roleDO.setUpdateUser(currentUserId);
        roleDO.setCreateTime(new Date());
        roleDO.setUpdateTime(new Date());
        roleMapper.save(roleDO);

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

        String verifyCode = verifyOpRole(role);
        if (!ErrorCode.SUCCESS.equals(verifyCode)) {
            result.setErrorCode(verifyCode);
            return result;
        }
        RoleDO roleDO = ConverterUtil.convert(role, RoleDO.class);
        roleDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        roleDO.setUpdateTime(new Date());
        roleMapper.update(roleDO);

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

        List<UserRoleDO> userRoleList = userRoleMapper.findListByRoleId(id);
        if (userRoleList != null && userRoleList.size() > 0) {
            result.setErrorCode(ErrorCode.ROLE_HAVE_USER);
            return result;
        }
        RoleDO managementRoleDO = roleMapper.findByMapId(id);
        if (managementRoleDO == null) {
            result.setErrorCode(ErrorCode.RECORD_NOT_EXISTS);
            return result;
        }

        RoleDO roleDO = new RoleDO();
        roleDO.setId(id);
        roleDO.setUpdateUser(userSupport.getCurrentUserId().toString());
        roleDO.setUpdateTime(new Date());
        roleDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        roleMapper.update(roleDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Page<Role>> getRoleList(RoleQueryParam param) {
        ServiceResult<String, Page<Role>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(param.getPageNo(), param.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("start", pageQuery.getStart());
        params.put("pageSize", pageQuery.getPageSize());
        params.put("roleQueryParam", param);
        List<RoleDO> list = roleMapper.findList(params);
        Integer count = roleMapper.findListCount(params);

        Page<Role> page = new Page<>(ConverterUtil.convertList(list, Role.class), count, param.getPageNo(), param.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> saveUserRole(UserRole userRole) {
        // 超级管理员不用赋权限
        ServiceResult<String, Integer> result = new ServiceResult<>();
        List<RoleDO> roleList = userRoleMapper.findRoleListByUserId(userRole.getUserId());
        Map<Integer, RoleDO> dbRoleMap = new HashMap<>();
        dbRoleMap = ListUtil.listToMap(roleList, "id");
        String currentUserId = userSupport.getCurrentUserId().toString();
        Date now = new Date();
        if (userRole.getRoleList() != null && userRole.getRoleList().size() > 0) {
            for (Role role : userRole.getRoleList()) {
                dbRoleMap.remove(role.getRoleId());
                UserRoleDO dbRecord = userRoleMapper.findUserRole(userRole.getUserId(), role.getRoleId());
                if (dbRecord != null) {
                    continue;
                }
                UserRoleDO userRoleDO = ConverterUtil.convert(userRole, UserRoleDO.class);
                userRoleDO.setRoleId(role.getRoleId());
                userRoleDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                userRoleDO.setCreateUser(currentUserId);
                userRoleDO.setUpdateUser(currentUserId);
                userRoleDO.setCreateTime(now);
                userRoleDO.setUpdateTime(now);
                userRoleMapper.save(userRoleDO);
            }
        }

        for (Map.Entry<Integer, RoleDO> entry : dbRoleMap.entrySet()) {
            UserRoleDO userRoleDO = userRoleMapper.findUserRole(userRole.getUserId(), entry.getKey());
            userRoleDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            userRoleDO.setUpdateUser(currentUserId);
            userRoleDO.setUpdateTime(new Date());
            userRoleMapper.update(userRoleDO);
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(userRole.getUserId());
        return result;
    }

    @Override
    public ServiceResult<String, UserRole> getUserRoleList(UserRoleQueryParam param) {
        ServiceResult<String, UserRole> result = new ServiceResult<>();
        List<RoleDO> roleDOList = userRoleMapper.findRoleListByUserId(param.getUserId());
        UserRole userRole = new UserRole();
        userRole.setUserId(param.getUserId());
        userRole.setRoleList(ConverterUtil.convertList(roleDOList, Role.class));
        result.setResult(userRole);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> saveRoleMenu(RoleMenu roleMenu) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String currentUserId = userSupport.getCurrentUserId().toString();
        Date now = new Date();
        roleMenuMapper.deleteMenuByRoleId(roleMenu.getRoleId());
        if (roleMenu.getMenuList() != null && roleMenu.getMenuList().size() > 0) {
            for (Menu menu : roleMenu.getMenuList()) {
                RoleMenuDO dbRecord = roleMenuMapper.findRoleMenu(roleMenu.getRoleId(), menu.getMenuId());
                if (dbRecord != null) {
                    continue;
                }
                RoleMenuDO roleMenuDO = ConverterUtil.convert(roleMenu, RoleMenuDO.class);

                roleMenuDO.setMenuId(menu.getMenuId());
                roleMenuDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                roleMenuDO.setCreateUser(currentUserId);
                roleMenuDO.setUpdateUser(currentUserId);
                roleMenuDO.setCreateTime(now);
                roleMenuDO.setUpdateTime(now);
                roleMenuMapper.save(roleMenuDO);
                saveParentMenu(roleMenu.getRoleId(), menu.getMenuId());
            }
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private void saveParentMenu(Integer roleId, Integer menuId) {
        String currentUserId = userSupport.getCurrentUserId().toString();
        Date now = new Date();
        SysMenuDO sysMenuDO = sysMenuMapper.findByMenuId(menuId);
        if (sysMenuDO != null && !CommonConstant.SUPER_MENU_ID.equals(sysMenuDO.getParentMenuId())) {
            RoleMenuDO dbRecord = roleMenuMapper.findRoleMenu(roleId, sysMenuDO.getParentMenuId());
            if (dbRecord == null) {
                RoleMenuDO roleMenuDO = new RoleMenuDO();
                roleMenuDO.setRoleId(roleId);
                roleMenuDO.setMenuId(sysMenuDO.getParentMenuId());

                roleMenuDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                roleMenuDO.setCreateUser(currentUserId);
                roleMenuDO.setUpdateUser(currentUserId);
                roleMenuDO.setCreateTime(now);
                roleMenuDO.setUpdateTime(now);
                roleMenuMapper.save(roleMenuDO);
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
        List<SysMenuDO> menuDOList = sysMenuMapper.findRoleMenu(maps);

        List<SysMenuDO> nodeList = UserRoleConverter.convertTree(menuDOList);
        List<Menu> resultList = new ArrayList<>();
        for (SysMenuDO node1 : nodeList) {
            resultList.add(ConverterUtil.convert(node1, Menu.class));
        }

        RoleDO roleDO = roleMapper.findByMapId(param.getRoleId());
        RoleMenu roleMenu = new RoleMenu();
        if (roleDO != null) {
            roleMenu.setRoleId(roleDO.getId());
            roleMenu.setRole(ConverterUtil.convert(roleDO, Role.class));
        }
        roleMenu.setMenuList(resultList);

        result.setResult(roleMenu);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateRoleDepartmentData(RoleDepartmentData roleDepartmentData) {
        ServiceResult<String, Integer> result = new ServiceResult<>();

        RoleDO roleDO = roleMapper.findById(roleDepartmentData.getRoleId());
        if(roleDO == null ){
            result.setErrorCode(ErrorCode.ROLE_NOT_NULL);
            return result;
        }
        String currentUserId = userSupport.getCurrentUserId().toString();
        Date now = new Date();
        List<Department> departmentList = roleDepartmentData.getDepartmentList();
        Set<Integer> departmentIdSet = ListUtil.listToSet(departmentList,"departmentId");
        List<RoleDepartmentDataDO> oldList = roleDepartmentDataMapper.getRoleDepartmentDataListByRoleId(roleDepartmentData.getRoleId());
        Map<Integer,RoleDepartmentDataDO> oldMap = ListUtil.listToMap(oldList,"departmentId");

        if(CollectionUtil.isNotEmpty(departmentIdSet)){
            for(Integer departmentId : departmentIdSet){
                DepartmentDO departmentDO = departmentMapper.findById(departmentId);
                if(departmentDO==null){
                    result.setErrorCode(ErrorCode.DEPARTMENT_NOT_EXISTS);
                    return result;
                }
                //当前部门不在旧部门列表中，新增部门，在旧列表中，不处理
                if (oldMap.containsKey(departmentId)) {
                    oldMap.remove(departmentId);
                    continue;
                } else {
                    RoleDepartmentDataDO roleDepartmentDataDO = new RoleDepartmentDataDO();
                    roleDepartmentDataDO.setRoleId(roleDepartmentData.getRoleId());
                    roleDepartmentDataDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    roleDepartmentDataDO.setCreateUser(currentUserId);
                    roleDepartmentDataDO.setUpdateUser(currentUserId);
                    roleDepartmentDataDO.setCreateTime(now);
                    roleDepartmentDataDO.setUpdateTime(now);
                    roleDepartmentDataDO.setDepartmentId(departmentId);
                    roleDepartmentDataMapper.save(roleDepartmentDataDO);
                }
            }
        }

        for (Integer departmentId : oldMap.keySet()) {
            RoleDepartmentDataDO roleDepartmentDataDO = oldMap.get(departmentId);
            roleDepartmentDataDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            roleDepartmentDataDO.setUpdateTime(now);
            roleDepartmentDataDO.setUpdateUser(currentUserId);
            roleDepartmentDataMapper.update(roleDepartmentDataDO);
        }
        result.setResult(roleDepartmentData.getRoleId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, RoleDepartmentData> getRoleDepartmentDataListByRole(Integer roleId) {
        ServiceResult<String, RoleDepartmentData> result = new ServiceResult<>();
        List<RoleDepartmentDataDO> roleDepartmentDataDOList = roleDepartmentDataMapper.getRoleDepartmentDataListByRoleId(roleId);
        RoleDepartmentData roleDepartmentData = UserRoleConverter.convertRoleDepartmentDataDOList(roleDepartmentDataDOList);
        result.setResult(roleDepartmentData);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateRoleUserData(RoleUserData roleUserData) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        String currentUserId = userSupport.getCurrentUserId().toString();
        Date now = new Date();
        UserDO userDO = userMapper.findByUserId(roleUserData.getActiveUserId());
        if(userDO==null){
            result.setErrorCode(ErrorCode.USER_NOT_EXISTS);
            return result;
        }
        List<RoleUserDataDO> oldList = roleUserDataMapper.getRoleUserDataListByActiveId(roleUserData.getActiveUserId());
        Map<Integer,RoleUserDataDO> oldMap = ListUtil.listToMap(oldList,"passiveUserId");

        List<User> passiveUserList = roleUserData.getPassiveUserList();
        Set<Integer> passiveUserSet = ListUtil.listToSet(passiveUserList,"userId");
        if(CollectionUtil.isNotEmpty(passiveUserSet)){
            for(Integer userId : passiveUserSet){
                UserDO passiveUserDO = userMapper.findByUserId(userId);
                if(passiveUserDO==null){
                    result.setErrorCode(ErrorCode.USER_NOT_EXISTS);
                    return result;
                }
                if (oldMap.containsKey(userId)) {
                    oldMap.remove(userId);
                    continue;
                } else {
                    RoleUserDataDO roleUserDataDO = new RoleUserDataDO();
                    roleUserDataDO.setActiveUserId(roleUserData.getActiveUserId());
                    roleUserDataDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    roleUserDataDO.setCreateUser(currentUserId);
                    roleUserDataDO.setUpdateUser(currentUserId);
                    roleUserDataDO.setCreateTime(now);
                    roleUserDataDO.setUpdateTime(now);
                    roleUserDataDO.setPassiveUserId(userId);
                    roleUserDataMapper.save(roleUserDataDO);
                }
            }
        }

        for (Integer passiveUserId : oldMap.keySet()) {
            RoleUserDataDO roleUserDataDO = oldMap.get(passiveUserId);
            roleUserDataDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
            roleUserDataDO.setUpdateTime(now);
            roleUserDataDO.setUpdateUser(currentUserId);
            roleUserDataMapper.update(roleUserDataDO);
        }
        result.setResult(roleUserData.getActiveUserId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, RoleUserData> getRoleUserDataListByUser(Integer activeUserId) {
        ServiceResult<String, RoleUserData> result = new ServiceResult<>();
        List<RoleUserDataDO> roleUserDataDOList = roleUserDataMapper.getRoleUserDataListByActiveId(activeUserId);
        RoleUserData roleUserData = UserRoleConverter.convertRoleUserDataDOList(roleUserDataDOList);
        result.setResult(roleUserData);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 已废弃
     * @param activeUserId
     * @return
     */
    @Override
    public ServiceResult<String, RoleUserFinal> rebuildFinalRoleUserData(Integer activeUserId) {
        ServiceResult<String, RoleUserFinal> result = new ServiceResult<>();
        String currentUserId = userSupport.getCurrentUserId().toString();
        Date now = new Date();
        //获取用户【旧的】最终可观察用户列表
        List<RoleUserFinalDO> roleUserFinalList = roleUserFinalMapper.getFinalRoleUserData(activeUserId);
        Map<Integer, String> oldMap = new HashMap();
        for (RoleUserFinalDO roleUserFinalDO : roleUserFinalList) {
            oldMap.put(roleUserFinalDO.getPassiveUserId(), "");
        }

        //获取用户最【新的】最终可观察用户列表
        List<UserDO> userDOList = userMapper.getPassiveUserByUser(activeUserId);
        Map<Integer, String> newMap = new HashMap();
        for (UserDO userDO : userDOList) {
            newMap.put(userDO.getId(), "");
        }

        RoleUserFinalDO roleUserFinalDO = new RoleUserFinalDO();
        roleUserFinalDO.setActiveUserId(activeUserId);
        roleUserFinalDO.setUpdateTime(now);
        roleUserFinalDO.setUpdateUser(currentUserId);
        //旧列表有，新列表没有，数据库删除该条权限
        for (RoleUserFinalDO old : roleUserFinalList) {
            if (!newMap.containsKey(old.getPassiveUserId())) {
                roleUserFinalDO.setPassiveUserId(old.getPassiveUserId());
                roleUserFinalMapper.deleteByActiveUserAadPassiveUser(roleUserFinalDO);
            }
        }
        //旧列表没有，新列表有，数据库添加该条权限
        roleUserFinalDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        roleUserFinalDO.setCreateTime(now);
        roleUserFinalDO.setCreateUser(currentUserId);
        for (UserDO user : userDOList) {
            if (!oldMap.containsKey(user.getId())) {
                roleUserFinalDO.setPassiveUserId(user.getId());
                roleUserFinalMapper.save(roleUserFinalDO);
            }
        }
        roleUserFinalList = roleUserFinalMapper.getFinalRoleUserData(activeUserId);
        result.setResult(UserRoleConverter.convertRoleUserFinalDOList(roleUserFinalList));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, RoleUserFinal> getFinalRoleUserData(Integer activeUserId) {
        ServiceResult<String, RoleUserFinal> result = new ServiceResult<>();
        List<RoleUserFinalDO> roleUserFinalList = roleUserFinalMapper.getFinalRoleUserData(activeUserId);
        result.setResult(UserRoleConverter.convertRoleUserFinalDOList(roleUserFinalList));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    private String verifyOpRole(Role role) {
        if (role == null || StringUtil.isBlank(role.getRoleName())) {
            return ErrorCode.ROLE_NAME_NOT_NULL;
        }
        if (role.getDepartmentId() == null) {
            return ErrorCode.DEPARTMENT_ID_NOT_NULL;
        }
        DepartmentDO departmentDO = departmentMapper.findById(role.getDepartmentId());
        if (departmentDO == null) {
            return ErrorCode.DEPARTMENT_NOT_EXISTS;
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    public ServiceResult<String, CompanyRoleTree> getCompanyRoleTree(UserRoleQueryParam param) {
        ServiceResult<String, CompanyRoleTree> result = new ServiceResult<>();
        CompanyRoleTree roleTree = new CompanyRoleTree();
        List<SubCompany> subCompanyList = new ArrayList<>();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start", 0);
        paramMap.put("pageSize", Integer.MAX_VALUE);
        List<SubCompanyDO> subCompanyDOList = subCompanyMapper.listPage(paramMap);
        if (subCompanyDOList != null && !subCompanyDOList.isEmpty()) {
            for (SubCompanyDO subCompanyDO : subCompanyDOList) {
                DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
                departmentQueryParam.setSubCompanyId(subCompanyDO.getId());
                paramMap.put("departmentQueryParam", departmentQueryParam);
                List<DepartmentDO> departmentDOList = departmentMapper.getRoleList(paramMap);
                List<DepartmentDO> nodeList = DepartmentConverter.convertTree(departmentDOList);
                subCompanyDO.setDepartmentDOList(nodeList);
                subCompanyList.add(ConverterUtil.convert(subCompanyDO, SubCompany.class));
            }
        }
        roleTree.setSubCompanyList(subCompanyList);

        result.setResult(roleTree);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }
}
