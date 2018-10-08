package com.lxzl.erp.core.service.user.impl;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.core.service.user.UserPrevService;
import com.lxzl.erp.dataaccess.dao.mysql.user.UserPrevMapper;
import com.lxzl.erp.dataaccess.domain.user.UserPrevDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserPrevMapper userPrevMapper;

    /**
     * @param userPrevDO
     * @return
     */
    @Override
    public ServiceResult<String, Integer> insetUserPrev(UserPrevDO userPrevDO) {

        ServiceResult<String, Integer> result = new ServiceResult<>();
        Integer res = userPrevMapper.save(userPrevDO);
        if (res <= 0) {
            result.setErrorCode(ErrorCode.USER_NAME_NOT_FOUND);
        } else {
            result.setErrorCode(ErrorCode.SUCCESS);
            result.setResult(res);
        }
        return result;
    }

    /**
     * 单对象添加权限
     *
     * @param userPrevDO
     * @return
     */
    @Override
    public ServiceResult<String, Integer> AddPrev(UserPrevDO userPrevDO) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Integer res = userPrevMapper.AddPrev(userPrevDO);

        result.setResult(res);

        return null;
    }

    /**
     * 批量添加权限
     *
     * @param userPrevDOs
     * @return
     */
    @Override
    public ServiceResult<String, Integer> BatchPrev(List<UserPrevDO> userPrevDOs) {
        ServiceResult<String, Integer> result = new ServiceResult<>();
        Integer res = userPrevMapper.BatchPrev(userPrevDOs);

        result.setResult(res);
        return result;
    }


}
