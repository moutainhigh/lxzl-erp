package com.lxzl.erp.common.domain.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.math.BigDecimal;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponBatchDetailQueryParam extends BasePageParam{

    private Integer couponBatchDetailId;   //唯一标识
    private Integer couponBatchId;   //批次ID
    private BigDecimal faceValue;   //优惠券面值
    private Integer isOnline;   //是否线上，0-否，1-是
    private Date startTime;   //创建起始时间
    private Date endTime;   //创建结束时间


    public Integer getCouponBatchDetailId() {
        return couponBatchDetailId;
    }

    public void setCouponBatchDetailId(Integer couponBatchDetailId) {
        this.couponBatchDetailId = couponBatchDetailId;
    }

    public Integer getCouponBatchId() {
        return couponBatchId;
    }

    public void setCouponBatchId(Integer couponBatchId) {
        this.couponBatchId = couponBatchId;
    }

    public BigDecimal getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
