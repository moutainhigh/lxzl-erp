package com.lxzl.erp.common.constant;

/**
 * 变更单状态
 */
public class ExchangeOrderStatus {
    public static final Integer ORDER_STATUS_WAIT_COMMIT = 0;    // 待提交
    public static final Integer ORDER_STATUS_VERIFYING = 2;    // 审核中
    public static final Integer ORDER_STATUS_CONFIRM = 4;    // 审核通过
    public static final Integer ORDER_STATUS_CANCEL = 8;     // 取消
    public static final Integer ORDER_STATUS_OK = 10;     // 变更成功
}
