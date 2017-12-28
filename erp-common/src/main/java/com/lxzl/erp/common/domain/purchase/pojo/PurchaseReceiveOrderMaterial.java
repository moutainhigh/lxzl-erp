package com.lxzl.erp.common.domain.purchase.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.IdGroup;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseReceiveOrderMaterial extends BasePO {

	@NotNull(message = ErrorCode.ID_NOT_NULL,groups = {IdGroup.class})
	private Integer purchaseReceiveOrderMaterialId;   //唯一标识
	private Integer purchaseReceiveOrderId;   //采购单ID
	private Integer purchaseOrderMaterialId;   //采购单物料项ID
	private Integer purchaseDeliveryOrderMaterialId;   //采购发货单物料项ID
	private Integer materialId;   //物料ID冗余
	private String materialName;   //物料名称冗余
	private String materialSnapshot;   //物料快照
	private Integer materialCount;   //物料总数
	private BigDecimal materialAmount;   //物料单价
	private Integer realMaterialId;   //实际物料ID
	private String realMaterialNo;
	private String realMaterialName;   //实际物料ID名称
	private String realMaterialSnapshot;   //实际物料快照
	private Integer realMaterialCount;   //实际物料总数
	private Integer isSrc;   //原单项标志，查原单时此标志要传入0，0-收货新添项，1-原单项
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	public Integer getPurchaseReceiveOrderMaterialId(){
		return purchaseReceiveOrderMaterialId;
	}

	public void setPurchaseReceiveOrderMaterialId(Integer purchaseReceiveOrderMaterialId){
		this.purchaseReceiveOrderMaterialId = purchaseReceiveOrderMaterialId;
	}

	public Integer getPurchaseReceiveOrderId(){
		return purchaseReceiveOrderId;
	}

	public void setPurchaseReceiveOrderId(Integer purchaseReceiveOrderId){
		this.purchaseReceiveOrderId = purchaseReceiveOrderId;
	}

	public Integer getPurchaseOrderMaterialId(){
		return purchaseOrderMaterialId;
	}

	public void setPurchaseOrderMaterialId(Integer purchaseOrderMaterialId){
		this.purchaseOrderMaterialId = purchaseOrderMaterialId;
	}

	public Integer getPurchaseDeliveryOrderMaterialId(){
		return purchaseDeliveryOrderMaterialId;
	}

	public void setPurchaseDeliveryOrderMaterialId(Integer purchaseDeliveryOrderMaterialId){
		this.purchaseDeliveryOrderMaterialId = purchaseDeliveryOrderMaterialId;
	}

	public Integer getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Integer materialId){
		this.materialId = materialId;
	}

	public String getRealMaterialNo() {
		return realMaterialNo;
	}

	public void setRealMaterialNo(String realMaterialNo) {
		this.realMaterialNo = realMaterialNo;
	}

	public String getMaterialName(){
		return materialName;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

	public String getMaterialSnapshot(){
		return materialSnapshot;
	}

	public void setMaterialSnapshot(String materialSnapshot){
		this.materialSnapshot = materialSnapshot;
	}

	public Integer getMaterialCount(){
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount){
		this.materialCount = materialCount;
	}

	public Integer getRealMaterialId(){
		return realMaterialId;
	}

	public void setRealMaterialId(Integer realMaterialId){
		this.realMaterialId = realMaterialId;
	}

	public String getRealMaterialName(){
		return realMaterialName;
	}

	public void setRealMaterialName(String realMaterialName){
		this.realMaterialName = realMaterialName;
	}

	public String getRealMaterialSnapshot(){
		return realMaterialSnapshot;
	}

	public void setRealMaterialSnapshot(String realMaterialSnapshot){
		this.realMaterialSnapshot = realMaterialSnapshot;
	}

	public Integer getRealMaterialCount(){
		return realMaterialCount;
	}

	public void setRealMaterialCount(Integer realMaterialCount){
		this.realMaterialCount = realMaterialCount;
	}

	public Integer getIsSrc(){
		return isSrc;
	}

	public void setIsSrc(Integer isSrc){
		this.isSrc = isSrc;
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

	public BigDecimal getMaterialAmount() {
		return materialAmount;
	}

	public void setMaterialAmount(BigDecimal materialAmount) {
		this.materialAmount = materialAmount;
	}
}