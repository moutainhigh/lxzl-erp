package com.lxzl.erp.dataaccess.domain.purchase;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.math.BigDecimal;
import java.util.Date;

public class PurchaseOrderDO extends BaseDO {

    private Integer id;
    private String purchaseNo;
    private Integer productSupplierId;
    private Integer invoiceSupplierId;
    private Integer warehouseId;
    private Integer isInvoice;
    private Integer isNew;
    private BigDecimal purchaseOrderAmountTotal;
    private BigDecimal purchaseOrderAmountReal;
    private BigDecimal purchaseOrderAmountStatement;
    private Integer purchaseOrderStatus;
    private Integer commitStatus;
    private Date deliveryTime;
    private Integer dataStatus;
    private Integer owner;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public BigDecimal getPurchaseOrderAmountTotal() {
        return purchaseOrderAmountTotal;
    }

    public void setPurchaseOrderAmountTotal(BigDecimal purchaseOrderAmountTotal) {
        this.purchaseOrderAmountTotal = purchaseOrderAmountTotal;
    }

    public BigDecimal getPurchaseOrderAmountReal() {
        return purchaseOrderAmountReal;
    }

    public void setPurchaseOrderAmountReal(BigDecimal purchaseOrderAmountReal) {
        this.purchaseOrderAmountReal = purchaseOrderAmountReal;
    }

    public BigDecimal getPurchaseOrderAmountStatement() {
        return purchaseOrderAmountStatement;
    }

    public void setPurchaseOrderAmountStatement(BigDecimal purchaseOrderAmountStatement) {
        this.purchaseOrderAmountStatement = purchaseOrderAmountStatement;
    }

    public Integer getPurchaseOrderStatus() {
        return purchaseOrderStatus;
    }

    public void setPurchaseOrderStatus(Integer purchaseOrderStatus) {
        this.purchaseOrderStatus = purchaseOrderStatus;
    }

    public Integer getCommitStatus() {
        return commitStatus;
    }

    public void setCommitStatus(Integer commitStatus) {
        this.commitStatus = commitStatus;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}