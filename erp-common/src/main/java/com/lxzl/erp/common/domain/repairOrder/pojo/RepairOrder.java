package com.lxzl.erp.common.domain.repairOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairOrder extends BasePO {

	private Integer repairOrderId;   //唯一标识
	@NotBlank(message = ErrorCode.REPAIR_ORDER_NO_IS_NOT_NULL,groups = {IdGroup.class})
	private String repairOrderNo;   //维修单编号
	@NotBlank(message = ErrorCode.REPAIR_REASON_IS_NOT_NULL,groups = {AddGroup.class})
	private String repairReason;   //维修原因，由发起人填写
	private Integer repairOrderStatus;   //维修单状态，0-初始化维修单,4-审核中,8-待维修,12-维修中,16-维修完成回库,20-取消维修
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Integer repairEquipmentCount; //送修设备数量
	private Integer repairBulkMaterialCount; //送修物料数量
	private Integer fixEquipmentCount; //修复设备数量
	private Integer fixBulkMaterialCount; //修复物料数量
	private String warehouseNo;//当前仓库编号
	private String repairEndRemark; //维修完成的备注


	private String warehouseName;

	@Valid
	private List<RepairOrderEquipment> repairOrderEquipmentList;
	@Valid
	private  List<RepairOrderBulkMaterial> repairOrderBulkMaterialList;

	// 审核人和提交审核信息,只提供给审核的时候用
	private Integer verifyUser;
	private String commitRemark;

	public Integer getRepairOrderId(){
		return repairOrderId;
	}

	public void setRepairOrderId(Integer repairOrderId){
		this.repairOrderId = repairOrderId;
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

	public List<RepairOrderEquipment> getRepairOrderEquipmentList() {
		return repairOrderEquipmentList;
	}

	public void setRepairOrderEquipmentList(List<RepairOrderEquipment> repairOrderEquipmentList) {
		this.repairOrderEquipmentList = repairOrderEquipmentList;
	}

	public List<RepairOrderBulkMaterial> getRepairOrderBulkMaterialList() {
		return repairOrderBulkMaterialList;
	}

	public void setRepairOrderBulkMaterialList(List<RepairOrderBulkMaterial> repairOrderBulkMaterialList) {
		this.repairOrderBulkMaterialList = repairOrderBulkMaterialList;
	}

	public Integer getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(Integer verifyUser) {
		this.verifyUser = verifyUser;
	}

	public String getCommitRemark() {
		return commitRemark;
	}

	public void setCommitRemark(String commitRemark) {
		this.commitRemark = commitRemark;
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

	public String getRepairEndRemark() {
		return repairEndRemark;
	}

	public void setRepairEndRemark(String repairEndRemark) {
		this.repairEndRemark = repairEndRemark;
	}
}