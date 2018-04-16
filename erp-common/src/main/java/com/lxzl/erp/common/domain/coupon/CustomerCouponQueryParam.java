package com.lxzl.erp.common.domain.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\4\16 0016 9:57
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCouponQueryParam extends BasePageParam{
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
