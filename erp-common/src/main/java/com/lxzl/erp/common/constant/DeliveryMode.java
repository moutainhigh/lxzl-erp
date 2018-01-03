package com.lxzl.erp.common.constant;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-03 21:10
 */
public class DeliveryMode {

    public static final Integer DELIVERY_MODE_EXPRESS = 1;
    public static final Integer DELIVERY_MODE_SINCE = 2;

    public static boolean inThisScope(Integer deliveryMode) {
        if (deliveryMode != null
                && (DELIVERY_MODE_EXPRESS.equals(deliveryMode)
                || DELIVERY_MODE_SINCE.equals(deliveryMode))) {
            return true;
        }
        return false;
    }
}
