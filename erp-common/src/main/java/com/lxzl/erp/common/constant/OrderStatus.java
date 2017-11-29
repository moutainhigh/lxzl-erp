package com.lxzl.erp.common.constant;

public class OrderStatus {
    public static final Integer ORDER_STATUS_WAIT_COMMIT = 0;      // 待提交
    public static final Integer ORDER_STATUS_VERIFYING = 4;      // 审核中
    public static final Integer ORDER_STATUS_WAIT_DELIVERY = 8;      // 待发货
    public static final Integer ORDER_STATUS_PROCESSING = 12;      // 处理中
    public static final Integer ORDER_STATUS_DELIVERED = 16; // 已发货
    public static final Integer ORDER_STATUS_CONFIRM = 20;   // 确认收货
    public static final Integer ORDER_STATUS_RETURN_BACK = 24;   // 全部归还
    public static final Integer ORDER_STATUS_CANCEL = 28;        // 取消
    public static final Integer ORDER_STATUS_END = 32;        // 结束
}
