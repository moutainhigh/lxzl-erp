package com.lxzl.erp.core.service.user.impl;

import com.lxzl.erp.common.cache.CommonCache;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.UserType;
import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.LoginParam;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.dataaccess.dao.mysql.company.DepartmentMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMapper;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserRoleMapper;
import com.lxzl.erp.dataaccess.domain.user.RoleDO;
import com.lxzl.erp.dataaccess.domain.user.UserDO;
import com.lxzl.erp.dataaccess.domain.user.UserRoleDO;
import com.lxzl.se.common.util.secret.MD5Util;
import com.lxzl.se.core.service.impl.BaseServiceImpl;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("UserService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, User> login(LoginParam loginParam, String ip) {
        ServiceResult<String, User> result = new ServiceResult<>();
        UserDO userDO = userMapper.findByUsername(loginParam.getUserName());
        if (userDO == null) {
            result.setErrorCode(ErrorCode.USER_NAME_NOT_FOUND);
        } else if (userDO.getIsDisabled().equals(CommonConstant.COMMON_CONSTANT_YES)) {
            result.setErrorCode(ErrorCode.USER_DISABLE);
        } else if (userDO.getIsActivated().equals(CommonConstant.COMMON_CONSTANT_NO)) {
            result.setErrorCode(ErrorCode.USER_NOT_ACTIVATED);
        } else if (!userDO.getPassword().equals(generateMD5Password(userDO.getUserName(), loginParam.getPassword(), ApplicationConfig.authKey))) {
            result.setErrorCode(ErrorCode.USER_PASSWORD_ERROR);
        } else {
            User user = ConverterUtil.convert(userDO, User.class);
            userDO.setLastLoginIp(ip);
            userDO.setLastLoginTime(new Date());
            userMapper.update(userDO);
            session.setAttribute(CommonConstant.ERP_USER_SESSION_KEY, user);
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(user);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> addUser(User user) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        Date currentTime = new Date();
        ServiceResult<String, Integer> result = new ServiceResult<>();
        UserDO userDO = userMapper.findByUsername(user.getUserName());
        if (userDO != null) {
            result.setErrorCode(ErrorCode.USER_EXISTS);
            return result;
        }
        userDO = ConverterUtil.convert(user, UserDO.class);
        List<Role> roleList = user.getRoleList();
        Map<Integer, String> finalRoleIdMap = new HashMap<Integer, String>();
        String validResult = validRoleIdList(roleList, finalRoleIdMap);
        if (!ErrorCode.SUCCESS.equals(validResult)) {
            result.setErrorCode(validResult);
            return result;
        }
        userDO.setUserType(UserType.USER_TYPE_DEFAULT);
        userDO.setRegisterTime(currentTime);
        userDO.setPassword(generateMD5Password(userDO.getUserName(), user.getPassword(), ApplicationConfig.authKey));
        if (userDO.getIsActivated() == null) {
            userDO.setIsActivated(CommonConstant.COMMON_CONSTANT_YES);
        } else {
            userDO.setIsActivated(user.getIsActivated());
        }
        if (userDO.getIsDisabled() == null) {
            userDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_NO);
        } else {
            userDO.setIsDisabled(user.getIsDisabled());
        }
        if (loginUser != null) {
            userDO.setCreateUser(loginUser.getUserId().toString());
            userDO.setUpdateUser(loginUser.getUserId().toString());
        }
        userDO.setCreateTime(currentTime);
        userDO.setUpdateTime(currentTime);
        userMapper.save(userDO);
        CommonCache.userMap.put(userDO.getId(), ConverterUtil.convert(userDO, User.class));
        saveRoleMap(userDO, finalRoleIdMap, currentTime, loginUser);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(userDO.getId());
        return result;
    }

    private void saveRoleMap(UserDO userDO, Map<Integer, String> finalRoleIdMap, Date currentTime, User loginUser) {
        for (Integer roleId : finalRoleIdMap.keySet()) {
            if (roleId != null) {
                UserRoleDO userRoleDO = new UserRoleDO();
                userRoleDO.setUserId(userDO.getId());
                userRoleDO.setRoleId(roleId);
                userRoleDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                userRoleDO.setCreateTime(currentTime);
                userRoleDO.setUpdateTime(currentTime);
                if (loginUser != null) {
                    userRoleDO.setCreateUser(loginUser.getUserId().toString());
                    userRoleDO.setUpdateUser(loginUser.getUserId().toString());
                }
                userRoleMapper.save(userRoleDO);
            }
        }
    }

    private String validRoleIdList(List<Role> roleList, Map<Integer, String> finalRoleIdMap) {
        if (roleList == null || roleList.size() == 0) {
            return ErrorCode.USER_ROLE_NOT_NULL;
        }
        for (Role role : roleList) {
            Integer roleId = role.getRoleId();
            if (roleId != null) {
                RoleDO roleDO = roleMapper.findByMapId(roleId);
                if (roleDO == null) {
                    return ErrorCode.ROLE_NOT_NULL;
                } else if (finalRoleIdMap.get(roleId) == null) {
                    finalRoleIdMap.put(roleId, "");
                }
            }
        }
        if (finalRoleIdMap.size() == 0) {
            return ErrorCode.USER_ROLE_NOT_NULL;
        }
        return ErrorCode.SUCCESS;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, Integer> updateUser(User user) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        UserDO userDO = userMapper.findByUserId(user.getUserId());
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);

        Date currentTime = new Date();
        if (userDO == null) {
            result.setErrorCode(ErrorCode.USER_NOT_EXISTS);
            return result;
        }
        userDO = ConverterUtil.convert(user, UserDO.class);

        List<Role> roleList = user.getRoleList();
        Map<Integer, String> finalRoleIdMap = new HashMap<Integer, String>();
        String validResult = validRoleIdList(roleList, finalRoleIdMap);
        if (!ErrorCode.SUCCESS.equals(validResult)) {
            result.setErrorCode(validResult);
            return result;
        }
        Map<Integer, Integer> modifyRoleIdMap = new HashMap<Integer, Integer>();
        List<UserRoleDO> oldUserRoleList = userRoleMapper.findListByUserId(user.getUserId());
        Map<Integer, UserRoleDO> oldRoleIdMap = new HashMap<>();
        for (UserRoleDO userRoleDO : oldUserRoleList) {
            oldRoleIdMap.put(userRoleDO.getRoleId(), userRoleDO);
        }
        for (Integer oldRoleId : oldRoleIdMap.keySet()) {
            //新的角色列表中找不到原角色列表中的某角色，则标记删除
            if (finalRoleIdMap.get(oldRoleId) == null) {
                modifyRoleIdMap.put(oldRoleId, CommonConstant.DATA_STATUS_DELETE);
            }
        }
        for (Integer newRoleId : finalRoleIdMap.keySet()) {
            //原角色列表中找不到新角色列表中的某角色，则标记新增
            if (oldRoleIdMap.get(newRoleId) == null) {
                modifyRoleIdMap.put(newRoleId, CommonConstant.DATA_STATUS_ENABLE);
            }
        }
        //本接口不做修改密码操作
        userDO.setPassword(null);
        userDO.setUpdateUser(loginUser.getUserId().toString());
        userDO.setUpdateTime(currentTime);
        userMapper.update(userDO);
        CommonCache.userMap.put(userDO.getId(), ConverterUtil.convert(userDO, User.class));

        for (Integer roleId : modifyRoleIdMap.keySet()) {
            UserRoleDO userRoleDO = null;
            if (roleId != null) {
                //标记新增的做新增操作
                if (CommonConstant.DATA_STATUS_ENABLE.equals(modifyRoleIdMap.get(roleId))) {
                    userRoleDO = new UserRoleDO();
                    userRoleDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
                    userRoleDO.setUserId(userDO.getId());
                    userRoleDO.setRoleId(roleId);
                    userRoleDO.setCreateTime(currentTime);
                    userRoleDO.setUpdateTime(currentTime);
                    userRoleDO.setCreateUser(loginUser.getUserId().toString());
                    userRoleDO.setUpdateUser(loginUser.getUserId().toString());
                    userRoleMapper.save(userRoleDO);
                }
                //标记删除的做删除操作
                if (CommonConstant.DATA_STATUS_DELETE.equals(modifyRoleIdMap.get(roleId))) {
                    userRoleDO = oldRoleIdMap.get(roleId);
                    userRoleDO.setCreateUser(null);
                    userRoleDO.setUpdateUser(loginUser.getUserId().toString());
                    userRoleDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
                    userRoleMapper.update(userRoleDO);
                }
            }
        }

        result.setResult(userDO.getId());
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, Integer> updateUserPassword(User user) {
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Date currentTime = new Date();
        UserDO userDO = userMapper.findByUserId(user.getUserId());
        if (userDO == null) {
            result.setErrorCode(ErrorCode.USER_NOT_EXISTS);
            return result;
        }

        if (!userRoleService.isSuperAdmin(loginUser.getUserId()) && !loginUser.getUserId().equals(user.getUserId())) {
            result.setErrorCode(ErrorCode.OPERATOR_IS_NOT_YOURSELF);
            return result;
        }

        userDO.setPassword(generateMD5Password(userDO.getUserName(), user.getPassword(), ApplicationConfig.authKey));
        userDO.setUpdateTime(currentTime);
        userDO.setUpdateUser(loginUser.getUserId().toString());
        userMapper.update(userDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(userDO.getId());
        return result;
    }

    @Override
    public ServiceResult<String, User> getUserById(Integer userId) {
        ServiceResult<String, User> result = new ServiceResult<>();
        UserDO userDO = userMapper.findByUserId(userId);
        if (userDO == null) {
            result.setErrorCode(ErrorCode.USER_NOT_EXISTS);
            return result;
        }

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(ConverterUtil.convert(userDO, User.class));
        return result;
    }

    @Override
    public ServiceResult<String, Page<User>> userPage(UserQueryParam userQueryParam) {
        ServiceResult<String, Page<User>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(userQueryParam.getPageNo(), userQueryParam.getPageSize());

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", pageQuery.getStart());
        maps.put("pageSize", pageQuery.getPageSize());
        maps.put("userQueryParam", userQueryParam);

        Integer totalCount = userMapper.listCount(maps);
        List<UserDO> list = userMapper.listPage(maps);
        List<User> userList = ConverterUtil.convertList(list, User.class);
        Page<User> page = new Page<>(userList, totalCount, userQueryParam.getPageNo(), userQueryParam.getPageSize());
        result.setResult(page);
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    @Override
    public ServiceResult<String, List<User>> getUserListByParam(UserQueryParam userQueryParam) {
        ServiceResult<String, List<User>> result = new ServiceResult<>();

        Map<String, Object> maps = new HashMap<>();
        maps.put("start", 0);
        maps.put("pageSize", Integer.MAX_VALUE);
        maps.put("userQueryParam", userQueryParam);
        List<UserDO> userDoList = userMapper.listPage(maps);
        result.setResult(ConverterUtil.convertList(userDoList, User.class));
        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


    private String generateMD5Password(String username, String password, String md5Key) {
        String value = MD5Util.encryptWithKey(username + password, md5Key);
        return value;
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.encrypt("123" + CommonConstant.MD5_KEY));
    }
}
