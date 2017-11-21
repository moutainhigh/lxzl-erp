package com.lxzl.erp.common.domain.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderProductEquipment implements Serializable {

	private Integer orderProductEquipmentId;   //唯一标识
	private Integer orderId;   //订单ID
	private Integer orderProductId;   //订单项ID
	private Integer equipmentId;   //设备ID
	private String equipmentNo;   //设备编号唯一
	private Date expectReturnTime;   //预计归还时间
	private Date actualReturnTime;   //实际归还时间
	private BigDecimal expectRentAmount;   //预计租金
	private BigDecimal actualRentAmount;   //实际租金
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getOrderProductEquipmentId(){
		return orderProductEquipmentId;
	}

	public void setOrderProductEquipmentId(Integer orderProductEquipmentId){
		this.orderProductEquipmentId = orderProductEquipmentId;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public Integer getOrderProductId(){
		return orderProductId;
	}

	public void setOrderProductId(Integer orderProductId){
		this.orderProductId = orderProductId;
	}

	public Integer getEquipmentId(){
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId){
		this.equipmentId = equipmentId;
	}

	public String getEquipmentNo(){
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo){
		this.equipmentNo = equipmentNo;
	}

	public Date getExpectReturnTime(){
		return expectReturnTime;
	}

	public void setExpectReturnTime(Date expectReturnTime){
		this.expectReturnTime = expectReturnTime;
	}

	public Date getActualReturnTime(){
		return actualReturnTime;
	}

	public void setActualReturnTime(Date actualReturnTime){
		this.actualReturnTime = actualReturnTime;
	}

	public BigDecimal getExpectRentAmount(){
		return expectRentAmount;
	}

	public void setExpectRentAmount(BigDecimal expectRentAmount){
		this.expectRentAmount = expectRentAmount;
	}

	public BigDecimal getActualRentAmount(){
		return actualRentAmount;
	}

	public void setActualRentAmount(BigDecimal actualRentAmount){
		this.actualRentAmount = actualRentAmount;
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