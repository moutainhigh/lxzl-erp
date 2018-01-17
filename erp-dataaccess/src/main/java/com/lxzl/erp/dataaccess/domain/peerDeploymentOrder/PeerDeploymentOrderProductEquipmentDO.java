package com.lxzl.erp.dataaccess.domain.peerDeploymentOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;


public class PeerDeploymentOrderProductEquipmentDO  extends BaseDO {

	private Integer id;
	private Integer peerDeploymentOrderProductId;
	private Integer peerDeploymentOrderId;
	private String peerDeploymentOrderNo;
	private Integer equipmentId;
	private String equipmentNo;
	private Date returnTime;
	private Integer dataStatus;
	private String remark;

	@Transient
	private Integer productId; //商品ID
	@Transient
	private String productName; //商品名称
	@Transient
	private Integer currentWarehouseId; //当前仓库ID
	@Transient
	private String currentWarehouseName; //当前仓库名称
	@Transient
	private Integer ownerWarehouseId; //所属仓库ID
	@Transient
	private String ownerWarehouseName; //所处仓库名称
	@Transient
	private Integer skuId; //SKUID
	@Transient
	private String skuName; //SKU名称
	@Transient
	private Integer equipmentStatus; //设备状态


	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getPeerDeploymentOrderProductId(){
		return peerDeploymentOrderProductId;
	}

	public void setPeerDeploymentOrderProductId(Integer peerDeploymentOrderProductId){ this.peerDeploymentOrderProductId = peerDeploymentOrderProductId; }

	public Integer getPeerDeploymentOrderId(){
		return peerDeploymentOrderId;
	}

	public void setPeerDeploymentOrderId(Integer peerDeploymentOrderId){ this.peerDeploymentOrderId = peerDeploymentOrderId; }

	public String getPeerDeploymentOrderNo(){
		return peerDeploymentOrderNo;
	}

	public void setPeerDeploymentOrderNo(String peerDeploymentOrderNo){ this.peerDeploymentOrderNo = peerDeploymentOrderNo; }

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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public Integer getEquipmentStatus() {
		return equipmentStatus;
	}

	public void setEquipmentStatus(Integer equipmentStatus) {
		this.equipmentStatus = equipmentStatus;
	}
}