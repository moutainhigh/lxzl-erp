package com.lxzl.erp.common.domain.material;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.se.dataaccess.mysql.config.PageQuery;

import java.io.Serializable;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-13 13:55
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialQueryParam extends BasePageParam {
    private String materialNo;
    private String materialName;
    private Integer materialType;
    private Double materialCapacityValue;
    private Integer materialModelId;
    private Integer isMainMaterial;

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public Integer getIsMainMaterial() {
        return isMainMaterial;
    }

    public void setIsMainMaterial(Integer isMainMaterial) {
        this.isMainMaterial = isMainMaterial;
    }

    public Double getMaterialCapacityValue() {
        return materialCapacityValue;
    }

    public void setMaterialCapacityValue(Double materialCapacityValue) {
        this.materialCapacityValue = materialCapacityValue;
    }

    public Integer getMaterialModelId() {
        return materialModelId;
    }

    public void setMaterialModelId(Integer materialModelId) {
        this.materialModelId = materialModelId;
    }
}
