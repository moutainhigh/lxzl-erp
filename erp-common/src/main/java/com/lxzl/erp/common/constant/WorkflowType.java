package com.lxzl.erp.common.constant;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-06 8:52
 */
public class WorkflowType {
    /**
     * 采购单审批
     */
    public static final Integer WORKFLOW_TYPE_PURCHASE = 1;
    /**
     * 订单信息审批
     */
    public static final Integer WORKFLOW_TYPE_ORDER_INFO = 2;
    /**
     * 调配单审批
     */
    public static final Integer WORKFLOW_TYPE_DEPLOYMENT_ORDER_INFO = 3;
    /**
     * 换货单审批
     */
    public static final Integer WORKFLOW_TYPE_CHANGE = 4;
    /**
     * 退货单审批
     */
    public static final Integer WORKFLOW_TYPE_RETURN = 5;

    /**
     * 维修单审批
     */
    public static final Integer WORKFLOW_TYPE_REPAIR = 6;

    /**
     * 采购申请单审批
     */
    public static final Integer WORKFLOW_TYPE_PURCHASE_APPLY_ORDER = 8;

    /**
     * 转移单入审批
     */
    public static final Integer WORKFLOW_TYPE_TRANSFER_IN_ORDER = 9;

    /**
     * 转移单出审批
     */
    public static final Integer WORKFLOW_TYPE_TRANSFER_OUT_ORDER = 10;

    /**
     * 同行调拨单入审批
     */
    public static final Integer WORKFLOW_TYPE_PEER_DEPLOYMENT_INTO = 11;

    /**
     * 同行调拨单出审批
     */
    public static final Integer WORKFLOW_TYPE_PEER_DEPLOYMENT_OUT = 12;
    /**
     * 结算单冲正
     */
    public static final Integer WORKFLOW_TYPE_STATEMENT_ORDER_CORRECT = 13;
    /**
     * K3换货单审批
     */
    public static final Integer WORKFLOW_TYPE_K3_CHANGE = 14;
    /**
     * K3退货单审批
     */
    public static final Integer WORKFLOW_TYPE_K3_RETURN = 15;

    /**
     * 客户审批流程
     */
    public static final Integer WORKFLOW_TYPE_CUSTOMER = 16;

    /**
     * 客户地址审批流程
     */
    public static final Integer WORKFLOW_TYPE_CUSTOMER_CONSIGN = 17;

    /**
     * 渠道客户审批流程
     */
    public static final Integer WORKFLOW_TYPE_CHANNEL_CUSTOMER = 18;

    /**
     * 续租审批流程
     */
    public static final Integer WORKFLOW_TYPE_RELET_ORDER_INFO = 19;

    /**
     * 商城订单审批流程
     */
    public static final Integer WORKFLOW_TYPE_MALL_ORDER = 20;

    /**
     * 商城换货单审批流程
     */
    public static final Integer WORKFLOW_TYPE_MALL_CHANGE_ORDER = 21;

    /**
     * 商城退货单审批流程
     */
    public static final Integer WORKFLOW_TYPE_MALL_RETURN_ORDER = 22;

    /**
     * 商城维修单审批流程
     */
    public static final Integer WORKFLOW_TYPE_MALL_REPAIR_ORDER = 23;

    /**
     * 商城续租单审批流程
     */
    public static final Integer WORKFLOW_TYPE_MALL_RELET_ORDER = 24;

    /**
     * 商城客户审批流程
     */
    public static final Integer WORKFLOW_TYPE_MALL_CUSTOMER = 25;

    /**
     * 商城客户收货地址审批流程
     */
    public static final Integer WORKFLOW_TYPE_MALL_CUSTOMER_CONSIGN = 26;

    public static String getWorkflowTypeDesc(Integer workflowType) {
        if (WORKFLOW_TYPE_PURCHASE.equals(workflowType)) {
            return "采购订单";
        } else if (WORKFLOW_TYPE_ORDER_INFO.equals(workflowType)) {
            return "订单";
        } else if (WORKFLOW_TYPE_DEPLOYMENT_ORDER_INFO.equals(workflowType)) {
            return "调拨单";
        } else if (WORKFLOW_TYPE_CHANGE.equals(workflowType)) {
            return "换货单";
        } else if (WORKFLOW_TYPE_RETURN.equals(workflowType)) {
            return "退货单";
        } else if (WORKFLOW_TYPE_REPAIR.equals(workflowType)) {
            return "维修单";
        } else if (WORKFLOW_TYPE_PURCHASE_APPLY_ORDER.equals(workflowType)) {
            return "采购申请单";
        } else if (WORKFLOW_TYPE_TRANSFER_IN_ORDER.equals(workflowType)) {
            return "其他入库单";
        } else if (WORKFLOW_TYPE_TRANSFER_OUT_ORDER.equals(workflowType)) {
            return "其他出库单";
        } else if (WORKFLOW_TYPE_PEER_DEPLOYMENT_INTO.equals(workflowType)) {
            return "同行调入单";
        } else if (WORKFLOW_TYPE_PEER_DEPLOYMENT_OUT.equals(workflowType)) {
            return "同行调出单";
        } else if (WORKFLOW_TYPE_STATEMENT_ORDER_CORRECT.equals(workflowType)) {
            return "结算冲正单";
        } else if (WORKFLOW_TYPE_K3_CHANGE.equals(workflowType)) {
            return "K3换货单";
        } else if (WORKFLOW_TYPE_K3_RETURN.equals(workflowType)) {
            return "K3退货单";
        } else if (WORKFLOW_TYPE_CUSTOMER.equals(workflowType)) {
            return "客户";
        } else if (WORKFLOW_TYPE_CUSTOMER_CONSIGN.equals(workflowType)) {
            return "客户地址";
        } else if (WORKFLOW_TYPE_RELET_ORDER_INFO.equals(workflowType)) {
            return "续租";
        } else if (WORKFLOW_TYPE_MALL_ORDER.equals(workflowType)) {
            return "商城订单";
        } else if (WORKFLOW_TYPE_MALL_CHANGE_ORDER.equals(workflowType)) {
            return "商城换货单";
        } else if (WORKFLOW_TYPE_MALL_RETURN_ORDER.equals(workflowType)) {
            return "商城退货单";
        } else if (WORKFLOW_TYPE_MALL_REPAIR_ORDER.equals(workflowType)) {
            return "商城维修单";
        } else if (WORKFLOW_TYPE_MALL_RELET_ORDER.equals(workflowType)) {
            return "商城续租单";
        } else if (WORKFLOW_TYPE_MALL_CUSTOMER.equals(workflowType)) {
            return "商城客户";
        } else if (WORKFLOW_TYPE_MALL_CUSTOMER_CONSIGN.equals(workflowType)) {
            return "商城收货地址";
        } else {
            return "其他";
        }
    }
}
