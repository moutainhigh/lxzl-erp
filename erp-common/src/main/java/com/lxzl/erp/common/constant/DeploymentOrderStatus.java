package com.lxzl.erp.common.constant;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-27 19:20
 */
public class DeploymentOrderStatus {
    public static final Integer DEPLOYMENT_ORDER_STATUS_WAIT_COMMIT = 0;      // 待提交
    public static final Integer DEPLOYMENT_ORDER_STATUS_VERIFYING = 1;      // 审核中
    public static final Integer DEPLOYMENT_ORDER_STATUS_PROCESSING = 3; // 处理中
    public static final Integer DEPLOYMENT_ORDER_STATUS_PROCESSED = 4; // 处理完毕
    public static final Integer DEPLOYMENT_ORDER_STATUS_CONFIRM = 5;   // 确认收货
}
