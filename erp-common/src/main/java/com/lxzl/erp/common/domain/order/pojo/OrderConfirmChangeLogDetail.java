package com.lxzl.erp.common.domain.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderConfirmChangeLogDetail extends BasePO {

	private Integer orderConfirmChangeLogDetailId;   //唯一标识
	private Integer orderId;   //订单ID
	private String orderNo;   //订单编号
	private Integer itemType;   //类型：1-商品项，2-配件项
	private Integer itemId;   //商品项/配件项ID
	private Integer orderItemCount;   //订单初始商品/配件数
	private Integer oldItemCount;   //原商品/配件数
	private Integer newItemCount;   //新商品/配件数
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人


	public Integer getOrderConfirmChangeLogDetailId(){
		return orderConfirmChangeLogDetailId;
	}

	public void setOrderConfirmChangeLogDetailId(Integer orderConfirmChangeLogDetailId){
		this.orderConfirmChangeLogDetailId = orderConfirmChangeLogDetailId;
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

}