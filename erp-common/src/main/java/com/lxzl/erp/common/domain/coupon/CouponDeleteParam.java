package com.lxzl.erp.common.domain.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.coupon.pojo.CouponBatchDetail;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponDeleteParam extends BasePageParam{

    @Valid
    private List<Coupon> couponList;

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }
}
