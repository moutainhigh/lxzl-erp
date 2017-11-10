package com.lxzl.erp.common.domain.purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseReceiveOrderQueryParam extends BasePageParam {
    private Integer purchaseOrderId ;
    private Integer purchaseDeliveryOrderId ;
    private Integer warehouseId ;

    private String purchaseNo ;//采购单编号
    private String purchaseDeliveryNo ;//发货单编号
    private String purchaseReceiveMo ; //采购收货单编号
    private String warehouseNo ; //仓库编号
    private Integer productSupplierId;//商品供应商ID
    private Integer autoAllotStatus;//分拨情况，0-未分拨，1-已分拨，2-被分拨，没发票的分公司仓库单，将生成一个总公司收货单，并生成分拨单号，自动分拨到分公司仓库
//    private Integer owner;//签单人
    private Integer isInvoice ;//是否有发票，0否1是
    private Integer isNew ;//是否全新机
    private Integer purchaseReceiveOrderStatus ;//采购单收货状态，0待收货，1已签单
    private Date createStartTime;//创建收货单起始时间
    private Date createEndTime;//创建收货单结束时间
    private Date confirmStartTime;//确认签单起始时间
    private Date confirmEndTime;//确认签单结束时间

    private String ownerString;//签单人真实姓名

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Integer getPurchaseDeliveryOrderId() {
        return purchaseDeliveryOrderId;
    }

    public void setPurchaseDeliveryOrderId(Integer purchaseDeliveryOrderId) {
        this.purchaseDeliveryOrderId = purchaseDeliveryOrderId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public String getPurchaseDeliveryNo() {
        return purchaseDeliveryNo;
    }

    public void setPurchaseDeliveryNo(String purchaseDeliveryNo) {
        this.purchaseDeliveryNo = purchaseDeliveryNo;
    }

    public String getPurchaseReceiveMo() {
        return purchaseReceiveMo;
    }

    public void setPurchaseReceiveMo(String purchaseReceiveMo) {
        this.purchaseReceiveMo = purchaseReceiveMo;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public Integer getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(Integer productSupplierId) {
        this.productSupplierId = productSupplierId;
    }

    public Integer getAutoAllotStatus() {
        return autoAllotStatus;
    }

    public void setAutoAllotStatus(Integer autoAllotStatus) {
        this.autoAllotStatus = autoAllotStatus;
    }

//    public Integer getOwner() {
//        return owner;
//    }
//
//    public void setOwner(Integer owner) {
//        this.owner = owner;
//    }

    public Integer getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Integer isInvoice) {
        this.isInvoice = isInvoice;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public String getOwnerString() {
        return ownerString;
    }

    public void setOwnerString(String ownerString) {
        this.ownerString = ownerString;
    }

    public Integer getPurchaseReceiveOrderStatus() {
        return purchaseReceiveOrderStatus;
    }

    public void setPurchaseReceiveOrderStatus(Integer purchaseReceiveOrderStatus) {
        this.purchaseReceiveOrderStatus = purchaseReceiveOrderStatus;
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }

    public Date getConfirmStartTime() {
        return confirmStartTime;
    }

    public void setConfirmStartTime(Date confirmStartTime) {
        this.confirmStartTime = confirmStartTime;
    }

    public Date getConfirmEndTime() {
        return confirmEndTime;
    }

    public void setConfirmEndTime(Date confirmEndTime) {
        this.confirmEndTime = confirmEndTime;
    }
}
