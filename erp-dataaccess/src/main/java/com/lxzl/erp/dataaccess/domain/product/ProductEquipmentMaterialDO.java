package com.lxzl.erp.dataaccess.domain.product;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-30 20:16
 */
public class ProductEquipmentMaterialDO extends BaseDO {
    private Integer id;

    private Integer equipmentId;

    private Integer materialId;

    private Integer materialCount;

    private Integer dataStatus;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(Integer materialCount) {
        this.materialCount = materialCount;
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
}
