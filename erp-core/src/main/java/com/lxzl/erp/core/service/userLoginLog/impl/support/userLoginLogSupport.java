package com.lxzl.erp.core.service.userLoginLog.impl.support;

import com.lxzl.erp.dataaccess.dao.mysql.userLoginLog.UserLoginLogMapper;
import com.lxzl.erp.dataaccess.domain.userLoginLog.UserLoginLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/22
 * @Time : Created in 20:05
 */
@Component
public class userLoginLogSupport {

    @Autowired
    UserLoginLogMapper userLoginLogMapper;

    public void addUserLoginLog(String userName, String loginIp, String loginMacAddress) {
        if (loginIp == null) {
            return;
        }
        UserLoginLogDO userLoginLogDO = new UserLoginLogDO();
        userLoginLogDO.setUserName(userName);
        userLoginLogDO.setLoginIp(loginIp);
        userLoginLogDO.setLoginMacAddress(loginMacAddress);
        userLoginLogDO.setCreateTime(new Date());
        userLoginLogMapper.save(userLoginLogDO);
    }
}
