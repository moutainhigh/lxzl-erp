package com.lxzl.erp.dataaccess.domain.purchaseApply;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class PurchaseApplyMaterialRelationDO  extends BaseDO {

	private Integer id;
	private Integer purchaseOrderId;
	private String purchaseOrderNo;
	private Integer purchaseOrderMaterialId;
	private Integer purchaseApplyOrderMaterialId;
	private Integer applyCount;
	private Integer realCount;
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

	public Integer getPurchaseOrderId(){
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(Integer purchaseOrderId){
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getPurchaseOrderNo(){
		return purchaseOrderNo;
	}

	public void setPurchaseOrderNo(String purchaseOrderNo){
		this.purchaseOrderNo = purchaseOrderNo;
	}

	public Integer getPurchaseOrderMaterialId(){
		return purchaseOrderMaterialId;
	}

	public void setPurchaseOrderMaterialId(Integer purchaseOrderMaterialId){
		this.purchaseOrderMaterialId = purchaseOrderMaterialId;
	}

	public Integer getPurchaseApplyOrderMaterialId(){
		return purchaseApplyOrderMaterialId;
	}

	public void setPurchaseApplyOrderMaterialId(Integer purchaseApplyOrderMaterialId){
		this.purchaseApplyOrderMaterialId = purchaseApplyOrderMaterialId;
	}

	public Integer getApplyCount(){
		return applyCount;
	}

	public void setApplyCount(Integer applyCount){
		this.applyCount = applyCount;
	}

	public Integer getRealCount(){
		return realCount;
	}

	public void setRealCount(Integer realCount){
		this.realCount = realCount;
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