package com.lxzl.erp.common.domain.delivery.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryOrderProduct extends BasePO {

	private Integer deliveryOrderProductId;   //唯一标识
	private Integer deliveryOrderId;   //发货单ID
	private Integer orderProductId;   //订单项ID
	private Integer productId;   //商品ID
	private Integer productSkuId;   //发货SKU
	private Integer deliveryProductSkuCount;   //发货数量
	private Integer isNew;   //是否全新，1是，0否
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getDeliveryOrderProductId(){
		return deliveryOrderProductId;
	}

	public void setDeliveryOrderProductId(Integer deliveryOrderProductId){
		this.deliveryOrderProductId = deliveryOrderProductId;
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