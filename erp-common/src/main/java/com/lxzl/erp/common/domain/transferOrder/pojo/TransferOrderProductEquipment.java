package com.lxzl.erp.common.domain.transferOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.common.domain.validGroup.TransferOrder.DumpTransferOrderMaterialOutGroup;
import com.lxzl.erp.common.domain.validGroup.TransferOrder.TramsferOrderMaterialOutGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferOrderProductEquipment extends BasePO {

	private Integer transferOrderProductEquipmentId;   //唯一标识
	private Integer transferOrderId;   //转移单ID
	private Integer transferOrderProductId;   //转移单商品项ID
	private String productEquipmentNo;   //商品设备唯一编号
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间N
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人

	private Integer productId; //商品ID
	private String productName; //商品名称
	private Integer currentWarehouseId; //当前仓库ID
	private String currentWarehouseName; //当前仓库名称
	private Integer ownerWarehouseId; //所属仓库ID
	private String ownerWarehouseName; //所处仓库名称
	private Integer skuId; //SKUID
	private String skuName; //SKU名称
	private Integer equipmentStatus; //设备状态


	public Integer getTransferOrderProductEquipmentId(){
		return transferOrderProductEquipmentId;
	}

	public void setTransferOrderProductEquipmentId(Integer transferOrderProductEquipmentId){
		this.transferOrderProductEquipmentId = transferOrderProductEquipmentId;
	}

	public Integer getTransferOrderId(){
		return transferOrderId;
	}

	public void setTransferOrderId(Integer transferOrderId){
		this.transferOrderId = transferOrderId;
	}

	public Integer getTransferOrderProductId(){
		return transferOrderProductId;
	}

	public void setTransferOrderProductId(Integer transferOrderProductId){
		this.transferOrderProductId = transferOrderProductId;
	}

	public String getProductEquipmentNo(){
		return productEquipmentNo;
	}

	public void setProductEquipmentNo(String productEquipmentNo){
		this.productEquipmentNo = productEquipmentNo;
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}
}