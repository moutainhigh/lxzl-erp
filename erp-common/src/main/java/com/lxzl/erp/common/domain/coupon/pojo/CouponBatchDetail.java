package com.lxzl.erp.common.domain.coupon.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.util.validate.constraints.In;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponBatchDetail extends BasePO {
	@NotNull(message = ErrorCode.ID_NOT_NULL,groups = {IdGroup.class})
	private Integer couponBatchDetailId;   //唯一标识
	@NotNull(message = ErrorCode.COUPON_BATCH_ID_NOT_NULL , groups = {AddGroup.class})
	private Integer couponBatchId;   //批次ID
	@NotNull(message = ErrorCode.COUPON_COUNT_NOT_NULL , groups = {AddGroup.class})
	@Min(message = ErrorCode.COUPON_COUNT_ERROR,value = 1, groups = {AddGroup.class})
	private Integer couponTotalCount;   //优惠券总数
	private Integer couponUsedCount;   //优惠券已使用总数
	private Integer couponReceivedCount;   //优惠券线上已领取总数
	@NotNull(message = ErrorCode.COUPON_FACE_AMOUNT_NOT_NULL , groups = {AddGroup.class})
	private BigDecimal faceValue;   //优惠券面值
	private BigDecimal totalFaceAmount;   //优惠券总面值
	private BigDecimal totalUsedAmount;   //已使用总面值
	private BigDecimal totalDeductionAmount;   //抵扣总金额
	@NotNull(message = ErrorCode.IS_ONLINE_NOT_NULL , groups = {AddGroup.class})
	@In(message = ErrorCode.IS_ONLINE_ERROR, value = {CommonConstant.NO,CommonConstant.YES},groups = {AddGroup.class})
	private Integer isOnline;   //是否线上，0-否，1-是
	private Date effectiveStartTime;   //有效期起始时间
	private Date effectiveEndTime;   //有效期结束时间
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Integer couponCancelCount;	//优惠券作废总数
	private Integer couponCanReceivedCount;//优惠券可领取总数

	public Integer getCouponCanReceivedCount() {
		return couponCanReceivedCount;
	}

	public void setCouponCanReceivedCount(Integer couponCanReceivedCount) {
		this.couponCanReceivedCount = couponCanReceivedCount;
	}

	public Integer getCouponBatchDetailId(){
		return couponBatchDetailId;
	}

	public void setCouponBatchDetailId(Integer couponBatchDetailId){
		this.couponBatchDetailId = couponBatchDetailId;
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

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
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