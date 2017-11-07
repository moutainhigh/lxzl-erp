package com.lxzl.erp.dataaccess.domain.purchase;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class PurchaseReceiveOrderDO  extends BaseDO {

	private Integer id;
	private Integer purchaseOrderId;
	private Integer purchaseDeliveryOrderId;
	private String purchaseReceiveNo;
	private Integer productSupplierId;
	private Integer warehouseId;
	private String warehouseSnapshot;
	private Integer isInvoice;
	private Integer autoAllotStatus;
	private String autoAllotNo;
	private Integer isNew;
	private Integer purchaseReceiveOrderStatus;
	private Date confirmTime;
	private Integer dataStatus;
	private Integer owner;
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

	public Integer getPurchaseDeliveryOrderId(){
		return purchaseDeliveryOrderId;
	}

	public void setPurchaseDeliveryOrderId(Integer purchaseDeliveryOrderId){
		this.purchaseDeliveryOrderId = purchaseDeliveryOrderId;
	}

	public String getPurchaseReceiveNo(){
		return purchaseReceiveNo;
	}

	public void setPurchaseReceiveNo(String purchaseReceiveNo){
		this.purchaseReceiveNo = purchaseReceiveNo;
	}

	public Integer getProductSupplierId(){
		return productSupplierId;
	}

	public void setProductSupplierId(Integer productSupplierId){
		this.productSupplierId = productSupplierId;
	}

	public Integer getWarehouseId(){
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId){
		this.warehouseId = warehouseId;
	}

	public String getWarehouseSnapshot(){
		return warehouseSnapshot;
	}

	public void setWarehouseSnapshot(String warehouseSnapshot){
		this.warehouseSnapshot = warehouseSnapshot;
	}

	public Integer getIsInvoice(){
		return isInvoice;
	}

	public void setIsInvoice(Integer isInvoice){
		this.isInvoice = isInvoice;
	}

	public Integer getAutoAllotStatus(){
		return autoAllotStatus;
	}

	public void setAutoAllotStatus(Integer autoAllotStatus){
		this.autoAllotStatus = autoAllotStatus;
	}

	public String getAutoAllotNo() {
		return autoAllotNo;
	}

	public void setAutoAllotNo(String autoAllotNo) {
		this.autoAllotNo = autoAllotNo;
	}

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
	}

	public Integer getPurchaseReceiveOrderStatus(){
		return purchaseReceiveOrderStatus;
	}

	public void setPurchaseReceiveOrderStatus(Integer purchaseReceiveOrderStatus){
		this.purchaseReceiveOrderStatus = purchaseReceiveOrderStatus;
	}

	public Date getConfirmTime(){
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime){
		this.confirmTime = confirmTime;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public Integer getOwner(){
		return owner;
	}

	public void setOwner(Integer owner){
		this.owner = owner;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

}