package com.lxzl.erp.dataaccess.domain.delivery;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class DeliveryOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer deliveryOrderId;
	private Integer orderMaterialId;
	private Integer materialId;
	private Integer deliveryMaterialCount;
	private Integer isNew;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getDeliveryOrderId(){
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(Integer deliveryOrderId){
		this.deliveryOrderId = deliveryOrderId;
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

	public Integer getDeliveryMaterialCount(){
		return deliveryMaterialCount;
	}

	public void setDeliveryMaterialCount(Integer deliveryMaterialCount){
		this.deliveryMaterialCount = deliveryMaterialCount;
	}

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
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