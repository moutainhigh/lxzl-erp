package com.lxzl.erp.dataaccess.domain.purchase;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.math.BigDecimal;
import java.util.Date;


public class PurchaseReceiveOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer purchaseReceiveOrderId;
	private Integer purchaseOrderMaterialId;
	private Integer purchaseDeliveryOrderMaterialId;
	private Integer materialId;
	private String materialName;
	private String materialSnapshot;
	private Integer materialCount;
	private BigDecimal materialAmount;
	private Integer realMaterialId;
	private String realMaterialName;
	private String realMaterialSnapshot;
	private Integer realMaterialCount;
	private Integer isSrc;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
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

	public BigDecimal getMaterialAmount() {
		return materialAmount;
	}

	public void setMaterialAmount(BigDecimal materialAmount) {
		this.materialAmount = materialAmount;
	}
}