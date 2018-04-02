package com.lxzl.erp.common.domain.coupon.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.IdGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coupon extends BasePO {

	@NotNull(message = ErrorCode.ID_NOT_NULL,groups = {IdGroup.class})
	private Integer couponId;   //唯一标识
	private Integer couponBatchId;   //批次ID
	private Integer erpBatchDetailId;   //批次详情ID
	private BigDecimal faceValue;   //优惠券面值
	private BigDecimal deductionAmount;   //抵扣金额
	private Integer couponStatus;   //优惠券状态，0-未领取，4-可用，8-已用
	private String customerNo;   //客戶编号
	private Integer isOnline;   //是否线上，0-否，1-是
	private Date receiveTime;   //领取时间
	private Date useTime;   //使用时间
	private Date effectiveStartTime;   //有效期起始时间
	private Date effectiveEndTime;   //有效期结束时间
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Integer dataStatus;   //状态：0不可用；1可用；2删除


	public Integer getCouponId(){
		return couponId;
	}

	public void setCouponId(Integer couponId){
		this.couponId = couponId;
	}

	public Integer getCouponBatchId(){
		return couponBatchId;
	}

	public void setCouponBatchId(Integer couponBatchId){
		this.couponBatchId = couponBatchId;
	}

	public Integer getErpBatchDetailId(){
		return erpBatchDetailId;
	}

	public void setErpBatchDetailId(Integer erpBatchDetailId){
		this.erpBatchDetailId = erpBatchDetailId;
	}

	public BigDecimal getFaceValue(){
		return faceValue;
	}

	public void setFaceValue(BigDecimal faceValue){
		this.faceValue = faceValue;
	}

	public BigDecimal getDeductionAmount(){
		return deductionAmount;
	}

	public void setDeductionAmount(BigDecimal deductionAmount){
		this.deductionAmount = deductionAmount;
	}

	public Integer getCouponStatus(){
		return couponStatus;
	}

	public void setCouponStatus(Integer couponStatus){
		this.couponStatus = couponStatus;
	}

	public String getCustomerNo(){
		return customerNo;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo = customerNo;
	}

	public Integer getIsOnline(){
		return isOnline;
	}

	public void setIsOnline(Integer isOnline){
		this.isOnline = isOnline;
	}

	public Date getReceiveTime(){
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime){
		this.receiveTime = receiveTime;
	}

	public Date getUseTime(){
		return useTime;
	}

	public void setUseTime(Date useTime){
		this.useTime = useTime;
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

}