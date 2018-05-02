package com.lxzl.erp.common.constant;

public class ReletOrderStatus {
    public static final Integer RELET_ORDER_STATUS_WAIT_COMMIT = 0;      // 待提交
    public static final Integer RELET_ORDER_STATUS_VERIFYING = 4;      // 审核中
    public static final Integer RELET_ORDER_STATUS_RELET = 8;      // 续租中
    public static final Integer RELET_ORDER_STATUS_PART_RETURN = 12;        // 部分退还
    public static final Integer RELET_ORDER_STATUS_RETURN_BACK = 16;   // 全部归还
    public static final Integer RELET_ORDER_STATUS_CANCEL = 20;        // 取消
    public static final Integer RELET_ORDER_STATUS_OVER = 24;        // 结束
}
