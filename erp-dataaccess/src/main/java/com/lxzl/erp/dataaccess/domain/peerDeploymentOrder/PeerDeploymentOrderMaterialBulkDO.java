package com.lxzl.erp.dataaccess.domain.peerDeploymentOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;


public class PeerDeploymentOrderMaterialBulkDO  extends BaseDO {

	private Integer id;
	private Integer peerDeploymentOrderMaterialId;
	private Integer peerDeploymentOrderId;
	private String peerDeploymentOrderNo;
	private Integer bulkMaterialId;
	private String bulkMaterialNo;
	private Date returnTime;
	private Integer dataStatus;
	private String remark;

	@Transient
	private String bulkMaterialName; //散料名称
	@Transient
	private String brandName; //品牌
	@Transient
	private Integer bulkMaterialType; //散料类型
	@Transient
	private String materialNo; // 配件编号
	@Transient
	private Integer currentWarehouseId; //当前仓库ID
	@Transient
	private String currentWarehouseName;//当前仓库名称
	@Transient
	private Integer ownerWarehouseId; //所属仓库ID
	@Transient
	private String ownerWarehouseName; //所属仓库名称
	@Transient
	private Double materialCapacityValue; //面料大小
	@Transient
	private BigDecimal bulkMaterialPrice; //散料价值
	@Transient
	private String currentEquipmentNo; //当前设备编号
	@Transient
	private Integer bulkMaterialStatus; //散料状态

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

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