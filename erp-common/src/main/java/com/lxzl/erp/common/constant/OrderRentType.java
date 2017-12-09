package com.lxzl.erp.common.constant;

public class OrderRentType {
    public static final Integer RENT_TYPE_TIME = 1;
    public static final Integer RENT_TYPE_DAY = 2;
    public static final Integer RENT_TYPE_MONTH = 3;

    public static boolean inThisScope(Integer rentType) {
        if (rentType != null
                && (RENT_TYPE_DAY.equals(rentType)
                || RENT_TYPE_MONTH.equals(rentType))) {
            return true;
        }
        return false;
    }
}
