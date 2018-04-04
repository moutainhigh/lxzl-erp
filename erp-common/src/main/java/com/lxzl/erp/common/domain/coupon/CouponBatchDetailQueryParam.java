package com.lxzl.erp.common.domain.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.math.BigDecimal;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponBatchDetailQueryParam extends BasePageParam{

    private Integer couponBatchId;   //批次ID
    private BigDecimal faceValue;   //优惠券面值
    private Integer isOnline;   //是否线上，0-否，1-是
    private Date createStartTime;   //起始时间
    private Date createEndTime;   //结束时间


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
