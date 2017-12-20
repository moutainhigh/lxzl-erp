package com.lxzl.erp.dataaccess.domain.repairOrder;

import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.Date;


public class RepairOrderBulkMaterialDO  extends BaseDO {

	private Integer id;
	private String repairOrderNo;
	private Integer bulkMaterialId;
	private String bulkMaterialNo;
	private Date repairEndTime;
	private Integer orderId;
	private Integer orderMaterialId;
	private Integer dataStatus;
	private String remark;

	@Transient
	private BulkMaterialDO bulkMaterialDO;

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

	public BulkMaterialDO getBulkMaterialDO() {
		return bulkMaterialDO;
	}

	public void setBulkMaterialDO(BulkMaterialDO bulkMaterialDO) {
		this.bulkMaterialDO = bulkMaterialDO;
	}
}