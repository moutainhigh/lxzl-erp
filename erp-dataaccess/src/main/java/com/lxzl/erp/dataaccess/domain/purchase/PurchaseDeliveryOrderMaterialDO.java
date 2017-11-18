package com.lxzl.erp.dataaccess.domain.purchase;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class PurchaseDeliveryOrderMaterialDO  extends BaseDO {

	private Integer id;
	private Integer purchaseDeliveryOrderId;
	private Integer purchaseOrderMaterialId;
	private Integer materialId;
	private String materialName;
	private String materialSnapshot;
	private Integer materialCount;
	private BigDecimal materialAmount;
	private Integer realMaterialId;
	private String realMaterialName;
	private String realMaterialSnapshot;
	private Integer realMaterialCount;
	private BigDecimal realMaterialAmount;
	private Integer dataStatus;
	private String remark;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getPurchaseDeliveryOrderId(){
		return purchaseDeliveryOrderId;
	}

	public void setPurchaseDeliveryOrderId(Integer purchaseDeliveryOrderId){
		this.purchaseDeliveryOrderId = purchaseDeliveryOrderId;
	}

	public Integer getPurchaseOrderMaterialId(){
		return purchaseOrderMaterialId;
	}

	public void setPurchaseOrderMaterialId(Integer purchaseOrderMaterialId){
		this.purchaseOrderMaterialId = purchaseOrderMaterialId;
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

	public BigDecimal getMaterialAmount(){
		return materialAmount;
	}

	public void setMaterialAmount(BigDecimal materialAmount){
		this.materialAmount = materialAmount;
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

	public BigDecimal getRealMaterialAmount(){
		return realMaterialAmount;
	}

	public void setRealMaterialAmount(BigDecimal realMaterialAmount){
		this.realMaterialAmount = realMaterialAmount;
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