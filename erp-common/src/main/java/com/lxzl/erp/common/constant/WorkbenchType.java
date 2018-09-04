package com.lxzl.erp.common.constant;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 10:02 2018/7/20
 * @Modified By:
 */
public class WorkbenchType {

    public static final Integer ORDER_STATUS_VERIFYING = 1; //审核中订单
    public static final Integer ORDER_STATUS_CAN_RELET = 2; //可续租的订单
    public static final Integer ORDER_STATUS_OVER_DUE = 3; //到期未处理的订单
    public static final Integer ORDER_STATUS_WAIT_DELIVERY = 4; //代发货的订单
    public static final Integer ORDER_STATUS_NOT_PAY = 5; //未支付的订单
    public static final Integer RETURN_ORDER_STATUS_VERIFYING = 6; //审核中退货单
    public static final Integer RETURN_ORDER_STATUS_REJECT = 7; //被驳回的退货单
    public static final Integer RETURN_ORDER_STATUS_PROCESSING = 8; //处理中的退货单
    public static final Integer RETURN_ORDER_STATUS_WAIT_COMMIT = 9; //未提交的的退货单
    public static final Integer COMPANY_CUSTOMER_STATUS_VERIFYING = 10; //审核中的企业客户
    public static final Integer COMPANY_CUSTOMER_STATUS_REJECT = 11; //被驳回的企业客户
    public static final Integer PERSON_CUSTOMER_STATUS_VERIFYING = 12; //审核中的个人客户
    public static final Integer PERSON_CUSTOMER_STATUS_REJECT = 13; //被驳回的个人客户
    public static final Integer WORKFLOW_STATUS_VERIFYING = 14; //审核中的工作流
    public static final Integer WORKFLOW__STATUS_REJECT = 15; //被驳回的工作流
    public static final Integer STATEMENT_ORDER_STATUS_INIT = 16; //未结算
    public static final Integer STATEMENT_ORDER_STATUS_SETTLED_PART = 17; //部分结算
    public static final Integer WORK_FLOW_LINK_WAIT_VERIFY = 18 ; //待审核的工作流
    public static final Integer BANK_SLIP_DETAIL_WAIT_CLAIM = 19; //待认领的银行流水
    public static final Integer NOT_PRINT_LOG_ODER = 20; //未打印订单
    public static final Integer TODAY_AWAIT_DELIVERY_ODER = 21; //今日待发货的订单
    public static final Integer EXPRESS_AWAIT_DELIVERY_ODER = 22; //快递待发货的订单
    public static final Integer SLIP_AWAIT_DELIVERY_ODER = 23; //转单待发货的订单
    public static final Integer OVERDUE_UN_SHIPPED_DELIVERY_ODER = 24; //逾期未发货的订单
    public static final Integer UN_CONFIRMED_AWAIT_DELIVERY_ODER = 25; //未确认收货的订单


}