package com.lxzl.erp.common.domain.coupon;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatch;
import com.lxzl.erp.common.domain.customer.pojo.Customer;

import java.util.List;
import java.util.Map;

/**
 * @Author: Sunzhipeng
 * @Description:发放优惠券的po类
 * @Date: Created in 2018\4\8 0008 15:24
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponProvideParam {
    private Integer couponBatchDetailId;
    private Map<String,Integer> provideMap;

    public Integer getCouponBatchDetailId() {
        return couponBatchDetailId;
    }

    public void setCouponBatchDetailId(Integer couponBatchDetailId) {
        this.couponBatchDetailId = couponBatchDetailId;
    }

    public Map<String, Integer> getProvideMap() {
        return provideMap;
    }

    public void setProvideMap(Map<String, Integer> provideMap) {
        this.provideMap = provideMap;
    }
}
