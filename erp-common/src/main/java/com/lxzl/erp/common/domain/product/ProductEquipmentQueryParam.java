package com.lxzl.erp.common.domain.product;

import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEquipmentQueryParam extends BasePageParam implements Serializable {
    private Integer productEquipmentId;
    private String orderNo;
    private String equipmentNo;
    private Integer productId;
    private String productName;
    private Integer skuId;
    private Integer equipmentStatus;
    private Integer isNew;
    private Date createStartTime;
    private Date createEndTime;

    private Integer currentWarehouseId;
    private Integer ownerWarehouseId;

    //控制数据权限
    private List<Integer> passiveUserIdList;//控制数据权限
    private Integer subCompanyId;
    private List<Integer> warehouseIdList;//控制数据权限

    public Integer getProductEquipmentId() {
        return productEquipmentId;
    }

    public void setProductEquipmentId(Integer productEquipmentId) {
        this.productEquipmentId = productEquipmentId;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(Integer equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCurrentWarehouseId() {
        return currentWarehouseId;
    }

    public void setCurrentWarehouseId(Integer currentWarehouseId) {
        this.currentWarehouseId = currentWarehouseId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getOwnerWarehouseId() {
        return ownerWarehouseId;
    }

    public void setOwnerWarehouseId(Integer ownerWarehouseId) {
        this.ownerWarehouseId = ownerWarehouseId;
    }

    public List<Integer> getPassiveUserIdList() { return passiveUserIdList; }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) { this.passiveUserIdList = passiveUserIdList; }

    public Integer getSubCompanyId() { return subCompanyId; }

    public void setSubCompanyId(Integer subCompanyId) { this.subCompanyId = subCompanyId; }

    public List<Integer> getWarehouseIdList() { return warehouseIdList; }

    public void setWarehouseIdList(List<Integer> warehouseIdList) { this.warehouseIdList = warehouseIdList; }
}
