package com.lxzl.erp.common.domain.peerDeploymentOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.math.BigDecimal;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerDeploymentOrderMaterialBulk extends BasePO {

	private Integer peerDeploymentOrderMaterialBulkId;   //唯一标识
	private Integer peerDeploymentOrderMaterialId;   //货物调拨配件项ID
	private Integer peerDeploymentOrderId;   //货物调拨单ID
	private String peerDeploymentOrderNo;   //货物调拨单编号
	private Integer bulkMaterialId;   //散料ID
	private String bulkMaterialNo;   //散料编号
	private Date returnTime;   //退还时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	private String bulkMaterialName; //散料名称
	private String brandName; //品牌
	private Integer bulkMaterialType; //散料类型
	private String materialNo; // 配件编号
	private Integer currentWarehouseId; //当前仓库ID
	private String currentWarehouseName;//当前仓库名称
	private Integer ownerWarehouseId; //所属仓库ID
	private String ownerWarehouseName; //所属仓库名称
	private Double materialCapacityValue; //面料大小
	private BigDecimal bulkMaterialPrice; //散料价值
	private String currentEquipmentNo; //当前设备编号
	private Integer bulkMaterialStatus; //散料状态


	public Integer getPeerDeploymentOrderMaterialBulkId(){
		return peerDeploymentOrderMaterialBulkId;
	}

	public void setPeerDeploymentOrderMaterialBulkId(Integer peerDeploymentOrderMaterialBulkId){ this.peerDeploymentOrderMaterialBulkId = peerDeploymentOrderMaterialBulkId; }

	public Integer getPeerDeploymentOrderMaterialId(){
		return peerDeploymentOrderMaterialId;
	}

	public void setPeerDeploymentOrderMaterialId(Integer peerDeploymentOrderMaterialId){ this.peerDeploymentOrderMaterialId = peerDeploymentOrderMaterialId; }

	public Integer getPeerDeploymentOrderId(){
		return peerDeploymentOrderId;
	}

	public void setPeerDeploymentOrderId(Integer peerDeploymentOrderId){ this.peerDeploymentOrderId = peerDeploymentOrderId; }

	public String getPeerDeploymentOrderNo(){
		return peerDeploymentOrderNo;
	}

	public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo){ this.peerDeploymentOrderNo = peerDeploymentOrderNo; }

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

	public Date getReturnTime(){
		return returnTime;
	}

	public void setReturnTime(Date returnTime){
		this.returnTime = returnTime;
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