package com.lxzl.erp.common.domain.order;

import java.io.Serializable;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-29 15:44
 */
public class ProcessOrderParam implements Serializable {
    private String orderNo;
    private String equipmentNo;
    private Integer materialId;
    private Integer materialCount;
    private Integer isNewMaterial;
    private Integer operationType;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
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

    public Integer getIsNewMaterial() {
        return isNewMaterial;
    }

    public void setIsNewMaterial(Integer isNewMaterial) {
        this.isNewMaterial = isNewMaterial;
    }
}
