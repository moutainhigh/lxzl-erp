package com.lxzl.erp.core.service.user.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class UserSupport {

    @Autowired(required = false)
    private HttpSession httpSession;


    public User getCurrentUser(){
        return (User) httpSession.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
    }
    public Integer getCurrentUserId(){
       User user = (User) httpSession.getAttribute(CommonConstant.ERP_USER_SESSION_KEY);
       return user.getUserId();
    }
}
