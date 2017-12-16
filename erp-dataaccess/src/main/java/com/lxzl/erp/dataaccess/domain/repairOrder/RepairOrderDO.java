package com.lxzl.erp.dataaccess.domain.repairOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class RepairOrderDO  extends BaseDO {

	private Integer id;
	private String repairOrderNo;
	private String repairReason;
	private Integer repairOrderStatus;
	private Integer dataStatus;
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

}