package com.lxzl.erp.common.constant;

public class RepairOrderStatus {
    public static final Integer REPAIR_ORDER_STATUS_INIT = 0;          // 发起维修
    public static final Integer REPAIR_ORDER_STATUS_VERIFYING = 4;     // 审批中
    public static final Integer REPAIR_ORDER_STATUS_WAIT_REPAIR = 8;     // 待维修
    public static final Integer REPAIR_ORDER_STATUS_REPAIRING = 12;     // 维修中
    public static final Integer REPAIR_ORDER_STATUS_REPAIRED = 16;      // 维修完成
    public static final Integer REPAIR_ORDER_STATUS_CANCEL = 20;      // 取消维修
}
