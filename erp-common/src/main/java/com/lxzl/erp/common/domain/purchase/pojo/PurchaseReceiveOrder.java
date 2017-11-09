package com.lxzl.erp.common.domain.purchase.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseReceiveOrder implements Serializable {

	private Integer purchaseReceiveOrderId;   //唯一标识
	private Integer purchaseOrderId;   //采购单ID
	private Integer purchaseDeliveryOrderId;   //采购发货单ID
	@NotEmpty(message = ErrorCode.PURCHASE_RECEIVE_ORDER_NO_NOT_NULL , groups = {IdGroup.class,UpdateGroup.class})
	private String purchaseReceiveNo;   //采购收货单编号
	private Integer productSupplierId;   //商品供应商ID
	private Integer warehouseId;   //仓库ID
	private String warehouseSnapshot;   //收货方仓库快照，JSON格式
	private Integer isInvoice;   //是否有发票，0否1是
	private Integer autoAllotStatus;   //分拨情况，0-未分拨，1-已分拨，2-被分拨，没发票的分公司仓库单，将生成一个总公司收货单，并生成分拨单号，自动分拨到分公司仓库
	private String autoAllotNo;   //分拨单号，仅在is_auto_allot字段为1时有值
	@NotNull(message = ErrorCode.IS_NEW_NOT_NULL , groups = {AddGroup.class,UpdateGroup.class})
	@In(value = {CommonConstant.YES,CommonConstant.NO},message = ErrorCode.IS_INVOICE_VALUE_ERROR,groups = {UpdateGroup.class})
	private Integer isNew;   //是否全新机，0否1是
	private Integer purchaseReceiveOrderStatus;   //采购单收货状态，0待收货，1已签单
	private Date confirmTime;   //签单时间
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private Integer owner;   //数据归属人
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人

	@CollectionNotNull(message = ErrorCode.PURCHASE_RECEIVE_ORDER_PRODUCT_LIST_NOT_NULL , groups = {UpdateGroup.class})
	List<PurchaseReceiveOrderProduct> purchaseReceiveOrderProductList ;

	public Integer getPurchaseReceiveOrderId(){
		return purchaseReceiveOrderId;
	}

	public void setPurchaseReceiveOrderId(Integer purchaseReceiveOrderId){
		this.purchaseReceiveOrderId = purchaseReceiveOrderId;
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

	public List<PurchaseReceiveOrderProduct> getPurchaseReceiveOrderProductList() {
		return purchaseReceiveOrderProductList;
	}

	public void setPurchaseReceiveOrderProductList(List<PurchaseReceiveOrderProduct> purchaseReceiveOrderProductList) {
		this.purchaseReceiveOrderProductList = purchaseReceiveOrderProductList;
	}
}