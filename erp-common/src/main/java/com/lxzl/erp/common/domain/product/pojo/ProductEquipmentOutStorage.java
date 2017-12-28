package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEquipmentOutStorage extends BasePO {

    private Integer productEquipmentId;
    private Integer productEquipmentCount;

    public Integer getProductEquipmentId() {
        return productEquipmentId;
    }

    public void setProductEquipmentId(Integer productEquipmentId) {
        this.productEquipmentId = productEquipmentId;
    }

    public Integer getProductEquipmentCount() {
        return productEquipmentCount;
    }

    public void setProductEquipmentCount(Integer productEquipmentCount) {
        this.productEquipmentCount = productEquipmentCount;
    }
}
