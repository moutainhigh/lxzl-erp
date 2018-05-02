package com.lxzl.erp.common.domain.reletorder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;

import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReletOrderMaterial extends BasePO {

	private Integer reletOrderMaterialId;   //唯一标识
	private Integer reletOrderId;   //续租订单ID
	private String reletOrderNo;   //续租订单编号
	private Integer orderId;   //订单ID
	private String orderNo;   //订单编号
	private Integer orderMaterialId;   //订单配件项ID
	private Integer materialId;   //配件ID
	private String materialName;   //配件名称
	private Integer materialCount;   //配件总数
	private BigDecimal materialUnitAmount;   //配件单价
	private BigDecimal materialAmount;   //配件价格
	private String materialSnapshot;   //配件冗余信息，防止商品修改留存快照
	private Integer paymentCycle;   //付款期数
	private Integer payMode;   //支付方式：1先用后付，2先付后用
	private Integer isNewMaterial;   //是否是全新机，1是0否
	private Integer rentingMaterialCount;   //在租配件总数
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	public ReletOrderMaterial(){

	}

	public ReletOrderMaterial(OrderMaterial orderMaterial, String reletOrderNo){
		this.orderNo = reletOrderNo;
		this.orderId = orderMaterial.getOrderId();
		this.orderMaterialId = orderMaterial.getOrderMaterialId();
		this.materialId = orderMaterial.getMaterialId();
		this.materialName = orderMaterial.getMaterialName();
		this.materialCount = orderMaterial.getMaterialCount();
		this.materialUnitAmount = orderMaterial.getMaterialUnitAmount();
		this.materialAmount = orderMaterial.getMaterialAmount();
//		this.materialSnapshot = orderMaterial.getMaterialSnapshot();
		this.paymentCycle = orderMaterial.getPaymentCycle();
		this.payMode = orderMaterial.getPayMode();
		this.isNewMaterial = orderMaterial.getIsNewMaterial();
		this.rentingMaterialCount = orderMaterial.getRentingMaterialCount();

	}


	public Integer getReletOrderMaterialId(){
		return reletOrderMaterialId;
	}

	public void setReletOrderMaterialId(Integer reletOrderMaterialId){
		this.reletOrderMaterialId = reletOrderMaterialId;
	}

	public Integer getReletOrderId(){
		return reletOrderId;
	}

	public void setReletOrderId(Integer reletOrderId){
		this.reletOrderId = reletOrderId;
	}

	public String getReletOrderNo(){
		return reletOrderNo;
	}

	public void setReletOrderNo(String reletOrderNo){
		this.reletOrderNo = reletOrderNo;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Integer getOrderMaterialId(){
		return orderMaterialId;
	}

	public void setOrderMaterialId(Integer orderMaterialId){
		this.orderMaterialId = orderMaterialId;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public String getMaterialName(){
		return materialName;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

	public Integer getMaterialCount(){
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount){
		this.materialCount = materialCount;
	}

	public BigDecimal getMaterialUnitAmount(){
		return materialUnitAmount;
	}

	public void setMaterialUnitAmount(BigDecimal materialUnitAmount){
		this.materialUnitAmount = materialUnitAmount;
	}

	public BigDecimal getMaterialAmount(){
		return materialAmount;
	}

	public void setMaterialAmount(BigDecimal materialAmount){
		this.materialAmount = materialAmount;
	}

	public String getMaterialSnapshot(){
		return materialSnapshot;
	}

	public void setMaterialSnapshot(String materialSnapshot){
		this.materialSnapshot = materialSnapshot;
	}

	public Integer getPaymentCycle(){
		return paymentCycle;
	}

	public void setPaymentCycle(Integer paymentCycle){
		this.paymentCycle = paymentCycle;
	}

	public Integer getPayMode(){
		return payMode;
	}

	public void setPayMode(Integer payMode){
		this.payMode = payMode;
	}

	public Integer getIsNewMaterial(){
		return isNewMaterial;
	}

	public void setIsNewMaterial(Integer isNewMaterial){
		this.isNewMaterial = isNewMaterial;
	}

	public Integer getRentingMaterialCount(){
		return rentingMaterialCount;
	}

	public void setRentingMaterialCount(Integer rentingMaterialCount){
		this.rentingMaterialCount = rentingMaterialCount;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
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

}