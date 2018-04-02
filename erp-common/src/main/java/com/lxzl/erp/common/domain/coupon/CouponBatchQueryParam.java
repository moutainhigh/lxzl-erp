package com.lxzl.erp.common.domain.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponBatchQueryParam extends BasePageParam{
    private Integer couponBatchId;   //唯一标识
    private String couponBatchName;   //批次名称
    private String couponBatchDescribe;   //批次描述
    private Integer couponType;   //优惠券类型，1-设备优惠券
    private Date effectiveStartTime;   //有效期起始时间
    private Date effectiveEndTime;   //有效期结束时间
    private Integer couponBatchTotalCount;   //优惠券总数
    private Integer couponBatchUsedCount;   //优惠券已使用总数
    private BigDecimal totalFaceAmount;   //优惠券总面值
    private BigDecimal totalUsedAmount;   //已使用总面值
    private BigDecimal totalDeductionAmount;   //抵扣总金额
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //修改时间
    private String updateUser;   //修改人
    private Integer dataStatus;   //状态：0不可用；1可用；2删除

    public Integer getCouponBatchId() {
        return couponBatchId;
    }

    public void setCouponBatchId(Integer couponBatchId) {
        this.couponBatchId = couponBatchId;
    }

    public String getCouponBatchName() {
        return couponBatchName;
    }

    public void setCouponBatchName(String couponBatchName) {
        this.couponBatchName = couponBatchName;
    }

    public String getCouponBatchDescribe() {
        return couponBatchDescribe;
    }

    public void setCouponBatchDescribe(String couponBatchDescribe) {
        this.couponBatchDescribe = couponBatchDescribe;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public Integer getCouponBatchTotalCount(){
        return couponBatchTotalCount;
    }

    public void setCouponBatchTotalCount(Integer couponBatchTotalCount){
        this.couponBatchTotalCount = couponBatchTotalCount;
    }

    public Integer getCouponBatchUsedCount(){
        return couponBatchUsedCount;
    }

    public void setCouponBatchUsedCount(Integer couponBatchUsedCount){
        this.couponBatchUsedCount = couponBatchUsedCount;
    }

    public void setTotalFaceAmount(BigDecimal totalFaceAmount) {
        this.totalFaceAmount = totalFaceAmount;
    }

    public BigDecimal getTotalUsedAmount() {
        return totalUsedAmount;
    }

    public void setTotalUsedAmount(BigDecimal totalUsedAmount) {
        this.totalUsedAmount = totalUsedAmount;
    }

    public BigDecimal getTotalDeductionAmount() {
        return totalDeductionAmount;
    }

    public void setTotalDeductionAmount(BigDecimal totalDeductionAmount) {
        this.totalDeductionAmount = totalDeductionAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getDataStatus(){
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus){
        this.dataStatus = dataStatus;
    }
}
