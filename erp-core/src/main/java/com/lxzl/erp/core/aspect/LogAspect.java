package com.lxzl.erp.core.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User : LiuKe
 * Date : 2016/12/24
 * Time : 9:47
 */
@Component
@Aspect
@Order(value=1)
public class LogAspect {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.lxzl.erp.core.annotation.ControllerLog)")
    public void controllerLogMethodCut(){}
    @Pointcut("@within(com.lxzl.erp.core.annotation.ControllerLog)")
    public void controllerLogCut(){}

    @Around("controllerLogMethodCut()||controllerLogCut()")
    public Object controllerLogRequest(ProceedingJoinPoint pjp) throws Throwable {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String url = request.getServletPath().toString();
        if("".equals(url)){
            url = request.getPathInfo();
        }
        if("".equals(url)){
            url = request.getRequestURI();
        }
        log.info("**********************************************  Request To Controller ("+url+") **********************************************");
        log.info("------------------------  Request Parameters  ------------------------startTime: " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS").format(new Date()));
        Object[] os = pjp.getArgs();
        Object result = null;
        try{
            for(Object o : os ){
                if(o instanceof ServletResponse){
                    continue;
                }else if(o instanceof Model){
                    log.info("org.springframework.ui.Model  ---------   "+JSON.toJSONString(o));
                }else if(o instanceof ServletRequest){
                    log.info("ServletRequest ParameterMap  ---------   "+JSON.toJSONString(((ServletRequest) o).getParameterMap()));
                }else if(!(o instanceof BindingResult)){
                    log.info(JSON.toJSONString(o));
                }
            }
        }catch (Error e){
            log.info("Controller Log Error ---------   "+e.getMessage());
        }finally {
            result = pjp.proceed();
        }
        return result;
    }
    @AfterReturning(value="controllerLogMethodCut()||controllerLogCut()",returning="obj")
    public Object controllerLogResponse(Object obj) throws Throwable {
        log.info("------------------------  Response Result  ------------------------");
        log.info(JSON.toJSONString(obj));
        log.info("**********************************************  Request End **********************************************endTime: " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS").format(new Date()));
        return obj;
    }
}
