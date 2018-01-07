package com.lxzl.erp.common.domain.purchaseApply.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseApplyProductRelation extends BasePO {

	private Integer purchaseApplyProductRelationId;   //唯一标识
	private Integer purchaseOrderId;   //采购单ID
	private String purchaseOrderNo;   //采购单编号
	private Integer purchaseOrderProductId;   //采购单商品项ID
	private Integer purchaseApplyOrderProductId;   //采购申请单商品项ID
	private Integer applyCount;   //预计采购数量
	private Integer realCount;   //实际采购数量
	private Date purchaseStartTime;   //采购开始时间
	private Date purchaseEndTime;   //全部采购完成时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人


	public Integer getPurchaseApplyProductRelationId(){
		return purchaseApplyProductRelationId;
	}

	public void setPurchaseApplyProductRelationId(Integer purchaseApplyProductRelationId){
		this.purchaseApplyProductRelationId = purchaseApplyProductRelationId;
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

	public Integer getPurchaseOrderProductId(){
		return purchaseOrderProductId;
	}

	public void setPurchaseOrderProductId(Integer purchaseOrderProductId){
		this.purchaseOrderProductId = purchaseOrderProductId;
	}

	public Integer getPurchaseApplyOrderProductId(){
		return purchaseApplyOrderProductId;
	}

	public void setPurchaseApplyOrderProductId(Integer purchaseApplyOrderProductId){
		this.purchaseApplyOrderProductId = purchaseApplyOrderProductId;
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