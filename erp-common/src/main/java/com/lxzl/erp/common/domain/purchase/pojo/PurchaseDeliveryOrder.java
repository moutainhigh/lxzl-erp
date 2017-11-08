package com.lxzl.erp.common.domain.purchase.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseDeliveryOrder implements Serializable {

	private Integer purchaseDeliveryOrderId;   //唯一标识
	private Integer purchaseOrderId;   //采购单ID
	@NotNull(message = ErrorCode.PURCHASE_DELIVERY_ORDER_NO_NOT_NULL , groups = {IdGroup.class})
	private String purchaseDeliveryNo;   //采购发货单编号
	private Integer warehouseId;   //收货方仓库ID
	private String warehouseSnapshot;   //收货方仓库快照，JSON格式
	private Integer isInvoice;   //是否有发票，0否1是
	private Integer isNew;   //是否全新机
	private Integer purchaseDeliveryOrderStatus;   //采购发货单状态，0待发货，1已发货
	private Date deliveryTime;   //发货时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Integer ownerSupplierId;   //数据归属人
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	private List<PurchaseDeliveryOrderProduct> purchaseDeliveryOrderProductList;


	public Integer getPurchaseDeliveryOrderId(){
		return purchaseDeliveryOrderId;
	}

	public void setPurchaseDeliveryOrderId(Integer purchaseDeliveryOrderId){
		this.purchaseDeliveryOrderId = purchaseDeliveryOrderId;
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

	public List<PurchaseDeliveryOrderProduct> getPurchaseDeliveryOrderProductList() {
		return purchaseDeliveryOrderProductList;
	}

	public void setPurchaseDeliveryOrderProductList(List<PurchaseDeliveryOrderProduct> purchaseDeliveryOrderProductList) {
		this.purchaseDeliveryOrderProductList = purchaseDeliveryOrderProductList;
	}
}