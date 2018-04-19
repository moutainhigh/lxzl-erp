package com.lxzl.erp.dataaccess.domain.coupon;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class CouponBatchDO  extends BaseDO {

	private Integer id;
	private String couponBatchName;
	private String couponBatchDescribe;
	private Integer couponType;
	private Date effectiveStartTime;
	private Date effectiveEndTime;
	private Integer couponBatchTotalCount;
	private Integer couponBatchUsedCount;
	private BigDecimal totalFaceAmount;
	private BigDecimal totalUsedAmount;
	private BigDecimal totalDeductionAmount;
	private String remark;
	private Integer dataStatus;
	private Integer couponBatchCancelCount;//优惠券作废总数

	public Integer getCouponBatchCancelCount() {
		return couponBatchCancelCount;
	}

	public void setCouponBatchCancelCount(Integer couponBatchCancelCount) {
		this.couponBatchCancelCount = couponBatchCancelCount;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getCouponBatchName(){
		return couponBatchName;
	}

	public void setCouponBatchName(String couponBatchName){
		this.couponBatchName = couponBatchName;
	}

	public String getCouponBatchDescribe(){
		return couponBatchDescribe;
	}

	public void setCouponBatchDescribe(String couponBatchDescribe){
		this.couponBatchDescribe = couponBatchDescribe;
	}

	public Integer getCouponType(){
		return couponType;
	}

	public void setCouponType(Integer couponType){
		this.couponType = couponType;
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

}