package com.lxzl.erp.core.component;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.se.common.domain.Result;
import com.lxzl.se.common.util.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class ResultGenerator {
    private static final String SUCCESS_CODE = ErrorCode.SUCCESS;
    private static final String CUSTOM_ERROR_CODE = ErrorCode.CUSTOM_ERROR;

    public Result generate(String code, Object obj) {
        Result result = null;
        if (SUCCESS_CODE.equals(code)) {
            result = new Result(SUCCESS_CODE, ErrorCode.getMessage(SUCCESS_CODE), true);
            result.setProperty("data", obj);
        } else if (StringUtil.isEmpty(ErrorCode.getMessage(code))) {
            result = new Result(ErrorCode.BUSINESS_EXCEPTION, code, false);
        } else {
            result = new Result(code, ErrorCode.getMessage(code), false);
        }

        if (CUSTOM_ERROR_CODE.equals(code)) {
            ErrorCode.clear(CUSTOM_ERROR_CODE);
        }
        return result;
    }

    public Result generate(String code) {
        return generate(code, "");
    }

    public Result generate(ServiceResult serviceResult) {
        String code = (String)serviceResult.getErrorCode();
        Object data = serviceResult.getResult();
        Object[] formatArgs = serviceResult.getFormatArgs();
        Result result = null;
        if (SUCCESS_CODE.equals(code)) {
            result = new Result(SUCCESS_CODE, ErrorCode.getMessage(SUCCESS_CODE), true);
            result.setProperty("data", data);
        } else if (StringUtil.isEmpty(ErrorCode.getMessage(code))) {
            result = new Result(ErrorCode.BUSINESS_EXCEPTION, data.toString(), false);
        } else if(formatArgs==null||formatArgs.length==0){
            result = new Result(code, ErrorCode.getMessage(code), false);
        } else{
            result = new Result(code, String.format(ErrorCode.getMessage(code),formatArgs), false);
        }

        if (CUSTOM_ERROR_CODE.equals(code)) {
            ErrorCode.clear(CUSTOM_ERROR_CODE);
        }
        return result;
    }
}
