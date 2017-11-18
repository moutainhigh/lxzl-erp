package com.lxzl.erp.common.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-18 13:26
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEquipmentBulkMaterialQueryParam  extends BasePageParam {
    private String equipmentNo;
    private Integer equipmentId;

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }
}
