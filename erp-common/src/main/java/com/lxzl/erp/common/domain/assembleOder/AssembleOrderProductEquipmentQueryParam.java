package com.lxzl.erp.common.domain.assembleOder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;

/**
 * User : XiaoLuYu
 * Date : Created in ${Date}
 * Time : Created in ${Time}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssembleOrderProductEquipmentQueryParam extends BasePO {
    private Integer assembleOrderId;

    public Integer getAssembleOrderId() {
        return assembleOrderId;
    }

    public void setAssembleOrderId(Integer assembleOrderId) {
        this.assembleOrderId = assembleOrderId;
    }
}
