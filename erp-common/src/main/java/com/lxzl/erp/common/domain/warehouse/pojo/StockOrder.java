package com.lxzl.erp.common.domain.warehouse.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class StockOrder implements Serializable {

	private Integer stockOrderId;
	private String stockOrderNo;
	private Integer operationType;
	private Integer causeType;
	private String referNo;
	private Integer orderStatus;
	private Integer srcWarehouseId;
	private Integer srcWarehousePositionId;
	private Integer targetWarehouseId;
	private Integer targetWarehousePositionId;
	private Integer owner;
	private Integer dataStatus;
	private String remark;
	private Date createTime;
	private String createUser;
	private Date updateTime;
	private String updateUser;

	private List<StockOrderEquipment> stockOrderEquipmentList;
	private List<StockOrderBulkMaterial> stockOrderBulkMaterialList;

	private String srcWarehouseName;
	private String targetWarehouseName;

	public Integer getStockOrderId(){
		return stockOrderId;
	}

	public void setStockOrderId(Integer stockOrderId){
		this.stockOrderId = stockOrderId;
	}

	public String getStockOrderNo(){
		return stockOrderNo;
	}

	public void setStockOrderNo(String stockOrderNo){
		this.stockOrderNo = stockOrderNo;
	}

	public Integer getOperationType(){
		return operationType;
	}

	public void setOperationType(Integer operationType){
		this.operationType = operationType;
	}

	public Integer getCauseType(){
		return causeType;
	}

	public void setCauseType(Integer causeType){
		this.causeType = causeType;
	}

	public String getReferNo(){
		return referNo;
	}

	public void setReferNo(String referNo){
		this.referNo = referNo;
	}

	public Integer getOrderStatus(){
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus){
		this.orderStatus = orderStatus;
	}

	public Integer getSrcWarehouseId(){
		return srcWarehouseId;
	}

	public void setSrcWarehouseId(Integer srcWarehouseId){
		this.srcWarehouseId = srcWarehouseId;
	}

	public Integer getSrcWarehousePositionId(){
		return srcWarehousePositionId;
	}

	public void setSrcWarehousePositionId(Integer srcWarehousePositionId){
		this.srcWarehousePositionId = srcWarehousePositionId;
	}

	public Integer getTargetWarehouseId(){
		return targetWarehouseId;
	}

	public void setTargetWarehouseId(Integer targetWarehouseId){
		this.targetWarehouseId = targetWarehouseId;
	}

	public Integer getTargetWarehousePositionId(){
		return targetWarehousePositionId;
	}

	public void setTargetWarehousePositionId(Integer targetWarehousePositionId){
		this.targetWarehousePositionId = targetWarehousePositionId;
	}

	public Integer getOwner(){
		return owner;
	}

	public void setOwner(Integer owner){
		this.owner = owner;
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

	public List<StockOrderEquipment> getStockOrderEquipmentList() {
		return stockOrderEquipmentList;
	}

	public void setStockOrderEquipmentList(List<StockOrderEquipment> stockOrderEquipmentList) {
		this.stockOrderEquipmentList = stockOrderEquipmentList;
	}

	public List<StockOrderBulkMaterial> getStockOrderBulkMaterialList() {
		return stockOrderBulkMaterialList;
	}

	public void setStockOrderBulkMaterialList(List<StockOrderBulkMaterial> stockOrderBulkMaterialList) {
		this.stockOrderBulkMaterialList = stockOrderBulkMaterialList;
	}

	public String getSrcWarehouseName() {
		return srcWarehouseName;
	}

	public void setSrcWarehouseName(String srcWarehouseName) {
		this.srcWarehouseName = srcWarehouseName;
	}

	public String getTargetWarehouseName() {
		return targetWarehouseName;
	}

	public void setTargetWarehouseName(String targetWarehouseName) {
		this.targetWarehouseName = targetWarehouseName;
	}
}