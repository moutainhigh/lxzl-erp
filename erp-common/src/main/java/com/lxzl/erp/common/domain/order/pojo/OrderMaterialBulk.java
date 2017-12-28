package com.lxzl.erp.common.domain.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderMaterialBulk extends BasePO {

	private Integer orderMaterialBulkId;   //唯一标识
	private Integer orderId;   //订单ID
	private Integer orderMaterialId;   //订单物料项ID
	private Integer bulkMaterialId;   //设备ID
	private String bulkMaterialNo;   //设备编号唯一
	private Date expectReturnTime;   //预计归还时间
	private Date actualReturnTime;   //实际归还时间
	private BigDecimal materialBulkUnitAmount;	// 散料单价
	private BigDecimal expectRentAmount;   //预计租金
	private BigDecimal actualRentAmount;   //实际租金
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getOrderMaterialBulkId(){
		return orderMaterialBulkId;
	}

	public void setOrderMaterialBulkId(Integer orderMaterialBulkId){
		this.orderMaterialBulkId = orderMaterialBulkId;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public Integer getOrderMaterialId(){
		return orderMaterialId;
	}

	public void setOrderMaterialId(Integer orderMaterialId){
		this.orderMaterialId = orderMaterialId;
	}

	public Integer getBulkMaterialId(){
		return bulkMaterialId;
	}

	public void setBulkMaterialId(Integer bulkMaterialId){
		this.bulkMaterialId = bulkMaterialId;
	}

	public String getBulkMaterialNo(){
		return bulkMaterialNo;
	}

	public void setBulkMaterialNo(String bulkMaterialNo){
		this.bulkMaterialNo = bulkMaterialNo;
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

	public BigDecimal getMaterialBulkUnitAmount() {
		return materialBulkUnitAmount;
	}

	public void setMaterialBulkUnitAmount(BigDecimal materialBulkUnitAmount) {
		this.materialBulkUnitAmount = materialBulkUnitAmount;
	}
}