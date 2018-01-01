package com.lxzl.erp.common.domain.purchase;

import com.lxzl.erp.common.domain.base.BasePageParam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrderQueryParam extends BasePageParam {
    private String purchaseNo ;//采购单编号
    private Integer purchaseType ;//采购单类型
    private Integer productSupplierId ;//商品供应商ID
    private String warehouseNo ;//仓库ID
    private Integer warehouseId ;//仓库ID
    private Integer isInvoice ;//是否有发票，0否1是
    private Integer isNew ;//是否全新机
    private Integer purchaseOrderStatus ;//采购单状态，0待采购，1部分采购，2全部采购
    private Integer commitStatus ;//提交状态，0未提交，1已提交
    private Date createStartTime;
    private Date createEndTime;

    //控制数据权限
    private List<Integer> passiveUserIdList;

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public Integer getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(Integer purchaseType) {
        this.purchaseType = purchaseType;
    }

    public Integer getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(Integer productSupplierId) {
        this.productSupplierId = productSupplierId;
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

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<Integer> getPassiveUserIdList() {
        return passiveUserIdList;
    }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) {
        this.passiveUserIdList = passiveUserIdList;
    }

}
