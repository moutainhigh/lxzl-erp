package com.lxzl.erp.common.constant;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 17:23 2018/7/12
 * @Modified By:
 */
public class OperationType {
    public static final Integer CREATE_ORDER = 1;   //创建订单
    public static final Integer UPDATE_ORDER = 2;   //修改订单
    public static final Integer COMMIT_ORDER = 3;	//提交审核
    public static final Integer VERIFY_ORDER_SUCCESS = 4;	//审核通过
    public static final Integer VERIFY_ORDER_FAILED = 5;	//审核未通过	
    public static final Integer K3_DELIVER_CALLBACK = 6;	//K3发货回调（系统）
    public static final Integer COMFIRM_ORDER = 7;			//确认收货
    public static final Integer K3_RETURN_CALLBACK = 8;		//k3退货回调（K3操作员或系统）
    public static final Integer CANCEL_ORDER = 9;			//取消订单
    public static final Integer FORCE_CANCEL_ORDER = 10;	//强制取消订单	
    public static final Integer STATEMENT_PAID = 11;		//结算支付

}
