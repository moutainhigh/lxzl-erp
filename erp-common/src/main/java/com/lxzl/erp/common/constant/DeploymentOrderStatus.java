package com.lxzl.erp.common.constant;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-27 19:20
 */
public class DeploymentOrderStatus {
    public static final Integer DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT = 0;      // 待提交
    public static final Integer DEPLOYMENT_ORDER_STATUS_VERIFYING = 4;      // 审核中
    public static final Integer DEPLOYMENT_ORDER_STATUS_PROCESSING = 8; // 处理中
    public static final Integer DEPLOYMENT_ORDER_STATUS_DELIVERED = 12; // 处理完毕
    public static final Integer DEPLOYMENT_ORDER_STATUS_CONFIRM = 16;   // 确认收货
    public static final Integer DEPLOYMENT_ORDER_STATUS_CANCEL = 20;   // 取消
}
