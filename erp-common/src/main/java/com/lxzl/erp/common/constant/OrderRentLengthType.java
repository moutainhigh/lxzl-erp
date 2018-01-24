package com.lxzl.erp.common.constant;

public class OrderRentLengthType {
    public static final Integer RENT_LENGTH_TYPE_SHORT = 1;
    public static final Integer RENT_LENGTH_TYPE_LONG = 2;

    public static boolean inThisScope(Integer rentType) {
        if (rentType != null
                && (RENT_LENGTH_TYPE_SHORT.equals(rentType)
                || RENT_LENGTH_TYPE_LONG.equals(rentType))) {
            return true;
        }
        return false;
    }
}
