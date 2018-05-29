package com.lxzl.erp.common.constant;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/5/24
 * @Time : Created in 11:06
 */
public class ChargeStatus {

    public static final Integer INITIALIZE = 0; //0初始化
    public static final Integer PAYING = 4; //4充值中
    public static final Integer PAY_SUCCESS = 8; //8已经充值
    public static final Integer PAY_FAIL = 16; //16充值失败
    public static final Integer PAY_OVERDUE = 20; //20充值失效
}
