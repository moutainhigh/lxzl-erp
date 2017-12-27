package com.lxzl.erp.web.test;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.RedisKeyConstant;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import com.lxzl.erp.common.util.ListUtil;
import com.lxzl.erp.core.service.user.UserService;
import com.lxzl.erp.dataaccess.dao.redis.RedisManager;
import com.lxzl.se.unit.test.BaseUnTransactionalTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-21 20:54
 */
public class RedisTest extends BaseUnTransactionalTest {
    @Test
    public void addUser() {
        {
            UserQueryParam userQueryParam = new UserQueryParam();
            ServiceResult<String, List<User>> getUserResult = userService.getUserListByParam(userQueryParam);
            if (ErrorCode.SUCCESS.equals(getUserResult.getErrorCode())) {
                List<User> userList = getUserResult.getResult();
                Map<Integer, User> userMap = ListUtil.listToMap(userList, "userId");
                redisManager.add(RedisKeyConstant.USER_KEY, userMap);
            }
        }
    }

    @Test
    public void getUser(){
        String str = redisManager.get(RedisKeyConstant.USER_KEY);
    }


    @Autowired
    private UserService userService;

    @Autowired
    private RedisManager redisManager;
}
