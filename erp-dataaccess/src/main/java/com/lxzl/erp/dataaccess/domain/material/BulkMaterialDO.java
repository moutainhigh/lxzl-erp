package com.lxzl.erp.dataaccess.domain.material;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class BulkMaterialDO  extends BaseDO {

	private Integer id;
	private String bulkMaterialNo;
	private Integer bulkMaterialType;
	private String bulkMaterialName;
	private Integer materialId;
	private String materialNo;
	private Integer warehouseId;
	private Integer warehousePositionId;
	private Integer ownerWarehouseId;
	private Integer ownerWarehousePositionId;
	private Integer brandId;
	private Integer categoryId;
	private Integer propertyId;
	private Integer propertyValueId;
	private BigDecimal bulkMaterialPrice;
	private BigDecimal originalPrice;
	private BigDecimal rentPrice;
	private Integer bulkMaterialStatus;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getBulkMaterialNo(){
		return bulkMaterialNo;
	}

	public void setBulkMaterialNo(String bulkMaterialNo){
		this.bulkMaterialNo = bulkMaterialNo;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
	}

	public Integer getWarehousePositionId(){
		return warehousePositionId;
	}

	public void setWarehousePositionId(Integer warehousePositionId){
		this.warehousePositionId = warehousePositionId;
	}

	public Integer getOwnerWarehouseId(){
		return ownerWarehouseId;
	}

	public void setOwnerWarehouseId(Integer ownerWarehouseId){
		this.ownerWarehouseId = ownerWarehouseId;
	}

	public Integer getOwnerWarehousePositionId(){
		return ownerWarehousePositionId;
	}

	public void setOwnerWarehousePositionId(Integer ownerWarehousePositionId){
		this.ownerWarehousePositionId = ownerWarehousePositionId;
	}

	public Integer getBrandId(){
		return brandId;
	}

	public void setBrandId(Integer brandId){
		this.brandId = brandId;
	}

	public Integer getCategoryId(){
		return categoryId;
	}

	public void setCategoryId(Integer categoryId){
		this.categoryId = categoryId;
	}

	public Integer getPropertyId(){
		return propertyId;
	}

	public void setPropertyId(Integer propertyId){
		this.propertyId = propertyId;
	}

	public Integer getPropertyValueId(){
		return propertyValueId;
	}

	public void setPropertyValueId(Integer propertyValueId){
		this.propertyValueId = propertyValueId;
	}

	public BigDecimal getBulkMaterialPrice(){
		return bulkMaterialPrice;
	}

	public void setBulkMaterialPrice(BigDecimal bulkMaterialPrice){
		this.bulkMaterialPrice = bulkMaterialPrice;
	}

	public BigDecimal getOriginalPrice(){
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice){
		this.originalPrice = originalPrice;
	}

	public BigDecimal getRentPrice(){
		return rentPrice;
	}

	public void setRentPrice(BigDecimal rentPrice){
		this.rentPrice = rentPrice;
	}

	public Integer getBulkMaterialStatus(){
		return bulkMaterialStatus;
	}

	public void setBulkMaterialStatus(Integer bulkMaterialStatus){
		this.bulkMaterialStatus = bulkMaterialStatus;
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

	public Integer getBulkMaterialType() {
		return bulkMaterialType;
	}

	public void setBulkMaterialType(Integer bulkMaterialType) {
		this.bulkMaterialType = bulkMaterialType;
	}

	public String getBulkMaterialName() {
		return bulkMaterialName;
	}

	public void setBulkMaterialName(String bulkMaterialName) {
		this.bulkMaterialName = bulkMaterialName;
	}
}