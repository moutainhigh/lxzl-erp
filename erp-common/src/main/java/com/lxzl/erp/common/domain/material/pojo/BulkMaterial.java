package com.lxzl.erp.common.domain.material.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkMaterial implements Serializable {

	private Integer bulkMaterialId;   //散料ID
	private String bulkMaterialNo;   //散料唯一编号
	private Integer bulkMaterialType;	// 散料类型，来自于物料类型
	private Integer isMainMaterial;		// 是否是四大件，1是0否
	private String bulkMaterialName;   //散料名称
	private Integer materialId;   //物料ID，从哪个物料生成的
	private String materialNo;   //物料编号，从哪个物料生成的
	private Integer currentWarehouseId;   //目前仓库ID
	private Integer currentWarehousePositionId;   //目前仓位ID
	private Integer ownerWarehouseId;   //归属仓库ID
	private Integer ownerWarehousePositionId;   //归属目前仓位ID
	private Integer brandId;   //所属品牌ID
	private Integer categoryId;   //所属类目ID
	private Integer propertyId;   //属性ID
	private Integer propertyValueId;   //属性值ID
	private BigDecimal bulkMaterialPrice;   //散料本身的价值(单价)
	private Integer currentEquipmentId;	// 当前设备ID
	private String currentEquipmentNo;	// 当前设备编号
	private Integer bulkMaterialStatus;   //散料状态，0闲置，1租赁中，2报废
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


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

	public Integer getCurrentWarehouseId() {
		return currentWarehouseId;
	}

	public void setCurrentWarehouseId(Integer currentWarehouseId) {
		this.currentWarehouseId = currentWarehouseId;
	}

	public Integer getCurrentWarehousePositionId() {
		return currentWarehousePositionId;
	}

	public void setCurrentWarehousePositionId(Integer currentWarehousePositionId) {
		this.currentWarehousePositionId = currentWarehousePositionId;
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

	public Integer getCurrentEquipmentId() {
		return currentEquipmentId;
	}

	public void setCurrentEquipmentId(Integer currentEquipmentId) {
		this.currentEquipmentId = currentEquipmentId;
	}

	public String getCurrentEquipmentNo() {
		return currentEquipmentNo;
	}

	public void setCurrentEquipmentNo(String currentEquipmentNo) {
		this.currentEquipmentNo = currentEquipmentNo;
	}

	public Integer getIsMainMaterial() {
		return isMainMaterial;
	}

	public void setIsMainMaterial(Integer isMainMaterial) {
		this.isMainMaterial = isMainMaterial;
	}
}