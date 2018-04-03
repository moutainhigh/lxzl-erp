package com.lxzl.erp.common.domain.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;

import javax.validation.Valid;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponDeleteParam{

    @Valid
    private List<Coupon> couponList;

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }

}
