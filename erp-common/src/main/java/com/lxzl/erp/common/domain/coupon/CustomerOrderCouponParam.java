package com.lxzl.erp.common.domain.coupon;

import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.order.pojo.Order;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\4\26 0026 9:58
 */
public class CustomerOrderCouponParam {
    private Customer customer;
    private Order order;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
