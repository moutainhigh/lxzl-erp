package com.lxzl.erp.common.domain.coupon;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\4\13 0013 11:59
 */
public class CustomerProvide {
    private String customerNo;
    private Integer provideCount;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public Integer getProvideCount() {
        return provideCount;
    }

    public void setProvideCount(Integer provideCount) {
        this.provideCount = provideCount;
    }
}
