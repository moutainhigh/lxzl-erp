package com.lxzl.erp.web.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/12/24.
 * Time: 10:52.
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(SecurityInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        /*UserResponse userResponse = (UserResponse) session.getAttribute(CommonConstant.USER_SESSION_KEY);
        if (userResponse == null) {
            return false;
        }*/

        ServiceResult<String, Boolean> result = new ServiceResult<>();
        if(!result.getResult()){
            response.sendRedirect(request.getContextPath()+"/index");
        }
        return result.getResult();
    }

    //模块权限值
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }


}
