package com.lxzl.erp.dataaccess.domain.purchaseApply;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class PurchaseApplyOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer purchaseApplyOrderId;
	private String purchaseApplyOrderNo;
	private Integer materialId;
	private String materialNo;
	private Integer applyCount;
	private Integer realCount;
	private Date purchaseStartTime;
	private Date purchaseEndTime;
	private Integer purchaseApplyOrderItemStatus;
	private Integer isNew;
	private Date useTime;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getPurchaseApplyOrderId(){
		return purchaseApplyOrderId;
	}

	public void setPurchaseApplyOrderId(Integer purchaseApplyOrderId){
		this.purchaseApplyOrderId = purchaseApplyOrderId;
	}

	public String getPurchaseApplyOrderNo(){
		return purchaseApplyOrderNo;
	}

	public void setPurchaseApplyOrderNo(String purchaseApplyOrderNo){
		this.purchaseApplyOrderNo = purchaseApplyOrderNo;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public String getMaterialNo(){
		return materialNo;
	}

	public void setMaterialNo(String materialNo){
		this.materialNo = materialNo;
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

	public Integer getPurchaseApplyOrderItemStatus(){
		return purchaseApplyOrderItemStatus;
	}

	public void setPurchaseApplyOrderItemStatus(Integer purchaseApplyOrderItemStatus){
		this.purchaseApplyOrderItemStatus = purchaseApplyOrderItemStatus;
	}

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
	}

	public Date getUseTime(){
		return useTime;
	}

	public void setUseTime(Date useTime){
		this.useTime = useTime;
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