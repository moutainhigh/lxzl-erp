package com.lxzl.erp.dataaccess.domain.reletorder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReletOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer reletOrderId;
	private String reletOrderNo;
	private Integer orderId;
	private String orderNo;
	private Integer orderMaterialId;
	private Integer materialId;
	private String materialName;
	private Integer materialCount;
	private BigDecimal materialUnitAmount;
	private BigDecimal materialAmount;
	private String materialSnapshot;
	private Integer paymentCycle;
	private Integer payMode;
	private Integer isNewMaterial;
	private Integer rentingMaterialCount;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

}