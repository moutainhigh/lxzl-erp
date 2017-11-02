package com.lxzl.erp.dataaccess.domain.purchase;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrderDO extends BaseDO {

    private Integer id;

    private String purchaseNo;

    private Integer productSupplierId;

    private Integer invoiceSupplierId;

    private Integer warehouseId;

    private Integer isInvoice;

    private Integer isNew;

    private BigDecimal purchaseOrderAmountTotal;

    private Integer purchaseOrderStatus;

    private Date deliveryTime;

    private Integer verifyStatus;

    private String verifyUser;

    private Date verifyTime;

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

    public Integer getPurchaseOrderStatus() {
        return purchaseOrderStatus;
    }

    public void setPurchaseOrderStatus(Integer purchaseOrderStatus) {
        this.purchaseOrderStatus = purchaseOrderStatus;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
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