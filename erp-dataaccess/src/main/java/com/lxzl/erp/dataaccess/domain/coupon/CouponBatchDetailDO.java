package com.lxzl.erp.dataaccess.domain.coupon;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class CouponBatchDetailDO  extends BaseDO {

	private Integer id;
	private Integer couponBatchId;
	private Integer couponTotalCount;
	private Integer couponUsedCount;
	private Integer couponReceivedCount;
	private BigDecimal faceValue;
	private BigDecimal totalFaceAmount;
	private BigDecimal totalUsedAmount;
	private BigDecimal totalDeductionAmount;
	private Integer isOnline;
	private Date effectiveStartTime;
	private Date effectiveEndTime;
	private String remark;
	private Integer dataStatus;
	private Integer couponCancelCount;	//优惠券作废总数
	private Integer couponCanReceivedCount;//优惠券可领取总数

	public Integer getCouponCanReceivedCount() {
		return couponCanReceivedCount;
	}

	public void setCouponCanReceivedCount(Integer couponCanReceivedCount) {
		this.couponCanReceivedCount = couponCanReceivedCount;
	}
	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getCouponBatchId(){
		return couponBatchId;
	}

	public void setCouponBatchId(Integer couponBatchId){
		this.couponBatchId = couponBatchId;
	}

	public Integer getCouponTotalCount(){
		return couponTotalCount;
	}

	public void setCouponTotalCount(Integer couponTotalCount){
		this.couponTotalCount = couponTotalCount;
	}

	public Integer getCouponUsedCount(){
		return couponUsedCount;
	}

	public void setCouponUsedCount(Integer couponUsedCount){
		this.couponUsedCount = couponUsedCount;
	}

	public Integer getCouponReceivedCount(){
		return couponReceivedCount;
	}

	public void setCouponReceivedCount(Integer couponReceivedCount){
		this.couponReceivedCount = couponReceivedCount;
	}

	public BigDecimal getFaceValue(){
		return faceValue;
	}

	public void setFaceValue(BigDecimal faceValue){
		this.faceValue = faceValue;
	}

	public BigDecimal getTotalFaceAmount(){
		return totalFaceAmount;
	}

	public void setTotalFaceAmount(BigDecimal totalFaceAmount){
		this.totalFaceAmount = totalFaceAmount;
	}

	public BigDecimal getTotalUsedAmount(){
		return totalUsedAmount;
	}

	public void setTotalUsedAmount(BigDecimal totalUsedAmount){
		this.totalUsedAmount = totalUsedAmount;
	}

	public BigDecimal getTotalDeductionAmount(){
		return totalDeductionAmount;
	}

	public void setTotalDeductionAmount(BigDecimal totalDeductionAmount){
		this.totalDeductionAmount = totalDeductionAmount;
	}

	public Integer getIsOnline(){
		return isOnline;
	}

	public void setIsOnline(Integer isOnline){
		this.isOnline = isOnline;
	}

	public Date getEffectiveStartTime(){
		return effectiveStartTime;
	}

	public void setEffectiveStartTime(Date effectiveStartTime){
		this.effectiveStartTime = effectiveStartTime;
	}

	public Date getEffectiveEndTime(){
		return effectiveEndTime;
	}

	public void setEffectiveEndTime(Date effectiveEndTime){
		this.effectiveEndTime = effectiveEndTime;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public Integer getCouponCancelCount() {
		return couponCancelCount;
	}

	public void setCouponCancelCount(Integer couponCancelCount) {
		this.couponCancelCount = couponCancelCount;
	}
}