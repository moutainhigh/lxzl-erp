package com.lxzl.erp.dataaccess.domain.repairOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.List;


public class RepairOrderDO  extends BaseDO {

	private Integer id;
	private String repairOrderNo;
	private String repairReason;
	private Integer repairOrderStatus;
	private Integer dataStatus;
	private String remark;

	private Integer repairEquipmentCount; //送修设备数量
	private Integer repairBulkMaterialCount; //送修物料数量
	private Integer fixEquipmentCount; //修复设备数量
	private Integer fixBulkMaterialCount; //修复物料数量
	private String warehouseNo;//当前仓库编号
	private String repairEndRemark; //维修完成的备注

	@Transient
	private String warehouseName;
	@Transient
	private List<RepairOrderEquipmentDO> repairOrderEquipmentDOList;
	@Transient
	private  List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList;


	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getRepairOrderNo(){
		return repairOrderNo;
	}

	public void setRepairOrderNo(String repairOrderNo){
		this.repairOrderNo = repairOrderNo;
	}

	public String getRepairReason(){
		return repairReason;
	}

	public void setRepairReason(String repairReason){
		this.repairReason = repairReason;
	}

	public Integer getRepairOrderStatus(){
		return repairOrderStatus;
	}

	public void setRepairOrderStatus(Integer repairOrderStatus){
		this.repairOrderStatus = repairOrderStatus;
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

	public Integer getRepairEquipmentCount() {
		return repairEquipmentCount;
	}

	public void setRepairEquipmentCount(Integer repairEquipmentCount) {
		this.repairEquipmentCount = repairEquipmentCount;
	}

	public Integer getRepairBulkMaterialCount() {
		return repairBulkMaterialCount;
	}

	public void setRepairBulkMaterialCount(Integer repairBulkMaterialCount) {
		this.repairBulkMaterialCount = repairBulkMaterialCount;
	}

	public Integer getFixEquipmentCount() {
		return fixEquipmentCount;
	}

	public void setFixEquipmentCount(Integer fixEquipmentCount) {
		this.fixEquipmentCount = fixEquipmentCount;
	}

	public Integer getFixBulkMaterialCount() {
		return fixBulkMaterialCount;
	}

	public void setFixBulkMaterialCount(Integer fixBulkMaterialCount) {
		this.fixBulkMaterialCount = fixBulkMaterialCount;
	}

	public String getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public List<RepairOrderEquipmentDO> getRepairOrderEquipmentDOList() {
		return repairOrderEquipmentDOList;
	}

	public void setRepairOrderEquipmentDOList(List<RepairOrderEquipmentDO> repairOrderEquipmentDOList) {
		this.repairOrderEquipmentDOList = repairOrderEquipmentDOList;
	}

	public List<RepairOrderBulkMaterialDO> getRepairOrderBulkMaterialDOList() {
		return repairOrderBulkMaterialDOList;
	}

	public void setRepairOrderBulkMaterialDOList(List<RepairOrderBulkMaterialDO> repairOrderBulkMaterialDOList) {
		this.repairOrderBulkMaterialDOList = repairOrderBulkMaterialDOList;
	}

	public String getRepairEndRemark() {
		return repairEndRemark;
	}

	public void setRepairEndRemark(String repairEndRemark) {
		this.repairEndRemark = repairEndRemark;
	}
}