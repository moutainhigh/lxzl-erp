package com.lxzl.erp.common.constant;

public class CanReletOrderStatus {
    public static final Integer CAN_RELET_ORDER_STATUS_NO = 0;                                  // 不可续租
    public static final Integer CAN_RELET_ORDER_STATUS_YES = 1;                                 // 可以续租
    public static final Integer CAN_RELET_ORDER_STATUS_EXIST_WAIT_HANDLE = 2;                   // 存在待处理的续租单
    public static final Integer CAN_RELET_ORDER_STATUS_EXIST_SUCCESS_RELET_NOT_BEGIN = 3;       // 存在未开始的续租单
}
