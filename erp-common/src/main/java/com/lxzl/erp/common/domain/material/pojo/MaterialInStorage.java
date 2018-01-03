package com.lxzl.erp.common.domain.material.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-16 15:58
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialInStorage extends BasePO {
    private Integer materialId;
    private Integer materialCount;
    private Integer isNew;
    private Integer itemReferId;
    private Integer itemReferType;

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

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getItemReferId() {
        return itemReferId;
    }

    public void setItemReferId(Integer itemReferId) {
        this.itemReferId = itemReferId;
    }

    public Integer getItemReferType() {
        return itemReferType;
    }

    public void setItemReferType(Integer itemReferType) {
        this.itemReferType = itemReferType;
    }
}
