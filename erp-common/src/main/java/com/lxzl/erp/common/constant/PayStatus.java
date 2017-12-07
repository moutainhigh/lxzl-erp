package com.lxzl.erp.common.constant;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-29 14:26
 */
public class PayStatus {
    public static final Integer PAY_STATUS_INIT = 0;            // 初始化
    public static final Integer PAY_STATUS_PAID_PART = 4;       // 部分支付
    public static final Integer PAY_STATUS_PAID = 8;            // 已支付
    public static final Integer PAY_STATUS_REFUND = 16;         // 已退款
    public static final Integer PAY_STATUS_OVERDUE = 20;        // 逾期
}
