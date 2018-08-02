package com.lxzl.erp.common.constant;

/**
 * 订单操作状态码
 */
public class OrderBusinessType {
    public static final Integer ORDER_OPERATION_WAIT_COMMIT = 10101;      // 待提交
    public static final Integer ORDER_OPERATION_VERIFYING = 10102;       // 审核中
    public static final Integer ORDER_OPERATION_WAIT_DELIVERY = 10103;   // 待发货
    public static final Integer ORDER_OPERATION_PROCESSING = 10104;     // 处理中
    public static final Integer ORDER_OPERATION_DELIVERED = 10105;      // 已发货
    public static final Integer ORDER_OPERATION_CONFIRM = 10106;        // 确认收货
    public static final Integer ORDER_OPERATION_PART_RETURN = 10107;    // 部分退还
    public static final Integer ORDER_OPERATION_RETURN_BACK = 10108;    // 全部归还
    public static final Integer ORDER_OPERATION_CANCEL = 10109;         // 取消
    public static final Integer ORDER_OPERATION_OVER = 10112;           // 结束
    public static final Integer ORDER_OPERATION_COLSE = 10113;          // 关闭
    public static final Integer ORDER_OPERATION_UPD_GOODS_PRICE = 10114;     // 修改商品、配件价格
    //#TODO  后续添加 订单备注，修改结算，订单重算，等操作记录
}
