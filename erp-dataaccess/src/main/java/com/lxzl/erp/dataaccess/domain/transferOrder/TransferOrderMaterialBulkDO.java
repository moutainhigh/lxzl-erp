package com.lxzl.erp.dataaccess.domain.transferOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;


public class TransferOrderMaterialBulkDO  extends BaseDO {

	private Integer id;
	private Integer transferOrderId;

	private Integer transferOrderMaterialId;
	private String bulkMaterialNo;
	private Integer dataStatus;
	private String remark;

	@Transient
	private String bulkMaterialName;
	@Transient
	private String brandName;
	@Transient
	private Integer bulkMaterialType;
	@Transient
	private String materialNo;
	@Transient
	private Integer currentWarehouseId;
	@Transient
	private String currentWarehouseName;
	@Transient
	private Integer ownerWarehouseId;
	@Transient
	private String ownerWarehouseName;
	@Transient
	private Double materialCapacityValue;
	@Transient
	private BigDecimal bulkMaterialPrice;
	@Transient
	private String currentEquipmentNo;
	@Transient
	private Integer bulkMaterialStatus;


	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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