package com.lxzl.erp.dataaccess.domain.order;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class OrderMaterialBulkDO  extends BaseDO {

	private Integer id;
	private Integer orderId;
	private Integer orderMaterialId;
	private Integer bulkMaterialId;
	private String bulkMaterialNo;
	private Date rentStartTime;
	private Date expectReturnTime;
	private Date actualReturnTime;
	private BigDecimal materialBulkUnitAmount;
	private BigDecimal expectRentAmount;
	private BigDecimal actualRentAmount;
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

	public Date getRentStartTime() {
		return rentStartTime;
	}

	public void setRentStartTime(Date rentStartTime) {
		this.rentStartTime = rentStartTime;
	}

	public BigDecimal getMaterialBulkUnitAmount() {
		return materialBulkUnitAmount;
	}

	public void setMaterialBulkUnitAmount(BigDecimal materialBulkUnitAmount) {
		this.materialBulkUnitAmount = materialBulkUnitAmount;
	}
}