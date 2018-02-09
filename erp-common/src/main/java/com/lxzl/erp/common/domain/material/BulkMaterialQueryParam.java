package com.lxzl.erp.common.domain.material;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

    private Integer isNew;

    //控制数据权限
    private List<Integer> passiveUserIdList;//控制数据权限
    private List<Integer> warehouseIdList;//控制数据权限
    private Integer subCompanyId;


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

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public List<Integer> getPassiveUserIdList() { return passiveUserIdList; }

    public void setPassiveUserIdList(List<Integer> passiveUserIdList) { this.passiveUserIdList = passiveUserIdList; }

    public Integer getSubCompanyId() { return subCompanyId; }

    public void setSubCompanyId(Integer subCompanyId) { this.subCompanyId = subCompanyId; }

    public List<Integer> getWarehouseIdList() { return warehouseIdList; }

    public void setWarehouseIdList(List<Integer> warehouseIdList) { this.warehouseIdList = warehouseIdList; }
}
