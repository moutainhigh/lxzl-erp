package com.lxzl.erp.common.domain.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderTimeAxis implements Serializable {

	private Integer orderTimeAxisId;   //唯一标识
	private Integer orderId;   //订单ID
	private Integer orderStatus;   //订单状态，0-待提交,4-审核中,8-待发货,12-处理中,16-已发货,20-确认收货,24-全部归还,28-取消,32-结束
	private Date generationTime;   //产生时间
	private String orderSnapshot;   //订单当前状态PO
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getOrderTimeAxisId(){
		return orderTimeAxisId;
	}

	public void setOrderTimeAxisId(Integer orderTimeAxisId){
		this.orderTimeAxisId = orderTimeAxisId;
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