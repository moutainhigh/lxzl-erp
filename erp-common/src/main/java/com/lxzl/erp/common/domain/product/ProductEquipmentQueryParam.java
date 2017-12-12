package com.lxzl.erp.common.domain.product;

import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

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
}
