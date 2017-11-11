package com.lxzl.erp.dataaccess.domain.product;

import com.lxzl.erp.dataaccess.domain.material.BulkMaterialDO;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEquipmentDO extends BaseDO {
    private Integer id;
    private String equipmentNo;
    private Integer productId;
    private String productName;
    private Integer skuId;
    private Integer warehouseId;
    private Integer warehousePositionId;
    private Integer ownerWarehouseId;
    private Integer ownerWarehousePositionId;
    private BigDecimal equipmentPrice;
    private Integer equipmentStatus;
    private Integer dataStatus;
    private String remark;
    private List<ProductImgDO> productImgDOList;           // 商品图片

    private List<BulkMaterialDO> bulkMaterialDOList;        // 设备散料

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<ProductImgDO> getProductImgDOList() {
        return productImgDOList;
    }

    public void setProductImgDOList(List<ProductImgDO> productImgDOList) {
        this.productImgDOList = productImgDOList;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getWarehousePositionId() {
        return warehousePositionId;
    }

    public void setWarehousePositionId(Integer warehousePositionId) {
        this.warehousePositionId = warehousePositionId;
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

    public List<BulkMaterialDO> getBulkMaterialDOList() {
        return bulkMaterialDOList;
    }

    public void setBulkMaterialDOList(List<BulkMaterialDO> bulkMaterialDOList) {
        this.bulkMaterialDOList = bulkMaterialDOList;
    }
}
