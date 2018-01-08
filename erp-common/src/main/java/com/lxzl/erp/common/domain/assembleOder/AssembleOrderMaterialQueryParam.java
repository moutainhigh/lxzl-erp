package com.lxzl.erp.common.domain.assembleOder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssembleOrderMaterialQueryParam extends BasePO {

    private Integer materialId;

    private Integer assembleOrderId;

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getAssembleOrderId() {
        return assembleOrderId;
    }

    public void setAssembleOrderId(Integer assembleOrderId) {
        this.assembleOrderId = assembleOrderId;
    }
}
