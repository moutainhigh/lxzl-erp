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
    public static final String ID_NOT_NULL = "J000994";

    public static final String USER_NAME_NOT_NULL = "J100000";
    public static final String USER_PASSWORD_NOT_NULL = "J100001";
    public static final String USER_DISABLE = "J100002";
    public static final String USER_NOT_ACTIVATED = "J100003";
    public static final String USER_NAME_NOT_FOUND = "J100004";
    public static final String USER_PASSWORD_ERROR = "J100005";
    public static final String USER_EXISTS = "J100015";
    public static final String USER_NOT_EXISTS = "J100016";
    public static final String USER_ROLE_NOT_NULL = "J100017";
    public static final String ROLE_NOT_NULL = "J100018";
    public static final String ROLE_HAVE_USER = "J100019";
    public static final String ROLE_NAME_NOT_NULL = "J100020";
    public static final String OPERATOR_IS_NOT_YOURSELF = "J900015";



    static
    {
        MAP.put(SUCCESS,"成功");
        MAP.put(ID_NOT_NULL,"ID不能为空");
        MAP.put(BUSINESS_EXCEPTION,"业务异常");
        MAP.put(SYSTEM_EXCEPTION,"系统异常");
        MAP.put(SYSTEM_ERROR,"系统错误,请联系管理员");
        MAP.put(RECORD_NOT_EXISTS,"记录不存在");
        MAP.put(USER_NOT_LOGIN,"用户未登录");
        MAP.put(USER_DISABLE,"用户已禁用，请联系管理员");
        MAP.put(USER_NAME_NOT_FOUND,"用户名不存在");
        MAP.put(USER_NOT_ACTIVATED,"用户未激活，请联系管理员");
        MAP.put(USER_PASSWORD_ERROR,"密码错误");
        MAP.put(USER_EXISTS,"用户已存在");
        MAP.put(USER_NAME_NOT_NULL,"用户名不能为空");
        MAP.put(USER_PASSWORD_NOT_NULL,"密码不能为空");
        MAP.put(USER_ROLE_NOT_NULL,"用户角色不能为空");
        MAP.put(ROLE_NOT_NULL,"角色不存在");
        MAP.put(USER_NOT_EXISTS,"用户不存在");

        MAP.put(OPERATOR_IS_NOT_YOURSELF,"操作者不是本人，不能操作");
        MAP.put(ROLE_NAME_NOT_NULL,"角色名称不能为空");
        MAP.put(ROLE_HAVE_USER,"角色包含用户");
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
