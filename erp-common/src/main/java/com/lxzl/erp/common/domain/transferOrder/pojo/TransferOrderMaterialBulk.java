package com.lxzl.erp.common.domain.transferOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.math.BigDecimal;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferOrderMaterialBulk extends BasePO {

	private Integer transferOrderMaterialBulkId;   //唯一标识
	private Integer transferOrderId;   //转移单ID
	private Integer transferOrderMaterialId;   //转移单ID
	private String bulkMaterialNo;   //散料唯一编号
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private String bulkMaterialName;
	private String brandName;
	private Integer bulkMaterialType;
	private String materialNo;
	private Integer currentWarehouseId;
	private String currentWarehouseName;
	private Integer ownerWarehouseId;
	private String ownerWarehouseName;
	private Double materialCapacityValue;
	private BigDecimal bulkMaterialPrice;
	private String currentEquipmentNo;
	private Integer bulkMaterialStatus;

	public Integer getTransferOrderMaterialBulkId(){
		return transferOrderMaterialBulkId;
	}

	public void setTransferOrderMaterialBulkId(Integer transferOrderMaterialBulkId){
		this.transferOrderMaterialBulkId = transferOrderMaterialBulkId;
	}

	public Integer getTransferOrderId(){
		return transferOrderId;
	}

	public void setTransferOrderId(Integer transferOrderId){
		this.transferOrderId = transferOrderId;
	}

	public Integer getTransferOrderMaterialId(){
		return transferOrderMaterialId;
	}

	public void setTransferOrderMaterialId(Integer transferOrderMaterialId){
		this.transferOrderMaterialId = transferOrderMaterialId;
	}

	public String getBulkMaterialNo(){
		return bulkMaterialNo;
	}

	public void setBulkMaterialNo(String bulkMaterialNo){
		this.bulkMaterialNo = bulkMaterialNo;
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

	public String getBulkMaterialName() {
		return bulkMaterialName;
	}

	public void setBulkMaterialName(String bulkMaterialName) {
		this.bulkMaterialName = bulkMaterialName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getBulkMaterialType() {
		return bulkMaterialType;
	}

	public void setBulkMaterialType(Integer bulkMaterialType) {
		this.bulkMaterialType = bulkMaterialType;
	}

	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

	public Integer getCurrentWarehouseId() {
		return currentWarehouseId;
	}

	public void setCurrentWarehouseId(Integer currentWarehouseId) {
		this.currentWarehouseId = currentWarehouseId;
	}

	public String getCurrentWarehouseName() {
		return currentWarehouseName;
	}

	public void setCurrentWarehouseName(String currentWarehouseName) {
		this.currentWarehouseName = currentWarehouseName;
	}

	public Integer getOwnerWarehouseId() {
		return ownerWarehouseId;
	}

	public void setOwnerWarehouseId(Integer ownerWarehouseId) {
		this.ownerWarehouseId = ownerWarehouseId;
	}

	public String getOwnerWarehouseName() {
		return ownerWarehouseName;
	}

	public void setOwnerWarehouseName(String ownerWarehouseName) {
		this.ownerWarehouseName = ownerWarehouseName;
	}

	public Double getMaterialCapacityValue() {
		return materialCapacityValue;
	}

	public void setMaterialCapacityValue(Double materialCapacityValue) {
		this.materialCapacityValue = materialCapacityValue;
	}

	public BigDecimal getBulkMaterialPrice() {
		return bulkMaterialPrice;
	}

	public void setBulkMaterialPrice(BigDecimal bulkMaterialPrice) {
		this.bulkMaterialPrice = bulkMaterialPrice;
	}

	public String getCurrentEquipmentNo() {
		return currentEquipmentNo;
	}

	public void setCurrentEquipmentNo(String currentEquipmentNo) {
		this.currentEquipmentNo = currentEquipmentNo;
	}

	public Integer getBulkMaterialStatus() {
		return bulkMaterialStatus;
	}

	public void setBulkMaterialStatus(Integer bulkMaterialStatus) {
		this.bulkMaterialStatus = bulkMaterialStatus;
	}
}