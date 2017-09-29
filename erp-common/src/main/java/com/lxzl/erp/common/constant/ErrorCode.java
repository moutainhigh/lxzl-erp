package com.lxzl.erp.common.constant;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {
    private static Map<String,String> MAP = new HashMap<String,String>();
    /**
     * 返回码
     */
    public static final String SUCCESS = "J000000";
    public static final String CUSTOM_ERROR = "J000996";
    public static final String BUSINESS_EXCEPTION = "J000997";
    public static final String SYSTEM_EXCEPTION = "J000998";
    public static final String SYSTEM_ERROR = "J000999";
    public static final String RECORD_NOT_EXISTS = "J009999";
    public static final String USER_NOT_LOGIN = "J000995";
    static
    {
        MAP.put(SUCCESS,"成功");
        MAP.put(BUSINESS_EXCEPTION,"业务异常");
        MAP.put(SYSTEM_EXCEPTION,"系统异常");
        MAP.put(SYSTEM_ERROR,"系统错误,请联系管理员");
        MAP.put(RECORD_NOT_EXISTS,"记录不存在");
        MAP.put(USER_NOT_LOGIN,"用户未登录");

    }

    public static String getMessage(String code)
    {
        return MAP.get(code);
    }

    public static String getMessage(String code , String parmName)
    {
        return MAP.get(code) + " : "+parmName;
    }

    public static String clear(String code)
    {
        return MAP.put(code,"");
    }
}
