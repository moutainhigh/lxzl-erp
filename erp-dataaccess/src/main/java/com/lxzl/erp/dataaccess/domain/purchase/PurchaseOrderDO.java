package com.lxzl.erp.dataaccess.domain.purchase;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PurchaseOrderDO extends BaseDO {

    private Integer id;
    private String purchaseNo;
    private Integer productSupplierId;
    private Integer warehouseId;
    private String warehouseSnapshot;
    private Integer isInvoice;
    private Integer isNew;
    private BigDecimal purchaseOrderAmountTotal;
    private BigDecimal purchaseOrderAmountReal;
    private BigDecimal purchaseOrderAmountStatement;
    private Integer purchaseOrderStatus;
    private Date deliveryTime;
    private Integer purchaseType;//采购类型：1-整机及四大件，2-小配件
    private Integer dataStatus;
    private Integer owner;
    private String remark;

    @Transient
    private List<PurchaseOrderProductDO> purchaseOrderProductDOList;
    @Transient
    private List<PurchaseOrderMaterialDO> purchaseOrderMaterialDOList;
    @Transient
    private String productSupplierName;//商品供应商名称
    @Transient
    private String ownerName;//采购员名称

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

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseSnapshot() {
        return warehouseSnapshot;
    }

    public void setWarehouseSnapshot(String warehouseSnapshot) {
        this.warehouseSnapshot = warehouseSnapshot;
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

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(Integer purchaseType) {
        this.purchaseType = purchaseType;
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

    public List<PurchaseOrderProductDO> getPurchaseOrderProductDOList() {
        return purchaseOrderProductDOList;
    }

    public void setPurchaseOrderProductDOList(List<PurchaseOrderProductDO> purchaseOrderProductDOList) {
        this.purchaseOrderProductDOList = purchaseOrderProductDOList;
    }

    public String getProductSupplierName() {
        return productSupplierName;
    }

    public void setProductSupplierName(String productSupplierName) {
        this.productSupplierName = productSupplierName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<PurchaseOrderMaterialDO> getPurchaseOrderMaterialDOList() {
        return purchaseOrderMaterialDOList;
    }

    public void setPurchaseOrderMaterialDOList(List<PurchaseOrderMaterialDO> purchaseOrderMaterialDOList) {
        this.purchaseOrderMaterialDOList = purchaseOrderMaterialDOList;
    }
}