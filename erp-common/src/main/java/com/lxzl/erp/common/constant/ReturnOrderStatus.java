package com.lxzl.erp.common.constant;

/**
 * 归还订单状态，0-待提交，4-审核中，8-待取货，12-处理中，16-已取消，20-已完成
 */
public class ReturnOrderStatus {
    public static final Integer RETURN_ORDER_STATUS_WAIT_COMMIT = 0;
    public static final Integer RETURN_ORDER_STATUS_VERIFYING = 4;
    public static final Integer RETURN_ORDER_STATUS_WAIT_TAKEN = 8;
    public static final Integer RETURN_ORDER_STATUS_PROCESSING = 12;
    public static final Integer RETURN_ORDER_STATUS_CANCEL = 16;
    public static final Integer RETURN_ORDER_STATUS_END = 20;

}
