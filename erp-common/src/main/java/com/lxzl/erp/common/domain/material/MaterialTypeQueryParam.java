package com.lxzl.erp.common.domain.material;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-01-20 10:54
 */
public class MaterialTypeQueryParam extends BasePageParam {
    private Integer materialTypeId;
    private String materialTypeName;
    private Integer isMainMaterial;
    private Integer isCapacityMaterial;

    public Integer getMaterialTypeId() {
        return materialTypeId;
    }

    public void setMaterialTypeId(Integer materialTypeId) {
        this.materialTypeId = materialTypeId;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    public Integer getIsMainMaterial() {
        return isMainMaterial;
    }

    public void setIsMainMaterial(Integer isMainMaterial) {
        this.isMainMaterial = isMainMaterial;
    }

    public Integer getIsCapacityMaterial() {
        return isCapacityMaterial;
    }

    public void setIsCapacityMaterial(Integer isCapacityMaterial) {
        this.isCapacityMaterial = isCapacityMaterial;
    }
}
