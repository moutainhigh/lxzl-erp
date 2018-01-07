package com.lxzl.erp.dataaccess.domain.purchaseApply;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class PurchaseApplyOrderDO  extends BaseDO {

	private Integer id;
	private String purchaseApplyOrderNo;
	private Integer applyUserId;
	private Integer warehouseId;
	private Integer departmentId;
	private Integer purchaseApplyOrderStatus;
	private Date allUseTime;
	private Date purchaseStartTime;
	private Date purchaseEndTime;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getPurchaseApplyOrderNo(){
		return purchaseApplyOrderNo;
	}

	public void setPurchaseApplyOrderNo(String purchaseApplyOrderNo){
		this.purchaseApplyOrderNo = purchaseApplyOrderNo;
	}

	public Integer getApplyUserId(){
		return applyUserId;
	}

	public void setApplyUserId(Integer applyUserId){
		this.applyUserId = applyUserId;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
	}

	public Integer getDepartmentId(){
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId){
		this.departmentId = departmentId;
	}

	public Integer getPurchaseApplyOrderStatus(){
		return purchaseApplyOrderStatus;
	}

	public void setPurchaseApplyOrderStatus(Integer purchaseApplyOrderStatus){
		this.purchaseApplyOrderStatus = purchaseApplyOrderStatus;
	}

	public Date getAllUseTime(){
		return allUseTime;
	}

	public void setAllUseTime(Date allUseTime){
		this.allUseTime = allUseTime;
	}

	public Date getPurchaseStartTime(){
		return purchaseStartTime;
	}

	public void setPurchaseStartTime(Date purchaseStartTime){
		this.purchaseStartTime = purchaseStartTime;
	}

	public Date getPurchaseEndTime(){
		return purchaseEndTime;
	}

	public void setPurchaseEndTime(Date purchaseEndTime){
		this.purchaseEndTime = purchaseEndTime;
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