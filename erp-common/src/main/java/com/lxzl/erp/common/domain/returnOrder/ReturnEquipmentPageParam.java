package com.lxzl.erp.common.domain.returnOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;
import com.lxzl.erp.common.domain.validGroup.IdGroup;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnEquipmentPageParam extends BasePageParam{
    @NotNull(message = ErrorCode.RETURN_ORDER_PRODUCT_ID_NOT_NULL,groups = {IdGroup.class})
    private Integer returnOrderProductId;

    public Integer getReturnOrderProductId() {
        return returnOrderProductId;
    }

    public void setReturnOrderProductId(Integer returnOrderProductId) {
        this.returnOrderProductId = returnOrderProductId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}

