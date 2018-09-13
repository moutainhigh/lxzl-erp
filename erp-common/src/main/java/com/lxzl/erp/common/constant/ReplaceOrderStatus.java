package com.lxzl.erp.common.constant;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\9\10 0010 11:10
 */
public class ReplaceOrderStatus {
    public static final Integer REPLACE_ORDER_STATUS_WAIT_COMMIT = 0;      // 待提交
    public static final Integer REPLACE_ORDER_STATUS_VERIFYING = 4;      // 审核中
    public static final Integer REPLACE_ORDER_STATUS_WAIT_DELIVERY = 8;      // 待发货
    public static final Integer REPLACE_ORDER_STATUS_PROCESSING = 12;      // 处理中
    public static final Integer REPLACE_ORDER_STATUS_DELIVERED = 16; // 已发货
    public static final Integer REPLACE_ORDER_STATUS_CONFIRM = 20;   // 已完成（确认换货）
    public static final Integer REPLACE_ORDER_STATUS_CANCEL = 24;        // 取消

}
