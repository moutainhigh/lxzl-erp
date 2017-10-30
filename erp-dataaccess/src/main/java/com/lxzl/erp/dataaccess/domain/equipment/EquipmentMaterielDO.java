package com.lxzl.erp.dataaccess.domain.equipment;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-30 20:16
 */
public class EquipmentMaterielDO extends BaseDO {
    private Integer id;

    private Long equipmentId;

    private Long materielId;

    private Integer materielCount;

    private Integer dataStatus;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getMaterielId() {
        return materielId;
    }

    public void setMaterielId(Long materielId) {
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
