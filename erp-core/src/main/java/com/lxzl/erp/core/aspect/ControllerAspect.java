package com.lxzl.erp.core.aspect;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.se.common.domain.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;


/**
 * User : LiuKe
 * Date : 2016/11/5
 * Time : 10:16
 */
@Component
@Aspect
@Order(value=2)
public class ControllerAspect {

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void controllerAspect(){

    }
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerAspect(){

    }

    @Around("controllerAspect()||restControllerAspect()")
    public Object valid(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Object result = null;
        for(Object arg : args){
            if(arg instanceof BindingResult){
                BindingResult validResult = (BindingResult)arg;
                if(validResult.hasErrors()){
                    String code = validResult.getFieldError().getDefaultMessage();
                    result = new Result(code, ErrorCode.getMessage(code),false);
                    return result;
                }
            }
        }
        result = pjp.proceed();
        return result;
    }
}
