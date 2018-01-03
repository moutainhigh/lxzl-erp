package com.lxzl.erp.common.domain.purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePageParam;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseReceiveOrderMaterialBulkPageParam extends BasePageParam {
    @NotNull(message = ErrorCode.ID_NOT_NULL)
    private Integer purchaseReceiveOrderMaterialId;
    private Integer itemReferType;

    public Integer getPurchaseReceiveOrderMaterialId() {
        return purchaseReceiveOrderMaterialId;
    }

    public void setPurchaseReceiveOrderMaterialId(Integer purchaseReceiveOrderMaterialId) {
        this.purchaseReceiveOrderMaterialId = purchaseReceiveOrderMaterialId;
    }

    public Integer getItemReferType() {
        return itemReferType;
    }

    public void setItemReferType(Integer itemReferType) {
        this.itemReferType = itemReferType;
    }
}
