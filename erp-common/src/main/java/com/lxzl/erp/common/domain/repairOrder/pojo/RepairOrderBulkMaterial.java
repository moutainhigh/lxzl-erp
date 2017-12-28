package com.lxzl.erp.common.domain.repairOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairOrderBulkMaterial extends BasePO {

	private Integer repairOrderBulkMaterialId;   //唯一标识
	private String repairOrderNo;   //维修单编号
	private Integer bulkMaterialId;   //散料ID
	@NotBlank(message = ErrorCode.BULK_MATERIAL_NO_NOT_NULL,groups = {AddGroup.class})
	private String bulkMaterialNo;   //散料编号唯一
	private Date repairEndTime;   //维修完成时间
	private Integer orderId;   //订单ID，如果是在客户手里出现的维修，此字段不能为空
	private Integer orderMaterialId;   //订单物料项ID,如果是在客户手里出现的维修，此字段不能为空
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private String repairEndRemark; //维修完成的备注

	private BulkMaterial bulkMaterial;


	public Integer getRepairOrderBulkMaterialId(){
		return repairOrderBulkMaterialId;
	}

	public void setRepairOrderBulkMaterialId(Integer repairOrderBulkMaterialId){
		this.repairOrderBulkMaterialId = repairOrderBulkMaterialId;
	}

	public String getRepairOrderNo(){
		return repairOrderNo;
	}

	public void setRepairOrderNo(String repairOrderNo){
		this.repairOrderNo = repairOrderNo;
	}

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

	public Date getRepairEndTime(){
		return repairEndTime;
	}

	public void setRepairEndTime(Date repairEndTime){
		this.repairEndTime = repairEndTime;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public Integer getOrderMaterialId(){
		return orderMaterialId;
	}

	public void setOrderMaterialId(Integer orderMaterialId){
		this.orderMaterialId = orderMaterialId;
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

	public String getRepairEndRemark() {
		return repairEndRemark;
	}

	public void setRepairEndRemark(String repairEndRemark) {
		this.repairEndRemark = repairEndRemark;
	}
}