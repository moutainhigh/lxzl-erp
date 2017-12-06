package com.lxzl.erp.common.domain.deploymentOrder;

import java.io.Serializable;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-28 15:18
 */
public class ProcessDeploymentOrderParam implements Serializable {
    private String deploymentOrderNo;
    private String equipmentNo;
    private List<String> bulkMaterialNoList;
    private Integer operationType;

    public String getDeploymentOrderNo() {
        return deploymentOrderNo;
    }

    public void setDeploymentOrderNo(String deploymentOrderNo) {
        this.deploymentOrderNo = deploymentOrderNo;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public List<String> getBulkMaterialNoList() {
        return bulkMaterialNoList;
    }

    public void setBulkMaterialNoList(List<String> bulkMaterialNoList) {
        this.bulkMaterialNoList = bulkMaterialNoList;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }
}
