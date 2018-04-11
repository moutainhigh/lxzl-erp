package com.lxzl.erp.common.domain.coupon;

import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.order.pojo.Order;

import java.util.List;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\4\10 0010 19:38
 */
public class UseCoupon {
    private Order order;
    private List<Coupon> couponList;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }
}
