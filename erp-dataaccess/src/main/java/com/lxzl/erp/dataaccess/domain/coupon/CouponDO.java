package com.lxzl.erp.dataaccess.domain.coupon;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class CouponDO  extends BaseDO {

	private Integer id;
	private Integer couponBatchId;
	private Integer couponBatchDetailId;
	private String couponCode;
	private BigDecimal faceValue;
	private BigDecimal deductionAmount;
	private Integer couponStatus;
	private String customerNo;
	private Integer isOnline;
	private Date receiveTime;
	private Date useTime;
	private Date effectiveStartTime;
	private Date effectiveEndTime;
	private String remark;
	private Integer dataStatus;
	private Integer orderId;	//订单ID
	private String orderNo;		//订单号
	private Integer orderProductId;		//订单商品项ID

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

	public Integer getCouponBatchDetailId(){
		return couponBatchDetailId;
	}

	public void setCouponBatchDetailId(Integer couponBatchDetailId){
		this.couponBatchDetailId = couponBatchDetailId;
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

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getOrderProductId() {
		return orderProductId;
	}

	public void setOrderProductId(Integer orderProductId) {
		this.orderProductId = orderProductId;
	}
}