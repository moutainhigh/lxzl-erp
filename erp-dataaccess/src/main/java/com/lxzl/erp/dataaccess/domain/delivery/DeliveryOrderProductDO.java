package com.lxzl.erp.dataaccess.domain.delivery;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class DeliveryOrderProductDO  extends BaseDO {

	private Integer id;
	private Integer deliveryOrderId;
	private Integer orderProductId;
	private Integer productId;
	private Integer productSkuId;
	private Integer deliveryProductSkuCount;
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

	public Integer getOrderProductId(){
		return orderProductId;
	}

	public void setOrderProductId(Integer orderProductId){
		this.orderProductId = orderProductId;
	}

	public Integer getProductId(){
		return productId;
	}

	public void setProductId(Integer productId){
		this.productId = productId;
	}

	public Integer getProductSkuId(){
		return productSkuId;
	}

	public void setProductSkuId(Integer productSkuId){
		this.productSkuId = productSkuId;
	}

	public Integer getDeliveryProductSkuCount(){
		return deliveryProductSkuCount;
	}

	public void setDeliveryProductSkuCount(Integer deliveryProductSkuCount){
		this.deliveryProductSkuCount = deliveryProductSkuCount;
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