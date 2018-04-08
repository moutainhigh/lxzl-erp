package com.lxzl.erp.web;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.se.common.exception.BusinessException;
import com.lxzl.se.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * User : LiuKe
 * Date : 2016/12/30
 * Time : 16:37
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> exception(BusinessException businessException , WebRequest request) {
        StringWriter exceptionFormat=new StringWriter();
        businessException.printStackTrace(new PrintWriter(exceptionFormat,true));
        log.error("ExceptionHandlerAdvice catch the BusinessException, ", businessException);
        if(StringUtil.isBlank(businessException.getMessage())){
            return new ResponseEntity<Object>(resultGenerator.generate(ErrorCode.SYSTEM_ERROR,ErrorCode.getMessage(ErrorCode.SYSTEM_ERROR) ),HttpStatus.OK);
        }else{
            String code = businessException.getCode();
            code = code==null?ErrorCode.BUSINESS_EXCEPTION:code;
            String description = businessException.getMessage();
            if(StringUtil.isNotBlank(description)){//获取到了错误码对应的描述，则返回错误码描述
                if(StringUtil.isNotBlank(ErrorCode.getMessage(description))){
                    return new ResponseEntity<Object>(resultGenerator.generate(description,ErrorCode.getMessage(description)),HttpStatus.OK);
                }
                return new ResponseEntity<Object>(resultGenerator.generate(code,description),HttpStatus.OK);
            }else{//没有获取到错误码对应的描述，则直接返回异常的描述
                return new ResponseEntity<Object>(resultGenerator.generate(code,ErrorCode.getMessage(code)),HttpStatus.OK);
            }
        }
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception exception , WebRequest request) {
        StringWriter exceptionFormat=new StringWriter();
        exception.printStackTrace(new PrintWriter(exceptionFormat,true));
        log.error("ExceptionHandlerAdvice catch the SystemException, ", exception);
        if(StringUtil.isEmpty(ErrorCode.getMessage(exception.getMessage()))){
            return new ResponseEntity<Object>(resultGenerator.generate(ErrorCode.SYSTEM_ERROR,ErrorCode.getMessage(ErrorCode.SYSTEM_ERROR) ),HttpStatus.OK);
        }else{
            return new ResponseEntity<Object>(resultGenerator.generate(exception.getMessage(),ErrorCode.getMessage(exception.getMessage()) ),HttpStatus.OK);
        }
    }
    @Autowired
    private ResultGenerator resultGenerator;
}
