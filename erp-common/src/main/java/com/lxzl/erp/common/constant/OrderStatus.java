package com.lxzl.erp.common.constant;

public class OrderStatus {
    public static final Integer ORDER_STATUS_INIT = 0;      // 初始化
    public static final Integer ORDER_STATUS_PAYING = 1;    // 支付中
    public static final Integer ORDER_STATUS_PAID = 2;      // 已支付
    public static final Integer ORDER_STATUS_DELIVERED = 3; // 已发货
    public static final Integer ORDER_STATUS_CONFIRM = 4;   // 确认收货
    public static final Integer ORDER_STATUS_OVERDUE = 5;   // 逾期
    public static final Integer ORDER_STATUS_RETURN_BACK = 6;   // 归还
    public static final Integer ORDER_STATUS_CANCEL = 7;        // 取消
}
