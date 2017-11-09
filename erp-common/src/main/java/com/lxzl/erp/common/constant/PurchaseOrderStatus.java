package com.lxzl.erp.common.constant;

/**
 * 采购单状态
 */
public class PurchaseOrderStatus {
    public static final Integer PURCHASE_ORDER_STATUS_WAIT_COMMIT = 0;          // 0-待提交
    public static final Integer PURCHASE_ORDER_STATUS_VERIFYING = 3;          // 3-审核中
    public static final Integer PURCHASE_ORDER_STATUS_PURCHASING = 6;          // 6-采购中
    public static final Integer PURCHASE_ORDER_STATUS_PART = 9;          // 9-部分采购
    public static final Integer PURCHASE_ORDER_STATUS_ALL = 12;          // 12-全部采购
    public static final Integer PURCHASE_ORDER_STATUS_END = 15;          // 15-结束采购
}
