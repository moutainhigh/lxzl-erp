package com.lxzl.erp.core.service.user.impl;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.pojo.UserSysDataPrivilege;
import com.lxzl.erp.core.service.user.UserPrevService;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserSysDataPrivilegeMapper;
import com.lxzl.erp.dataaccess.domain.user.UserSysDataPrivilegeDO;
import org.apache.hadoop.mapred.IFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/12/26.
 * Time: 9:04.
 */
@Service("UserPrevService")
public class UserPrevServiceImpl implements UserPrevService {

    @Autowired
    private UserSysDataPrivilegeMapper userSysDataPrivilegeMapper;
    @Autowired
    private UserSupport userSupport;

    /**
     * 取消授权
     * @param
     * @return
     */
    @Override
    public ServiceResult<String, Integer> deleteUserPrev(UserSysDataPrivilege userSysDataPrivilege) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Date now = new Date();
        String currentUserId = userSupport.getCurrentUserId().toString();

        UserSysDataPrivilegeDO userSysDataPrivilegeDO = userSysDataPrivilegeMapper.findByUserId(userSysDataPrivilege.getUserId());
        if (userSysDataPrivilegeDO == null){
            result.setErrorCode(ErrorCode.USER_SYS_DATA_PRIVILEGE_NOT_EXISTS);
            return result;
        }

        userSysDataPrivilegeDO.setDataStatus(CommonConstant.DATA_STATUS_DELETE);
        userSysDataPrivilegeDO.setUpdateTime(now);
        userSysDataPrivilegeDO.setUpdateUser(currentUserId);
        userSysDataPrivilegeMapper.delete(userSysDataPrivilegeDO.getId());

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 单对象添加权限
     *
     * @param userSysDataPrivilege
     * @return
     */
    @Override
    public ServiceResult<String, Integer> AddPrev(UserSysDataPrivilege userSysDataPrivilege) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Date now = new Date();
        String currentUserId = userSupport.getCurrentUserId().toString();

        UserSysDataPrivilegeDO userSysDataPrivilegeDO = new UserSysDataPrivilegeDO();
        userSysDataPrivilegeDO.setUserId(userSysDataPrivilege.getUserId());
        userSysDataPrivilegeDO.setRoleId(userSysDataPrivilege.getRoleId());
        userSysDataPrivilegeDO.setPrivilegeType(userSysDataPrivilege.getPrivilegeType());
        userSysDataPrivilegeDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
        userSysDataPrivilegeDO.setCreateTime(now);
        userSysDataPrivilegeDO.setCreateUser(currentUserId);
        userSysDataPrivilegeDO.setUpdateTime(now);
        userSysDataPrivilegeDO.setUpdateUser(currentUserId);
        Integer res = userSysDataPrivilegeMapper.save(userSysDataPrivilegeDO);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }

    /**
     * 批量添加权限
     *
     * @param userSysDataPrivilegeList
     * @return
     */
    @Override
    public ServiceResult<String, Integer> BatchPrev(List<UserSysDataPrivilege> userSysDataPrivilegeList) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Date now = new Date();
        String currentUserId = userSupport.getCurrentUserId().toString();

        List<UserSysDataPrivilegeDO> userSysDataPrivilegeDOList = new ArrayList<>();
        for (UserSysDataPrivilege userSysDataPrivilege : userSysDataPrivilegeList){
            UserSysDataPrivilegeDO userSysDataPrivilegeDO = new UserSysDataPrivilegeDO();
            userSysDataPrivilegeDO.setUserId(userSysDataPrivilege.getUserId());
            userSysDataPrivilegeDO.setRoleId(userSysDataPrivilege.getRoleId());
            userSysDataPrivilegeDO.setPrivilegeType(userSysDataPrivilege.getPrivilegeType());
            userSysDataPrivilegeDO.setDataStatus(CommonConstant.DATA_STATUS_ENABLE);
            userSysDataPrivilegeDO.setCreateTime(now);
            userSysDataPrivilegeDO.setCreateUser(currentUserId);
            userSysDataPrivilegeDO.setUpdateTime(now);
            userSysDataPrivilegeDO.setUpdateUser(currentUserId);
            userSysDataPrivilegeDOList.add(userSysDataPrivilegeDO);
        }
        userSysDataPrivilegeMapper.saveList(userSysDataPrivilegeDOList);

        result.setErrorCode(ErrorCode.SUCCESS);
        return result;
    }


}
