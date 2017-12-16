package com.lxzl.erp.dataaccess.domain.repairOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
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

}