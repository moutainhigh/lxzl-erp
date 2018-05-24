package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class OrderConfirmChangeLogDetailDO  extends BaseDO {

	private Integer id;
	private Integer orderId;
	private String orderNo;
	private Integer itemType;
	private Integer itemId;
	private Integer orderItemCount;
	private Integer oldItemCount;
	private Integer newItemCount;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public Integer getItemType(){
		return itemType;
	}

	public void setItemType(Integer itemType){
		this.itemType = itemType;
	}

	public Integer getItemId(){
		return itemId;
	}

	public void setItemId(Integer itemId){
		this.itemId = itemId;
	}

	public Integer getOrderItemCount(){
		return orderItemCount;
	}

	public void setOrderItemCount(Integer orderItemCount){
		this.orderItemCount = orderItemCount;
	}

	public Integer getOldItemCount(){
		return oldItemCount;
	}

	public void setOldItemCount(Integer oldItemCount){
		this.oldItemCount = oldItemCount;
	}

	public Integer getNewItemCount(){
		return newItemCount;
	}

	public void setNewItemCount(Integer newItemCount){
		this.newItemCount = newItemCount;
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