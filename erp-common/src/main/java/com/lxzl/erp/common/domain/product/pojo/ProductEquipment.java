package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEquipment implements Serializable {
    private Integer productEquipmentId;
    private String equipmentNo;
    private Integer productId;
    private Integer skuId;
    private Integer currentWarehouseId;
    private Integer currentWarehousePositionId;
    private Integer ownerWarehouseId;
    private Integer ownerWarehousePositionId;
    private BigDecimal equipmentPrice;
    private Integer equipmentStatus;
    private Integer dataStatus;
    private String remark;
    private List<ProductImg> productImgList;           // 商品图片
    private List<BulkMaterial> bulkMaterialList;        // 设备散料

    private String productName;

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

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ProductImg> getProductImgList() {
        return productImgList;
    }

    public void setProductImgList(List<ProductImg> productImgList) {
        this.productImgList = productImgList;
    }

    public Integer getCurrentWarehouseId() {
        return currentWarehouseId;
    }

    public void setCurrentWarehouseId(Integer currentWarehouseId) {
        this.currentWarehouseId = currentWarehouseId;
    }

    public Integer getCurrentWarehousePositionId() {
        return currentWarehousePositionId;
    }

    public void setCurrentWarehousePositionId(Integer currentWarehousePositionId) {
        this.currentWarehousePositionId = currentWarehousePositionId;
    }

    public Integer getOwnerWarehouseId() {
        return ownerWarehouseId;
    }

    public void setOwnerWarehouseId(Integer ownerWarehouseId) {
        this.ownerWarehouseId = ownerWarehouseId;
    }

    public Integer getOwnerWarehousePositionId() {
        return ownerWarehousePositionId;
    }

    public void setOwnerWarehousePositionId(Integer ownerWarehousePositionId) {
        this.ownerWarehousePositionId = ownerWarehousePositionId;
    }

    public BigDecimal getEquipmentPrice() {
        return equipmentPrice;
    }

    public void setEquipmentPrice(BigDecimal equipmentPrice) {
        this.equipmentPrice = equipmentPrice;
    }

    public List<BulkMaterial> getBulkMaterialList() {
        return bulkMaterialList;
    }

    public void setBulkMaterialList(List<BulkMaterial> bulkMaterialList) {
        this.bulkMaterialList = bulkMaterialList;
    }
}
