package com.lxzl.erp.common.constant;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 15:20 2018/1/3
 * @Modified By:
 */
public class TransferOrderStatus {

    public static final Integer TRANSFER_ORDER_STATUS_INIT = 0 ;  //0初始化
    public static final Integer TRANSFER_ORDER_STATUS_VERIFYING = 4 ;  //4审批中
    public static final Integer TRANSFER_ORDER_STATUS_SUCCESS = 8 ;  //8转移成功
    public static final Integer TRANSFER_ORDER_STATUS_CANCEL = 16 ;  //16取消转移
    public static final Integer TRANSFER_ORDER_STATUS_END = 20 ;  //20结束转移

}
