package com.lxzl.erp.common.domain.k3.pojo.returnOrder;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/6/21
 * @Time : Created in 20:50
 */
public class ReturnReasonType {
    public static final Integer NOT_REFUNDABLE = 1; //1-客户方设备不愿或无法退还
    public static final Integer EXPIRATION_OF_NORMAL = 2; //2-期满正常收回
    public static final Integer RETIRING_IN_ADVANCE = 3; //3-提前退租
    public static final Integer NO_PAYMENT_ON_TIME = 4; //4-未按时付款或风险等原因上门收回
    public static final Integer EQUIPMENT_FAILURE = 5; //5-设备故障等我方原因导致退货
    public static final Integer SUBJECTIVE_FACTORS = 6; //6-主观因素等客户方原因导致退货
    public static final Integer REPLACEMENT_EQUIPMENT = 7; //7-更换设备
    public static final Integer COMPANY_CLOSURES = 8; //8-公司倒闭
    public static final Integer IDLE_EQUIPMENT = 9; //9-设备
    public static final Integer FOLLOW_THE_RENT = 10; //10-满三个月或六个月随租随还
    public static final Integer OTHER = 11; //11-其它',


}
