package com.lxzl.erp.common.domain.purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseDeliveryOrderQueryParam extends BasePageParam {
    private Integer purchaseOrderId ;
    private String purchaseNo ;//采购单编号
    private Integer warehouseId ;
    private String warehouseNo ; //仓库编号
    private Integer isInvoice ;//是否有发票，0否1是
    private Integer isNew ;//是否全新机
    private Integer purchaseDeliveryOrderStatus ;//采购发货单状态，0待发货，1已发货
    private Date createStartTime;
    private Date createEndTime;

    private List<Integer> passiveUserIdList;//控制数据权限

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

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

    public Integer getPurchaseDeliveryOrderStatus() {
        return purchaseDeliveryOrderStatus;
    }

    public void setPurchaseDeliveryOrderStatus(Integer purchaseDeliveryOrderStatus) {
        this.purchaseDeliveryOrderStatus = purchaseDeliveryOrderStatus;
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

    public List<Integer> getPassiveUserIdList() {
        return passiveUserIdList;
    }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) {
        this.passiveUserIdList = passiveUserIdList;
    }
}
