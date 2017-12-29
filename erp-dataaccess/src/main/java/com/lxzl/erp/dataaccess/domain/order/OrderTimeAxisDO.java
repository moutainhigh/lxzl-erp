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

}