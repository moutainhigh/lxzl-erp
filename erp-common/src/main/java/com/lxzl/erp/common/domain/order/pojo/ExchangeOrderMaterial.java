package com.lxzl.erp.common.domain.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeOrderMaterial extends BasePO {

	private Integer exchangeOrderMaterialId;   //
	private Integer exchangeOrderId;   //变更单id
	private Integer orderProductId;   //订单商品项id
	private Integer materialId;   //配件ID
	private String materialName;   //配件名称
	private BigDecimal materialUnitAmount;   //配件单价
	private BigDecimal oldMaterialUnitAmount;   //原商品单价
	private Integer isNewMaterial;   //是否是全新机，1是0否
	private Integer dataStatus;   //状态：0不可用；1可用；2删除


	public Integer getExchangeOrderMaterialId(){
		return exchangeOrderMaterialId;
	}

	public void setExchangeOrderMaterialId(Integer exchangeOrderMaterialId){
		this.exchangeOrderMaterialId = exchangeOrderMaterialId;
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