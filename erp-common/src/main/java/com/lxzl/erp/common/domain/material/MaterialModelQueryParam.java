package com.lxzl.erp.common.domain.material;

import com.lxzl.erp.common.domain.base.BasePageParam;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-23 16:01
 */
public class MaterialModelQueryParam extends BasePageParam {
    private Integer materialType;
    private String modelName;

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
