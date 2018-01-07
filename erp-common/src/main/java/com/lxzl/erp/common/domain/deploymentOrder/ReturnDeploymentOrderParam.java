package com.lxzl.erp.common.domain.deploymentOrder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-07 10:09
 */
public class ReturnDeploymentOrderParam implements Serializable {
    private String deploymentOrderNo;
    private Date returnDate;
    private List<String> equipmentNoList;
    private Integer materialId;
    private Integer materialCount;

    public String getDeploymentOrderNo() {
        return deploymentOrderNo;
    }

    public void setDeploymentOrderNo(String deploymentOrderNo) {
        this.deploymentOrderNo = deploymentOrderNo;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public List<String> getEquipmentNoList() {
        return equipmentNoList;
    }

    public void setEquipmentNoList(List<String> equipmentNoList) {
        this.equipmentNoList = equipmentNoList;
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
}
