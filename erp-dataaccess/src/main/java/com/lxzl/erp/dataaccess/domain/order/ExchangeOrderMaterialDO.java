package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.math.BigDecimal;

public class ExchangeOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer exchangeOrderId;
	private Integer orderProductId;
	private Integer materialId;
	private String materialName;
	private BigDecimal materialUnitAmount;
	private BigDecimal oldMaterialUnitAmount;
	private Integer isNewMaterial;
	private Integer dataStatus;
	private Integer depositCycle;
	private Integer paymentCycle;
	private Integer payMode;

	public Integer getDepositCycle() {
		return depositCycle;
	}

	public void setDepositCycle(Integer depositCycle) {
		this.depositCycle = depositCycle;
	}

	public Integer getPaymentCycle() {
		return paymentCycle;
	}

	public void setPaymentCycle(Integer paymentCycle) {
		this.paymentCycle = paymentCycle;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getExchangeOrderId(){
		return exchangeOrderId;
	}

	public void setExchangeOrderId(Integer exchangeOrderId){
		this.exchangeOrderId = exchangeOrderId;
	}

	public Integer getOrderProductId(){
		return orderProductId;
	}

	public void setOrderProductId(Integer orderProductId){
		this.orderProductId = orderProductId;
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

	public BigDecimal getMaterialUnitAmount(){
		return materialUnitAmount;
	}

	public void setMaterialUnitAmount(BigDecimal materialUnitAmount){
		this.materialUnitAmount = materialUnitAmount;
	}

	public BigDecimal getOldMaterialUnitAmount(){
		return oldMaterialUnitAmount;
	}

	public void setOldMaterialUnitAmount(BigDecimal oldMaterialUnitAmount){
		this.oldMaterialUnitAmount = oldMaterialUnitAmount;
	}

	public Integer getIsNewMaterial(){
		return isNewMaterial;
	}

	public void setIsNewMaterial(Integer isNewMaterial){
		this.isNewMaterial = isNewMaterial;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

}