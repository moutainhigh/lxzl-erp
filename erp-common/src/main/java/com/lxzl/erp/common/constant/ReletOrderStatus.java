package com.lxzl.erp.common.constant;

public class ReletOrderStatus {
    public static final Integer RELET_ORDER_STATUS_WAIT_COMMIT = 0;      // 待提交
    public static final Integer RELET_ORDER_STATUS_VERIFYING = 4;      // 审核中
    public static final Integer RELET_ORDER_STATUS_RELETTING = 8;      // 续租成功

//    public static final Integer RELET_ORDER_STATUS_CANCEL = 20;        // 取消
//    public static final Integer RELET_ORDER_STATUS_OVER = 24;        // 结束

    public static boolean canReletOrderByCurrentStatus(Integer currentStatus){
        if (ReletOrderStatus.RELET_ORDER_STATUS_RELETTING.equals(currentStatus)){
            return true;
        }
        return false;
    }
}
