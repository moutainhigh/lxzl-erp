package com.lxzl.erp.common.domain.warehouse.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-02 15:45
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Warehouse implements Serializable {
    private Integer warehouseId;

    private String warehouseNo;

    private String warehouseName;

    private Integer warehouseType;

    private Integer subCompanyId;

    private String subCompanyName;

    private Integer subCompanyType;

    private Integer dataStatus;

    private String remark;

    private List<WarehousePosition> warehousePositionList;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
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

    public List<WarehousePosition> getWarehousePositionList() {
        return warehousePositionList;
    }

    public void setWarehousePositionList(List<WarehousePosition> warehousePositionList) {
        this.warehousePositionList = warehousePositionList;
    }

    public Integer getSubCompanyType() {
        return subCompanyType;
    }

    public void setSubCompanyType(Integer subCompanyType) {
        this.subCompanyType = subCompanyType;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public Integer getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(Integer warehouseType) {
        this.warehouseType = warehouseType;
    }
}
