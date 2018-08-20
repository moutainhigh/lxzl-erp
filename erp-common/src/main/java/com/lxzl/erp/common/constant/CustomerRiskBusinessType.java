package com.lxzl.erp.common.constant;

/**
 * @Author: Sunzhipeng
 * @Description: 客户授信变更业务操作编码 1客户授信，2提交订单，3强制取消订单，4确认收货，5退货
 * @Date: Created in 2018\8\20 0020 10:31
 */
public class CustomerRiskBusinessType {
    public static final Integer CUSTOMER_RISK_TYPE = 1;//1客户授信
    public static final Integer COMMIT_ORDER_TYPE= 2;//2提交订单
    public static final Integer FORCE_CANCEL_ORDER_TYPE = 3;//3强制取消订单
    public static final Integer CONFIRM_CHANGE_ORDER_TYPE = 4;//4确认收货
    public static final Integer RETURN_ORDER_TYPE = 5;//5退货
}
