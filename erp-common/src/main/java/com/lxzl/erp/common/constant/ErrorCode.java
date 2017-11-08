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
    public static final String DEPARTMENT_NOT_EXISTS = "J100019";

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

    public static final String WORKFLOW_TYPE_NOT_EXISTS = "J800001";
    public static final String WORKFLOW_TEMPLATE_HAVE_NO_NODE = "J800002";
    public static final String WORKFLOW_LINK_EXISTS = "J800003";
    public static final String WORKFLOW_LINK_NOT_EXISTS = "J800004";
    public static final String WORKFLOW_LINK_HAVE_NO_DETAIL = "J800005";
    public static final String WORKFLOW_LINK_VERIFY_ALREADY_OVER = "J800006";
    public static final String WORKFLOW_NOT_BELONG_TO_YOU = "J800007";
    public static final String WORKFLOW_NOT_EXISTS_CLOSED = "J800008";
    public static final String WORKFLOW_NODE_NOT_EXISTS = "J800009";
    public static final String WORKFLOW_VERIFY_USER_ERROR = "J800010";

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
    public static final String WAREHOUSE_ID_NOT_NULL = "J300002";
    public static final String IS_INVOICE_NOT_NULL = "J300003";
    public static final String IS_NEW_NOT_NULL = "J300004";
    public static final String PURCHASE_ORDER_PRODUCT_LIST_NOT_NULL = "J300005";
    public static final String IS_INVOICE_VALUE_ERROR = "J300006";
    public static final String IS_NEW_VALUE_ERROR = "J300007";
    public static final String PURCHASE_ORDER_NO_NOT_NULL = "J300008";
    public static final String PURCHASE_ORDER_NOT_EXISTS = "J300009";
    public static final String PURCHASE_ORDER_COMMITTED_CAN_NOT_UPDATE = "J300010";
    public static final String PURCHASE_ORDER_CANNOT_CREATE_BY_NEW_AND_AMOUNT = "J300011";
    public static final String WAREHOUSE_NOT_EXISTS = "J300012";
    public static final String PURCHASE_ORDER_PRODUCT_CAN_NOT_REPEAT = "J300013";
    public static final String PURCHASE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN = "J300014";
    public static final String WAREHOUSE_IN_STORAGE_LIST_NOT_NULL = "J300015";
    public static final String USER_CAN_NOT_OP_WAREHOUSE = "J300016";
    public static final String VERIFY_USER_NOT_NULL = "J300017";
    public static final String PURCHASE_ORDER_COMMITTED_CAN_NOT_DELETE = "J300018";
    public static final String PURCHASE_DELIVERY_ORDER_NO_NOT_NULL = "J300019";
    public static final String PURCHASE_DELIVERY_ORDER_NOT_EXISTS = "J300020";
    public static final String WAREHOUSE_NO_NOT_NULL = "J300021";
    public static final String SUPPLIER_NOT_EXISTS = "J300022";

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
        MAP.put(DEPARTMENT_NOT_EXISTS,"部门不存在");

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

        MAP.put(WORKFLOW_TYPE_NOT_EXISTS,"工作流类型不存在");
        MAP.put(WORKFLOW_TEMPLATE_HAVE_NO_NODE,"此工作流模板没有节点");
        MAP.put(WORKFLOW_LINK_EXISTS,"此单工作流已经存在");
        MAP.put(WORKFLOW_LINK_NOT_EXISTS,"此单工作流不存在");
        MAP.put(WORKFLOW_LINK_HAVE_NO_DETAIL,"此单工作流没有明细");
        MAP.put(WORKFLOW_LINK_VERIFY_ALREADY_OVER,"此单工作流已经结束");
        MAP.put(WORKFLOW_NOT_BELONG_TO_YOU,"此单工作流还不该您审核");
        MAP.put(WORKFLOW_NOT_EXISTS_CLOSED,"此工作流不存在或已经关闭");
        MAP.put(WORKFLOW_NODE_NOT_EXISTS,"此工作流节点不存在");
        MAP.put(WORKFLOW_VERIFY_USER_ERROR,"此工作流审核人员有误");

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

        MAP.put(PURCHASE_ORDER_ID_NOT_NULL,"采购订单ID不能为空");
        MAP.put(PRODUCT_SUPPLIER_ID_NOT_NULL,"商品供应商ID不能为空");
        MAP.put(WAREHOUSE_ID_NOT_NULL,"仓库ID不能为空");
        MAP.put(IS_INVOICE_NOT_NULL,"是否有发票字段必填");
        MAP.put(IS_NEW_NOT_NULL,"是否是全新机字段必填");
        MAP.put(PURCHASE_ORDER_PRODUCT_LIST_NOT_NULL,"采购订单商品项列表不能为空");
        MAP.put(IS_INVOICE_VALUE_ERROR,"是否有发票参数错误");
        MAP.put(IS_NEW_VALUE_ERROR,"是否是全新机参数错误");
        MAP.put(PURCHASE_ORDER_NO_NOT_NULL,"采购订单号不能为空");
        MAP.put(PURCHASE_ORDER_NOT_EXISTS,"采购订单不存在");
        MAP.put(PURCHASE_ORDER_COMMITTED_CAN_NOT_UPDATE,"待审核状态的采购单不允许修改");
        MAP.put(PURCHASE_ORDER_CANNOT_CREATE_BY_NEW_AND_AMOUNT,"拒绝创建该采购单，原因【分公司采购单不能为大于两万元的全新机】");
        MAP.put(WAREHOUSE_NOT_EXISTS,"仓库不存在");
        MAP.put(PURCHASE_ORDER_PRODUCT_CAN_NOT_REPEAT,"采购订单项重复");
        MAP.put(PURCHASE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN,"已提交的采购单不能再次提交");
        MAP.put(WAREHOUSE_IN_STORAGE_LIST_NOT_NULL,"商品入仓数据不能为空");
        MAP.put(USER_CAN_NOT_OP_WAREHOUSE,"您没有该仓库的操作权限");
        MAP.put(VERIFY_USER_NOT_NULL,"审核人不能为空");
        MAP.put(PURCHASE_ORDER_COMMITTED_CAN_NOT_DELETE,"已提交的采购单不能删除");
        MAP.put(PURCHASE_DELIVERY_ORDER_NO_NOT_NULL,"采购发货单编号不能为空");
        MAP.put(PURCHASE_DELIVERY_ORDER_NOT_EXISTS,"采购发货单不存在");
        MAP.put(WAREHOUSE_NO_NOT_NULL,"仓库编号不能为空");
        MAP.put(SUPPLIER_NOT_EXISTS,"供应商不存在");
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
