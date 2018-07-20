package com.lxzl.erp.common.constant;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 10:02 2018/7/20
 * @Modified By:
 */
public class WorkbenchType {

    public static final Integer WAIT_COMMIT = 1; //未提交
    public static final Integer VERIFYING = 2; //审核中
    public static final Integer REJECT = 3; //被驳回
    public static final Integer PROCESSING = 4; //处理中
    public static final Integer CAN_RELET = 5; //可续租
    public static final Integer OVER_DUE = 6; //到期未处理
    public static final Integer WAIT_DELIVERY = 7; //代发货
    public static final Integer NOT_PAY = 8; //未支付

}
