package com.lxzl.erp.dataaccess.domain.repairOrder;

import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;


public class RepairOrderEquipmentDO  extends BaseDO {

	private Integer id;
	private String repairOrderNo;
	private Integer equipmentId;
	private String equipmentNo;
	private Date repairEndTime;
	private Integer dataStatus;
	private Integer orderId;
	private Integer orderProductId;
	private String remark;
	private String repairEndRemark; //维修完成的备注

	@Transient
	private ProductEquipmentDO productEquipmentDO;

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

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
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

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public ProductEquipmentDO getProductEquipmentDO() {
		return productEquipmentDO;
	}

	public void setProductEquipmentDO(ProductEquipmentDO productEquipmentDO) {
		this.productEquipmentDO = productEquipmentDO;
	}

	public String getRepairEndRemark() {
		return repairEndRemark;
	}

	public void setRepairEndRemark(String repairEndRemark) {
		this.repairEndRemark = repairEndRemark;
	}
}