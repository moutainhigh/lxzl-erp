package com.lxzl.erp.core.service.user.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.UserType;
import com.lxzl.erp.common.domain.ApplicationConfig;
import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.erp.user.LoginParam;
import com.lxzl.erp.common.domain.erp.user.User;
import com.lxzl.erp.common.domain.erp.user.UserPageParam;
import com.lxzl.erp.core.service.user.UserRoleService;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.core.service.user.impl.support.UserConverter;
import com.lxzl.erp.dataaccess.dao.mysql.user.RoleMysqlDAO;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserMysqlDAO;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserRoleMysqlDAO;
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
    private UserMysqlDAO userMysqlDAO;

    @Autowired(required = false)
    private HttpSession session;

    @Autowired
    private UserRoleMysqlDAO userRoleMysqlDAO;

    @Autowired
    private RoleMysqlDAO roleMysqlDAO;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public ServiceResult<String, User> login(LoginParam loginParam, String ip) {
        ServiceResult<String, User> result = new ServiceResult<>();
        UserDO userDO = userMysqlDAO.findByUsername(loginParam.getUserName());
        if (userDO == null) {
            result.setErrorCode(ErrorCode.USER_NAME_NOT_FOUND);
        } else if (userDO.getIsDisabled().equals(CommonConstant.COMMON_CONSTANT_NO)) {
            result.setErrorCode(ErrorCode.USER_DISABLE);
        } else if (userDO.getIsActivated().equals(CommonConstant.COMMON_CONSTANT_NO)) {
            result.setErrorCode(ErrorCode.USER_NOT_ACTIVATED);
        } else if (!userDO.getPassword().equals(generateMD5Password(userDO.getUserName(), loginParam.getPassword(), ApplicationConfig.authKey))) {
            result.setErrorCode(ErrorCode.USER_PASSWORD_ERROR);
        } else {
            userDO.setRoleList(userRoleMysqlDAO.findRoleListByUserId(userDO.getId()));
            User user = UserConverter.convert(userDO);
            userDO.setLastLoginIp(ip);
            userDO.setLastLoginTime(new Date());
            userMysqlDAO.update(userDO);
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
        UserDO userDO = userMysqlDAO.findByUsername(user.getUserName());
        if (userDO != null) {
            result.setErrorCode(ErrorCode.USER_EXISTS);
            return result;
        }
        userDO = UserConverter.convert(user);
        List<Integer> roleIdList = user.getRoleList();
        Map<Integer, String> finalRoleIdMap = new HashMap<Integer, String>();
        /*String validResult = validRoleIdList(roleIdList, finalRoleIdMap);
        if (!ErrorCode.SUCCESS.equals(validResult)) {
            result.setErrorCode(validResult);
            return result;
        }*/
        userDO.setUserType(UserType.USER_TYPE_DEFAULT);
        userDO.setRegisterTime(currentTime);
        userDO.setPassword(generateMD5Password(userDO.getUserName(), user.getPassword(), ApplicationConfig.authKey));
        if (userDO.getIsActivated() == null) {
            userDO.setIsActivated(CommonConstant.COMMON_CONSTANT_YES);
        } else {
            userDO.setIsActivated(user.getIsActivated());
        }
        if (userDO.getIsDisabled() == null) {
            userDO.setIsDisabled(CommonConstant.COMMON_CONSTANT_YES);
        } else {
            userDO.setIsDisabled(user.getIsDisabled());
        }
        if (loginUser != null) {
            userDO.setCreateUser(loginUser.getUserId().toString());
            userDO.setUpdateUser(loginUser.getUserId().toString());
        }
        userDO.setCreateTime(currentTime);
        userDO.setUpdateTime(currentTime);
        userMysqlDAO.save(userDO);
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
                userRoleDO.setDataStatus(CommonConstant.COMMON_CONSTANT_OP_CREATE);
                userRoleDO.setCreateTime(currentTime);
                userRoleDO.setUpdateTime(currentTime);
                if (loginUser != null) {
                    userRoleDO.setCreateUser(loginUser.getUserId().toString());
                    userRoleDO.setUpdateUser(loginUser.getUserId().toString());
                }
                userRoleMysqlDAO.save(userRoleDO);
            }
        }
    }

    private String validRoleIdList(List<Integer> roleIdList, Map<Integer, String> finalRoleIdMap) {
        if (roleIdList == null || roleIdList.size() == 0) {
            return ErrorCode.USER_ROLE_NOT_NULL;
        }
        for (Integer roleId : roleIdList) {
            if (roleId != null) {
                RoleDO roleDO = roleMysqlDAO.findByMapId(roleId);
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
        UserDO userDO = userMysqlDAO.findByUserId(user.getUserId());
        User loginUser = (User) session.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);

        Date currentTime = new Date();
        if (userDO == null) {
            result.setErrorCode(ErrorCode.USER_NOT_EXISTS);
            return result;
        }
        userDO = UserConverter.convert(user);

        List<Integer> roleIdList = user.getRoleList();
        Map<Integer, String> finalRoleIdMap = new HashMap<Integer, String>();
        /*String validResult = validRoleIdList(roleIdList, finalRoleIdMap);
        if (!ErrorCode.SUCCESS.equals(validResult)) {
            result.setErrorCode(validResult);
            return result;
        }*/
        Map<Integer, Integer> modifyRoleIdMap = new HashMap<Integer, Integer>();
        List<UserRoleDO> oldUserRoleList = userRoleMysqlDAO.findListByUserId(user.getUserId());
        Map<Integer, UserRoleDO> oldRoleIdMap = new HashMap<>();
        for (UserRoleDO userRoleDO : oldUserRoleList) {
            oldRoleIdMap.put(userRoleDO.getRoleId(), userRoleDO);
        }
        for (Integer oldRoleId : oldRoleIdMap.keySet()) {
            //新的角色列表中找不到原角色列表中的某角色，则标记删除
            if (finalRoleIdMap.get(oldRoleId) == null) {
                modifyRoleIdMap.put(oldRoleId, CommonConstant.COMMON_CONSTANT_OP_DELETE);
            }
        }
        for (Integer newRoleId : finalRoleIdMap.keySet()) {
            //原角色列表中找不到新角色列表中的某角色，则标记新增
            if (oldRoleIdMap.get(newRoleId) == null) {
                modifyRoleIdMap.put(newRoleId, CommonConstant.COMMON_CONSTANT_OP_CREATE);
            }
        }
        //本接口不做修改密码操作
        userDO.setPassword(null);
        userDO.setUpdateUser(loginUser.getUserId().toString());
        userDO.setUpdateTime(currentTime);
        userMysqlDAO.update(userDO);

        for (Integer roleId : modifyRoleIdMap.keySet()) {
            UserRoleDO userRoleDO = null;
            if (roleId != null) {
                //标记新增的做新增操作
                if (CommonConstant.COMMON_CONSTANT_OP_CREATE.equals(modifyRoleIdMap.get(roleId))) {
                    userRoleDO = new UserRoleDO();
                    userRoleDO.setDataStatus(CommonConstant.COMMON_CONSTANT_OP_CREATE);
                    userRoleDO.setUserId(userDO.getId());
                    userRoleDO.setRoleId(roleId);
                    userRoleDO.setCreateTime(currentTime);
                    userRoleDO.setUpdateTime(currentTime);
                    userRoleDO.setCreateUser(loginUser.getUserId().toString());
                    userRoleDO.setUpdateUser(loginUser.getUserId().toString());
                    userRoleMysqlDAO.save(userRoleDO);
                }
                //标记删除的做删除操作
                if (CommonConstant.COMMON_CONSTANT_OP_DELETE.equals(modifyRoleIdMap.get(roleId))) {
                    userRoleDO = oldRoleIdMap.get(roleId);
                    userRoleDO.setDataStatus(CommonConstant.COMMON_CONSTANT_OP_DELETE);
                    userRoleMysqlDAO.update(userRoleDO);
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
        UserDO userDO = userMysqlDAO.findByUserId(user.getUserId());
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
        userMysqlDAO.update(userDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(userDO.getId());
        return result;
    }

    @Override
    public ServiceResult<String, User> getUserById(Integer userId) {
        ServiceResult<String, User> result = new ServiceResult<>();
        UserDO userDO = userMysqlDAO.findByUserId(userId);
        if (userDO == null) {
            result.setErrorCode(ErrorCode.USER_NOT_EXISTS);
            return result;
        }
        List<UserRoleDO> roleDOList = userRoleMysqlDAO.findListByUserId(userId);
        userDO.setUserRoleList(roleDOList);
        result.setErrorCode(ErrorCode.SUCCESS);
        result.setResult(UserConverter.convert(userDO));
        return result;
    }

    @Override
    public ServiceResult<String, Page<User>> userPage(UserPageParam userPageParam) {
        ServiceResult<String, Page<User>> result = new ServiceResult<>();
        PageQuery pageQuery = new PageQuery(userPageParam.getPageNo(), userPageParam.getPageSize());
        Integer totalCount = userMysqlDAO.listCount(userPageParam, pageQuery);
        List<UserDO> list = userMysqlDAO.listPage(userPageParam, pageQuery);
        List<User> userList = UserConverter.convertUserList(list);
        Page<User> page = new Page<>(userList, totalCount, userPageParam.getPageNo(), userPageParam.getPageSize());
        result.setResult(page);
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
