package com.lxzl.erp.common.domain.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponQueryParam extends BasePageParam{
    private Integer couponBatchId;   //批次ID
    private Integer couponBatchDetailId;   //批次详情ID
    private Integer couponStatus;   //优惠券状态，0-未领取，4-可用，8-已用
    private String customerNo;   //客戶编号
    private Integer isOnline;   //是否线上，0-否，1-是
    private Date createStartTime;   //起始时间
    private Date createEndTime;   //结束时间
    private Integer dataStatus;   //状态：0不可用；1可用；2删除


    public Integer getCouponBatchId() {
        return couponBatchId;
    }

    public void setCouponBatchId(Integer couponBatchId) {
        this.couponBatchId = couponBatchId;
    }

    public Integer getCouponBatchDetailId() {
        return couponBatchDetailId;
    }

    public void setCouponBatchDetailId(Integer couponBatchDetailId) {
        this.couponBatchDetailId = couponBatchDetailId;
    }

    public Integer getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Integer couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
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

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }
}
