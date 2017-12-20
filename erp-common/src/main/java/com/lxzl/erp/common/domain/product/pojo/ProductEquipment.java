package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEquipment implements Serializable {
    private Integer productEquipmentId; //设备ID
    private String equipmentNo; //设备编号唯一
    private Integer productId;  //所属产品ID
    private Integer skuId;  //所属SKU ID
    private String orderNo;  //关联订单号，租赁中状态时有值
    private Integer currentWarehouseId;  //目前仓库ID
    private Integer currentWarehousePositionId;  //目前仓位ID
    private Integer ownerWarehouseId;  //归属仓库ID
    private Integer ownerWarehousePositionId;  //归属目前仓位ID
    private BigDecimal equipmentPrice;  //设备本身价值
    private Integer equipmentStatus;  //设备状态，1设备空闲，2租赁中，3维修中，4报废，5调拨中
    private Integer isNew;  //是否全新，1是，0否
    private Integer dataStatus;  //状态：0不可用；1可用；2删除
    private String remark;  //备注
    private List<ProductImg> productImgList;           // 商品图片
    private List<BulkMaterial> bulkMaterialList;        // 设备散料列表

    private String ownerWarehouseName;  //归属仓库名称
    private String currentWarehouseName;  //目前仓库名称
    private String productName;  //商品名称

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public String getCurrentWarehouseName() {
        return currentWarehouseName;
    }

    public void setCurrentWarehouseName(String currentWarehouseName) {
        this.currentWarehouseName = currentWarehouseName;
    }

    public String getOwnerWarehouseName() {
        return ownerWarehouseName;
    }

    public void setOwnerWarehouseName(String ownerWarehouseName) {
        this.ownerWarehouseName = ownerWarehouseName;
    }
}
