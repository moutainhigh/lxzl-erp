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
    public static final String ADMIN_REAL_NAME = "管理员";

    public static final Integer SUPER_MENU_ID = 200000;
    public static final Integer SUPER_DATA_DICTIONARY_ID = 300000;
    public static final Integer SUPER_DEPARTMENT_ID = 400000;
    public static final Integer SUPER_USER_ID = 500000;
    public static final Integer SUPER_ROLE_ID = 600000;
    public static final Integer SUPER_CUSTOMER_ID = 700000;
    public static final Integer SUPER_PRODUCT_CATEGORY_ID = 800000;
    public static final Integer HEAD_COMPANY_ID = 1;
    public static final Integer ELECTRIC_SALE_COMPANY_ID = 10;
    public static final Integer CHANNEL_CUSTOMER_COMPANY_ID = 11;
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

    public static final Integer RELET_TIME_OF_RENT_TYPE_MONTH = 30;  //按月租提前30天
    public static final Integer RELET_TIME_OF_RENT_TYPE_DAY = 15;   //按天租提前15天

    public static final Integer STATEMENT_ADVANCE_EXPECT_PAY_END_TIME = 7;//工作台结算单预计支付时间提前七天查询

    public static final Integer ORDER_TYPE_RELET = 6;//续租状态，对账单导出专用
    public static final Integer ORDER_TYPE_RELET_RETURN = 7;//续租退货状态，对账单导出专用

    public static final long WORKBENCH_REDIS_SAVE_TIME = 180L; //工作台redis缓存时间，三分钟

    public static final Integer K3_SEL_STOCK_WARE_TYPE_ONE = 1; //k3库存查询库位类型，1-分公司仓
    public static final Integer K3_SEL_STOCK_WARE_TYPE_TWO = 2; //k3库存查询库位类型，2-借出仓
    public static final Integer K3_SEL_STOCK_WARE_TYPE_THREE = 3; //k3库存查询库位类型，3-全部

}
