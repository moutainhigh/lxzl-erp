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
    private List<String> bulkMaterialNoList;
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
