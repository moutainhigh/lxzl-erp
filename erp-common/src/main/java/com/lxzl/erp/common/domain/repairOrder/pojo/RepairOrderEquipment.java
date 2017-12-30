package com.lxzl.erp.common.domain.repairOrder.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairOrderEquipment extends BasePO {

	private Integer repairOrderEquipmentId;   //唯一标识
	private String repairOrderNo;   //维修单编号
	private Integer equipmentId;   //设备ID
	@NotBlank(message = ErrorCode.EQUIPMENT_NO_NOT_NULL,groups = {AddGroup.class})
	private String equipmentNo;   //设备编号唯一
	private Date repairEndTime;   //维修完成时间
	private Integer orderId;   //订单ID，如果是在客户手里出现的维修，此字段不能为空
	private Integer orderProductId;   //订单商品项ID,如果是在客户手里出现的维修，此字段不能为空
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private String repairEndRemark; //维修完成的备注

	private ProductEquipment productEquipment;

	public Integer getRepairOrderEquipmentId(){
		return repairOrderEquipmentId;
	}

	public void setRepairOrderEquipmentId(Integer repairOrderEquipmentId){
		this.repairOrderEquipmentId = repairOrderEquipmentId;
	}

	public String getRepairOrderNo(){
		return repairOrderNo;
	}

	public void setRepairOrderNo(String repairOrderNo){
		this.repairOrderNo = repairOrderNo;
	}

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

	public Integer getOrderProductId(){
		return orderProductId;
	}

	public void setOrderProductId(Integer orderProductId){
		this.orderProductId = orderProductId;
	}

	public Integer getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public ProductEquipment getProductEquipment() {
		return productEquipment;
	}

	public void setProductEquipment(ProductEquipment productEquipment) {
		this.productEquipment = productEquipment;
	}

	public String getRepairEndRemark() {
		return repairEndRemark;
	}

	public void setRepairEndRemark(String repairEndRemark) {
		this.repairEndRemark = repairEndRemark;
	}
}