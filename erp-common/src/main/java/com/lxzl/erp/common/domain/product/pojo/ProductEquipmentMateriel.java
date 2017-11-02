package com.lxzl.erp.common.domain.product.pojo;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-30 20:16
 */
public class ProductEquipmentMateriel implements Serializable {
    private Integer id;

    private Integer equipmentId;

    private Integer materielId;

    private Integer materielCount;

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

    public Integer getMaterielId() {
        return materielId;
    }

    public void setMaterielId(Integer materielId) {
        this.materielId = materielId;
    }

    public Integer getMaterielCount() {
        return materielCount;
    }

    public void setMaterielCount(Integer materielCount) {
        this.materielCount = materielCount;
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
