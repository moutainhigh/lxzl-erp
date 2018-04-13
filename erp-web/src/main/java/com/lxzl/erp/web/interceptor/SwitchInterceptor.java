package com.lxzl.erp.web.interceptor;


import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.interfaceSwitch.SwitchQueryParam;
import com.lxzl.erp.common.util.StrReplaceUtil;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.dataaccess.dao.mysql.functionSwitch.SwitchMapper;
import com.lxzl.erp.dataaccess.domain.interfaceSwitch.SwitchDO;
import com.lxzl.se.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/12/24.
 * Time: 10:52.
 */
public class SwitchInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(SwitchInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        requestPath = StrReplaceUtil.formatInterfaceUrl(requestPath.replace(contextPath,""));
        SwitchDO switchDO = switchMapper.findByInterfaceUrl(requestPath);
         if(switchDO != null&&CommonConstant.COMMON_CONSTANT_NO.equals(switchDO.getIsOpen())){
            throw new BusinessException(ErrorCode.SWITCH_CLOSE,ErrorCode.getMessage(ErrorCode.SWITCH_CLOSE));
        }
        return true;
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

    @Autowired
    private SwitchMapper switchMapper;
    @Autowired
    private ResultGenerator resultGenerator;
}
