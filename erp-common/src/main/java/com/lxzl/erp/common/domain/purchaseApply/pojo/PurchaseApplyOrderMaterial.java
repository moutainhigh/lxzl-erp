package com.lxzl.erp.common.domain.purchaseApply.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseApplyOrderMaterial extends BasePO {

	private Integer purchaseApplyOrderMaterialId;   //唯一标识
	private Integer purchaseApplyOrderId;   //采购申请单ID
	private String purchaseApplyOrderNo;   //采购申请单编号
	private Integer materialId;   //配件ID
	private String materialNo;   //配件编号
	private Integer applyCount;   //计划采购数量
	private Integer realCount;   //实际采购数量
	private Date purchaseStartTime;   //采购开始时间
	private Date purchaseEndTime;   //全部采购完成时间
	private Integer purchaseApplyOrderItemStatus;   //单项状态，0-待采购，3-采购中，6-部分采购，9-全部采购
	private Integer isNew;   //是否全新，1是，0否
	private Date useTime;   //计划使用时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getPurchaseApplyOrderMaterialId(){
		return purchaseApplyOrderMaterialId;
	}

	public void setPurchaseApplyOrderMaterialId(Integer purchaseApplyOrderMaterialId){
		this.purchaseApplyOrderMaterialId = purchaseApplyOrderMaterialId;
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

}