package com.lxzl.erp.common.constant;

public class PeerDeploymentOrderStatus {

    public static final Integer PEER_DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT = 0;      // 未提交
    public static final Integer PEER_DEPLOYMENT_ORDER_STATUS_VERIFYING = 4;      // 审核中
    public static final Integer PEER_DEPLOYMENT_ORDER_STATUS_PROCESSING = 8; // 处理中
    public static final Integer PEER_DEPLOYMENT_ORDER_STATUS_CONFIRM = 12;   // 确认收货
    public static final Integer PEER_DEPLOYMENT_ORDER_STATUS_VERIFYING_OUT = 16;   // 退回审批中
    public static final Integer PEER_DEPLOYMENT_ORDER_STATUS_PROCESSING_OUT = 20;   // 退回处理中
    public static final Integer PEER_DEPLOYMENT_ORDER_STATUS_CONFIRM_OUT = 24;   // 已退回
    public static final Integer PEER_DEPLOYMENT_ORDER_STATUS_CANCEL = 28;   // 取消

}
