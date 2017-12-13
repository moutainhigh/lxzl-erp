package com.lxzl.erp.common.domain.material;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-13 13:55
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkMaterialQueryParam extends BasePageParam {
    private Integer materialId;
    private String bulkMaterialNo;
    private String bulkMaterialName;
    private Integer bulkMaterialType;
    private Date createStartTime;
    private Date createEndTime;
    private Integer currentWarehouseId;
    private String orderNo;
    private Integer bulkMaterialStatus;

    private Integer bulkMaterialId;
    private String currentEquipmentNo;
    private Integer isOnEquipment;


    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getBulkMaterialNo() {
        return bulkMaterialNo;
    }

    public void setBulkMaterialNo(String bulkMaterialNo) {
        this.bulkMaterialNo = bulkMaterialNo;
    }

    public String getBulkMaterialName() {
        return bulkMaterialName;
    }

    public void setBulkMaterialName(String bulkMaterialName) {
        this.bulkMaterialName = bulkMaterialName;
    }

    public Integer getBulkMaterialType() {
        return bulkMaterialType;
    }

    public void setBulkMaterialType(Integer bulkMaterialType) {
        this.bulkMaterialType = bulkMaterialType;
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

    public Integer getBulkMaterialStatus() {
        return bulkMaterialStatus;
    }

    public void setBulkMaterialStatus(Integer bulkMaterialStatus) {
        this.bulkMaterialStatus = bulkMaterialStatus;
    }

    public String getCurrentEquipmentNo() {
        return currentEquipmentNo;
    }

    public void setCurrentEquipmentNo(String currentEquipmentNo) {
        this.currentEquipmentNo = currentEquipmentNo;
    }

    public Integer getBulkMaterialId() {
        return bulkMaterialId;
    }

    public void setBulkMaterialId(Integer bulkMaterialId) {
        this.bulkMaterialId = bulkMaterialId;
    }

    public Integer getIsOnEquipment() {
        return isOnEquipment;
    }

    public void setIsOnEquipment(Integer isOnEquipment) {
        this.isOnEquipment = isOnEquipment;
    }
}
