package com.lxzl.erp.dataaccess.domain.warehouse;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;


public class StockOrderDO  extends BaseDO {

	private Integer id;
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

	private List<StockOrderEquipmentDO> stockOrderEquipmentDOList;
	private List<StockOrderBulkMaterialDO> stockOrderBulkMaterialDOList;

	@Transient
	private String srcWarehouseName;
	@Transient
	private String targetWarehouseName;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public List<StockOrderEquipmentDO> getStockOrderEquipmentDOList() {
		return stockOrderEquipmentDOList;
	}

	public void setStockOrderEquipmentDOList(List<StockOrderEquipmentDO> stockOrderEquipmentDOList) {
		this.stockOrderEquipmentDOList = stockOrderEquipmentDOList;
	}

	public List<StockOrderBulkMaterialDO> getStockOrderBulkMaterialDOList() {
		return stockOrderBulkMaterialDOList;
	}

	public void setStockOrderBulkMaterialDOList(List<StockOrderBulkMaterialDO> stockOrderBulkMaterialDOList) {
		this.stockOrderBulkMaterialDOList = stockOrderBulkMaterialDOList;
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