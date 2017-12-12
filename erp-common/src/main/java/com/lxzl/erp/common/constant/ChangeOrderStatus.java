package com.lxzl.erp.common.constant;

/**
 * 换货订单状态，0-待提交，4-审核中，8-待备货，12-备货中，16-已发货待取货，20-处理中，24-取消，28-已完成
 */
public class ChangeOrderStatus{
    public static final Integer CHANGE_ORDER_STATUS_WAIT_COMMIT =0;//待提交
    public static final Integer CHANGE_ORDER_STATUS_VERIFYING =4;//审核中
    public static final Integer CHANGE_ORDER_STATUS_WAIT_STOCK =8;//待备货
    public static final Integer CHANGE_ORDER_STATUS_STOCKING =12;//备货中
    public static final Integer CHANGE_ORDER_STATUS_PICK_UP =16;//已发货待取货
    public static final Integer CHANGE_ORDER_STATUS_PROCESS =20;//处理中
    public static final Integer CHANGE_ORDER_STATUS_CANCEL =24;//取消
    public static final Integer CHANGE_ORDER_STATUS_END =28;//已完成

}
