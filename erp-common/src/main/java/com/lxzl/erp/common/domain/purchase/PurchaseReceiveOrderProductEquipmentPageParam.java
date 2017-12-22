package com.lxzl.erp.common.domain.purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseReceiveOrderProductEquipmentPageParam  extends BasePageParam {

    @NotNull(message = ErrorCode.ID_NOT_NULL)
    private Integer purchaseReceiveOrderProductId;

    public Integer getPurchaseReceiveOrderProductId() {
        return purchaseReceiveOrderProductId;
    }

    public void setPurchaseReceiveOrderProductId(Integer purchaseReceiveOrderProductId) {
        this.purchaseReceiveOrderProductId = purchaseReceiveOrderProductId;
    }
}
