package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class OrderTimeAxisDO  extends BaseDO {

	private Integer id;
	private Integer orderId;
	private Integer orderStatus;
	private Date generationTime;
	private String orderSnapshot;
	private Integer dataStatus;
	private String remark;

	private Integer operationType;// 操作类型,1-创建订单，2-修改订单,3-订单提交审核,4-订单审核通过,5-订单审核拒绝,6-K3发货回调（系统）,7-订单确认收货,8-k3退货回调（K3操作员或系统）,9-取消订单,10-强制取消订单,11-结算支付;

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

	public Integer getOrderStatus(){
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus){
		this.orderStatus = orderStatus;
	}

	public Date getGenerationTime(){
		return generationTime;
	}

	public void setGenerationTime(Date generationTime){
		this.generationTime = generationTime;
	}

	public String getOrderSnapshot(){
		return orderSnapshot;
	}

	public void setOrderSnapshot(String orderSnapshot){
		this.orderSnapshot = orderSnapshot;
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

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}
}