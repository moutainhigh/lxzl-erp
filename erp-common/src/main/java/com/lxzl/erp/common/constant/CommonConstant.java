package com.lxzl.erp.common.constant;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/11/1.
 * Time: 14:48.
 */
public class CommonConstant {
    public static final String ERP_USER_SESSION_KEY = "user";
    public static final Double PROPORTION_MAX = 100.0;

    public static final Integer COMMON_CONSTANT_YES = 1;
    public static final Integer COMMON_CONSTANT_NO = 0;

    public static final int YES = 1;
    public static final int NO  = 0;

    public static final String UPLOAD_USER = "admin";

    public static final Integer SUPER_MENU_ID = 200000;
    public static final Integer SUPER_DATA_DICTIONARY_ID = 300000;
    public static final Integer SUPER_DEPARTMENT_ID = 400000;
    public static final Integer SUPER_USER_ID = 500000;
    public static final Integer SUPER_ROLE_ID = 600000;
    public static final Integer SUPER_CUSTOMER_ID = 700000;
    public static final Integer SUPER_PRODUCT_CATEGORY_ID = 800000;
    public static final Integer HEAD_COMPANY_ID = 1;
    public static final Integer ELECTRIC_SALE_COMPANY_ID = 10;
    public static final Integer HEADER_COMPANY_ID = 1;

    public static final String COMMUNITY_SESSION_KEY = "community_id";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String MD5_KEY = "kjhku8gsasiow34";

    // 数据状态
    public static final Integer DATA_STATUS_DISABLE = 0;//标记禁用
    public static final Integer DATA_STATUS_ENABLE = 1;//标记可用
    public static final Integer DATA_STATUS_DELETE = 2;//标记删除
    // 操作类型
    public static final Integer COMMON_DATA_OPERATION_TYPE_ADD = 1;      // 增加
    public static final Integer COMMON_DATA_OPERATION_TYPE_UPDATE = 2;   // 修改
    public static final Integer COMMON_DATA_OPERATION_TYPE_DELETE = 3;   // 删除

    public static final String COMMON_CONSTANT_SEPARATOR = ",";

    public static final String NORMAL_STRING = "正常";
    public static final Integer ORDER_RENT_TYPE_LONG_MIN = 6;

    public static final Integer ORDER_NEED_VERIFY_DAYS = 90;
    public static final Integer ORDER_NEED_VERIFY_MONTHS = 3;
    public static final Integer ORDER_NEED_VERIFY_PRODUCT_COUNT = 100;
    public static final BigDecimal ORDER_NEED_VERIFY_PRODUCT_AMOUNT = new BigDecimal(200000);

    public static final Integer COMMON_ZERO = 0;
    public static final Integer COMMON_ONE = 1;
    public static final Integer COMMON_TWO = 2;
    public static final Integer WORKFLOW_STEP_TWO = 2;

}
