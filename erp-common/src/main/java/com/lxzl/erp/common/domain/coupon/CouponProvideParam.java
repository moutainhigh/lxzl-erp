package com.lxzl.erp.common.domain.coupon;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.domain.customer.pojo.Customer;

import java.util.List;

/**
 * @Author: Sunzhipeng
 * @Description:发放优惠券的po类
 * @Date: Created in 2018\4\8 0008 15:24
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponProvideParam {
    private Integer couponBatchDetailId;
    private Integer couponProvideAmount;
    private List<Customer> customerList;

    public Integer getCouponBatchDetailId() {
        return couponBatchDetailId;
    }

    public void setCouponBatchDetailId(Integer couponBatchDetailId) {
        this.couponBatchDetailId = couponBatchDetailId;
    }

    public Integer getCouponProvideAmount() {
        return couponProvideAmount;
    }

    public void setCouponProvideAmount(Integer couponProvideAmount) {
        this.couponProvideAmount = couponProvideAmount;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
