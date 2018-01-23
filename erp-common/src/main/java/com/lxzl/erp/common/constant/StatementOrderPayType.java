package com.lxzl.erp.common.constant;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-23 19:17
 */
public class StatementOrderPayType {
    public static final Integer PAY_TYPE_BALANCE = 1;
    public static final Integer PAY_TYPE_WEIXIN = 2;


    public static boolean inThisScope(Integer payType) {
        if (payType != null
                && (PAY_TYPE_BALANCE.equals(payType)
                || PAY_TYPE_WEIXIN.equals(payType))) {
            return true;
        }
        return false;
    }

}
