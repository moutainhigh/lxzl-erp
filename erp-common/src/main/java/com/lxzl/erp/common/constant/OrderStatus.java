package com.lxzl.erp.common.constant;

public class OrderStatus {
    public static final Integer ORDER_STATUS_WAIT_COMMIT = 0;      // 待提交
    public static final Integer ORDER_STATUS_VERIFYING = 1;      // 审核中
    public static final Integer ORDER_STATUS_WAIT_DELIVERY = 2;      // 待发货
    public static final Integer ORDER_STATUS_DELIVERED = 3; // 已发货
    public static final Integer ORDER_STATUS_CONFIRM = 4;   // 确认收货
    public static final Integer ORDER_STATUS_OVERDUE = 5;   // 逾期
    public static final Integer ORDER_STATUS_RETURN_BACK = 6;   // 归还
    public static final Integer ORDER_STATUS_CANCEL = 7;        // 取消
}
