package com.lxzl.erp.common.domain.warehouse.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehousePosition implements Serializable {
    private Integer warehousePositionId;

    private Integer warehouseId;

    private String warehousePositionName;

    private Integer dataStatus;

    private String remark;

    public Integer getWarehousePositionId() {
        return warehousePositionId;
    }

    public void setWarehousePositionId(Integer warehousePositionId) {
        this.warehousePositionId = warehousePositionId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehousePositionName() {
        return warehousePositionName;
    }

    public void setWarehousePositionName(String warehousePositionName) {
        this.warehousePositionName = warehousePositionName == null ? null : warehousePositionName.trim();
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
        this.remark = remark == null ? null : remark.trim();
    }
}