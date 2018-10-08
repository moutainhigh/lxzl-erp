package com.lxzl.erp.common.constant;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 17:23
 */
public class OrderType {
    public static final Integer ORDER_TYPE_ORDER = 1;
    public static final Integer ORDER_TYPE_DEPARTMENT = 2;
    public static final Integer ORDER_TYPE_CHANGE = 3;
    public static final Integer ORDER_TYPE_RETURN = 4;
    public static final Integer ORDER_TYPE_REPAIR = 5;
    /**
     * 换货单类型
     */
    public static final Integer ORDER_TYPE_REPLACE= 6;

    /** 订单类型---续租---该类型只是业务上的标志---与数据库中的类型没有直接联系--通过判断relet_order_item_refer_id */
    public static final Integer ORDER_TYPE_RELET = 100;
    /** 订单类型---续租退货---该类型只是业务上的标志---与数据库中的类型没有直接联系--通过判断relet_order_item_refer_id */
    public static final Integer ORDER_TYPE_RELET_RETURN = 101;

}
