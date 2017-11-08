package com.lxzl.erp.common.domain.purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseDeliveryOrderQueryParam extends BasePageParam {
    private String purchaseNo ;//采购单编号
    private Integer productSupplierId ;//商品供应商ID
    private Integer invoiceSupplierId ;//发票供应商ID
    private Integer warehouseId ;//仓库ID
    private Integer isInvoice ;//是否有发票，0否1是
    private Integer isNew ;//是否全新机
    private Integer purchaseDeliveryOrderStatus ;//采购发货单状态，0待发货，1已发货
    private Date createStartTime;
    private Date createEndTime;

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public Integer getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(Integer productSupplierId) {
        this.productSupplierId = productSupplierId;
    }

    public Integer getInvoiceSupplierId() {
        return invoiceSupplierId;
    }

    public void setInvoiceSupplierId(Integer invoiceSupplierId) {
        this.invoiceSupplierId = invoiceSupplierId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
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
}
