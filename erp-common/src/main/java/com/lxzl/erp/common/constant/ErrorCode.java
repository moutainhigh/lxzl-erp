package com.lxzl.erp.common.constant;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {
    private static Map<String,String> MAP = new HashMap<String,String>();
    /**
     * 返回码
     */
    public static final String SUCCESS = "J000000";

    public static final String USER_NOT_LOGIN = "J000995";
    public static final String CUSTOM_ERROR = "J000996";
    public static final String BUSINESS_EXCEPTION = "J000997";
    public static final String SYSTEM_EXCEPTION = "J000998";
    public static final String SYSTEM_ERROR = "J000999";
    public static final String RECORD_NOT_EXISTS = "J009999";

    public static final String USER_NAME_NOT_NULL = "J100000";
    public static final String USER_PASSWORD_NOT_NULL = "J100001";
    public static final String USER_DISABLE = "J100002";
    public static final String USER_NOT_ACTIVATED = "J100003";
    public static final String USER_NAME_NOT_FOUND = "J100004";
    public static final String USER_PASSWORD_ERROR = "J100005";
    public static final String USER_EXISTS = "J100006";
    public static final String USER_NOT_EXISTS = "J100007";
    public static final String USER_ROLE_NOT_NULL = "J100008";
    public static final String ROLE_NOT_NULL = "J100009";
    public static final String ROLE_HAVE_USER = "J100010";
    public static final String ROLE_NAME_NOT_NULL = "J100011";
    public static final String ROLE_ID_NOT_NULL = "J100013";
    public static final String DEPARTMENT_ID_NOT_NULL = "J100014";
    public static final String DEPARTMENT_NOT_NULL = "J100015";
    public static final String USER_NOT_NULL = "J100016";
    public static final String ACTIVE_USER_ID_NOT_NULL = "J100017";
    public static final String SUB_COMPANY_NAME_NOT_NULL = "J100018";
    public static final String USER_DEPARTMENT_NOT_NULL = "J100019";

    public static final String PRODUCT_ID_NOT_NULL = "J200000";
    public static final String PRODUCT_NAME_NOT_NULL = "J200001";
    public static final String PRODUCT_PROPERTY_NOT_NULL = "J200002";
    public static final String PRODUCT_SKU_NOT_NULL = "J200003";
    public static final String PRODUCT_SKU_PROPERTY_NOT_NULL = "J200004";
    public static final String PRODUCT_PROPERTY_VALUE_NOT_NULL = "J200005";
    public static final String PRODUCT_SKU_PROPERTY_VALUE_NOT_NULL = "J200006";
    public static final String PRODUCT_IMAGE_UPLOAD_ERROR = "J200007";
    public static final String PRODUCT_IS_NULL_OR_NOT_EXISTS = "J200008";
    public static final String PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS = "J200009";
    public static final String PRODUCT_SKU_PRICE_ERROR = "J200010";
    public static final String PRODUCT_SKU_COUNT_ERROR = "J200011";

    public static final String REMARK_PATTERN = "J900001";
    public static final String ID_NOT_NULL = "J900002";
    public static final String MONEY_MORE_THAN_ZERO = "J900003";
    public static final String IMAGE_NOT_EXISTS = "J900004";
    public static final String IMAGE_TYPE_ERROR = "J900005";
    public static final String IMAGE_USED = "J900006";
    public static final String DATA_DICTIONARY_NAME_NOT_NULL = "J900007";
    public static final String PARAM_IS_NOT_NULL = "J900008";
    public static final String HAVE_NO_CHANGE_STATUS = "J900009";
    public static final String STATUS_PATTERN = "J900010";
    public static final String COUNT_MORE_THAN_ZERO = "J900011";
    public static final String DAYS_MORE_THAN_ZERO = "J900012";
    public static final String DATA_STATUS_ERROR = "J900013";
    public static final String VERIFY_STATUS_ERROR = "J900014";
    public static final String OPERATOR_IS_NOT_YOURSELF = "J900015";
    public static final String PARAM_IS_NOT_ENOUGH = "J900016";
    public static final String NO_DUPLICATE_COMMIT = "J900017";

    public static final String PURCHASE_ORDER_ID_NOT_NULL = "J300000";
    public static final String PRODUCT_SUPPLIER_ID_NOT_NULL = "J300001";
    public static final String WARE_HOUSE_ID_NOT_NULL = "J300002";
    public static final String IS_INVOICE_NOT_NULL = "J300003";
    public static final String IS_NEW_NOT_NULL = "J300004";
    public static final String PURCHASE_ORDER_PRODUCT_LIST_NOT_NULL = "J300005";
    public static final String IS_INVOICE_VALUE_ERROR = "J300006";
    public static final String IS_NEW_VALUE_ERROR = "J300007";

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
        MAP.put(ROLE_ID_NOT_NULL,"角色ID不能为空");
        MAP.put(DEPARTMENT_ID_NOT_NULL,"部门ID不能为空");
        MAP.put(DEPARTMENT_NOT_NULL,"部门不能为空");
        MAP.put(USER_NOT_NULL,"用户不能为空");
        MAP.put(ACTIVE_USER_ID_NOT_NULL,"观察者用户ID不能为空");
        MAP.put(OPERATOR_IS_NOT_YOURSELF,"操作者不是本人，不能操作");
        MAP.put(ROLE_NAME_NOT_NULL,"角色名称不能为空");
        MAP.put(ROLE_HAVE_USER,"角色包含用户");
        MAP.put(SUB_COMPANY_NAME_NOT_NULL,"子公司名称不能为空");
        MAP.put(USER_DEPARTMENT_NOT_NULL,"用户部门不能为空");

        MAP.put(PRODUCT_ID_NOT_NULL,"商品ID不能为空");
        MAP.put(PRODUCT_NAME_NOT_NULL,"商品名称不能为空");
        MAP.put(PRODUCT_PROPERTY_NOT_NULL,"商品属性不能为空");
        MAP.put(PRODUCT_SKU_NOT_NULL,"商品SKU不能为空");
        MAP.put(PRODUCT_SKU_PROPERTY_NOT_NULL,"商品SKU属性不能为空");
        MAP.put(PRODUCT_PROPERTY_VALUE_NOT_NULL,"商品属性值不能为空");
        MAP.put(PRODUCT_SKU_PROPERTY_VALUE_NOT_NULL,"商品SKU属性值不能为空");
        MAP.put(PRODUCT_IMAGE_UPLOAD_ERROR,"商品图片上传失败");
        MAP.put(PRODUCT_IS_NULL_OR_NOT_EXISTS,"商品不存在或发生变更");
        MAP.put(PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS,"商品SKU不存在或发生变更");
        MAP.put(PRODUCT_SKU_PRICE_ERROR,"商品SKU价格填写不正确");
        MAP.put(PRODUCT_SKU_COUNT_ERROR,"商品SKU数量填写不正确");

        MAP.put(REMARK_PATTERN,"备注信息超过限制，最多输入200个字符");
        MAP.put(ID_NOT_NULL,"ID不能为空");
        MAP.put(MONEY_MORE_THAN_ZERO,"金额不能小于0");
        MAP.put(IMAGE_NOT_EXISTS,"图片不存在");
        MAP.put(IMAGE_TYPE_ERROR,"图片类型错误");
        MAP.put(IMAGE_USED,"图片已使用");
        MAP.put(DATA_DICTIONARY_NAME_NOT_NULL,"系统字典名称不能为空");
        MAP.put(PARAM_IS_NOT_NULL,"参数不能为空");
        MAP.put(HAVE_NO_CHANGE_STATUS,"无需改变状态");
        MAP.put(STATUS_PATTERN,"未知状态");
        MAP.put(COUNT_MORE_THAN_ZERO,"数量必须大于0");
        MAP.put(DAYS_MORE_THAN_ZERO,"天数不能小于0");
        MAP.put(DATA_STATUS_ERROR,"数据状态异常");
        MAP.put(VERIFY_STATUS_ERROR,"审核状态异常");
        MAP.put(OPERATOR_IS_NOT_YOURSELF,"操作者不是本人，不能操作");
        MAP.put(PARAM_IS_NOT_ENOUGH,"信息不全，请仔细检查");
        MAP.put(NO_DUPLICATE_COMMIT,"禁止重复提交");

        MAP.put(PURCHASE_ORDER_ID_NOT_NULL,"采购订单号不能为空");
        MAP.put(PRODUCT_SUPPLIER_ID_NOT_NULL,"商品供应商ID不能为空");
        MAP.put(WARE_HOUSE_ID_NOT_NULL,"仓库ID不能为空");
        MAP.put(IS_INVOICE_NOT_NULL,"是否有发票字段必填");
        MAP.put(IS_NEW_NOT_NULL,"是否是全新机字段必填");
        MAP.put(PURCHASE_ORDER_PRODUCT_LIST_NOT_NULL,"采购订单商品项列表不能为空");
        MAP.put(IS_INVOICE_VALUE_ERROR,"是否有发票参数错误");
        MAP.put(IS_NEW_VALUE_ERROR,"是否是全新机参数错误");
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
