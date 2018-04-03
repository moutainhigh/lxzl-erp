package com.lxzl.erp.common.domain.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponBatchQueryParam extends BasePageParam{
    private String couponBatchName;   //批次名称
    private Integer couponType;   //优惠券类型，1-设备优惠券
    private Date createStartTime;   //起始时间
    private Date createEndTime;   //结束时间

    public String getCouponBatchName() {
        return couponBatchName;
    }

    public void setCouponBatchName(String couponBatchName) {
        this.couponBatchName = couponBatchName;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

}
