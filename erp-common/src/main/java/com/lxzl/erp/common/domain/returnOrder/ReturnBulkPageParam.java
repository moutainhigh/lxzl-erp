package com.lxzl.erp.common.domain.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.IdGroup;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnBulkPageParam extends BasePageParam{
    @NotNull(message = ErrorCode.RETURN_ORDER_MATERIAL_ID_NOT_NULL,groups = {IdGroup.class})
    private Integer returnOrderMaterialId;

    public Integer getReturnOrderMaterialId() {
        return returnOrderMaterialId;
    }

    public void setReturnOrderMaterialId(Integer returnOrderMaterialId) {
        this.returnOrderMaterialId = returnOrderMaterialId;
    }
}

