package com.lxzl.erp.dataaccess.domain.purchase;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;
import java.math.BigDecimal;

public class PurchaseDeliveryOrderDO  extends BaseDO {

	private Integer id;
	private Integer purchaseOrderId;
	private String purchaseDeliveryNo;
	private Integer warehouseId;
	private String warehouseSnapshot;
	private Integer isInvoice;
	private Integer isNew;
	private Integer purchaseDeliveryOrderStatus;
	private Date deliveryTime;
	private Integer dataStatus;
	private Integer ownerSupplierId;
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

	public String getPurchaseDeliveryNo(){
		return purchaseDeliveryNo;
	}

	public void setPurchaseDeliveryNo(String purchaseDeliveryNo){
		this.purchaseDeliveryNo = purchaseDeliveryNo;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
	}

	public String getWarehouseSnapshot() {
		return warehouseSnapshot;
	}

	public void setWarehouseSnapshot(String warehouseSnapshot) {
		this.warehouseSnapshot = warehouseSnapshot;
	}

	public Integer getIsInvoice(){
		return isInvoice;
	}

	public void setIsInvoice(Integer isInvoice){
		this.isInvoice = isInvoice;
	}

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
	}

	public Integer getPurchaseDeliveryOrderStatus(){
		return purchaseDeliveryOrderStatus;
	}

	public void setPurchaseDeliveryOrderStatus(Integer purchaseDeliveryOrderStatus){
		this.purchaseDeliveryOrderStatus = purchaseDeliveryOrderStatus;
	}

	public Date getDeliveryTime(){
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime){
		this.deliveryTime = deliveryTime;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public Integer getOwnerSupplierId(){
		return ownerSupplierId;
	}

	public void setOwnerSupplierId(Integer ownerSupplierId){
		this.ownerSupplierId = ownerSupplierId;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

}