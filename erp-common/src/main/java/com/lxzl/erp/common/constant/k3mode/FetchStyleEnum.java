package com.lxzl.erp.common.constant.k3mode;

import com.lxzl.erp.common.constant.DeliveryMode;

public enum FetchStyleEnum {
    // 交货方式  FJH01 客户自提 FJH02 送货上门 FJH03 物流发货
    SELF_LIFTING("FJH01", DeliveryMode.DELIVERY_MODE_SINCE), DELIVERY_SERVICE("FJH02", DeliveryMode.DELIVERY_MODE_LX_EXPRESS), LOGISTICS_DELIVERY("FJH03", DeliveryMode.DELIVERY_MODE_EXPRESS);
    // 成员变量
    private String value;
    private Integer key;

    FetchStyleEnum(String value, Integer key) {
        this.value = value;
        this.key = key;
    }
    //根据key获取枚举
    public static FetchStyleEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (FetchStyleEnum temp : FetchStyleEnum.values()) {
            if (temp.getKey().equals(key)) {
                return temp;
            }
        }
        return null;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
