package com.lxzl.erp.common.domain.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.purchase.PurchaseReceiveOrderMaterialPrice;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateChangeMaterialPriceParam {
    @NotNull(message = ErrorCode.ID_NOT_NULL,groups = {UpdateGroup.class})
    private Integer purchaseReceiveOrderMaterialId;   //唯一标识
    @CollectionNotNull(message = ErrorCode.RECORD_NOT_EXISTS,groups = {UpdateGroup.class})
    @Valid
    private List<PurchaseReceiveOrderMaterialPrice> purchaseReceiveOrderMaterialPriceList;

    public Integer getPurchaseReceiveOrderMaterialId() {
        return purchaseReceiveOrderMaterialId;
    }

    public void setPurchaseReceiveOrderMaterialId(Integer purchaseReceiveOrderMaterialId) {
        this.purchaseReceiveOrderMaterialId = purchaseReceiveOrderMaterialId;
    }

    public List<PurchaseReceiveOrderMaterialPrice> getPurchaseReceiveOrderMaterialPriceList() {
        return purchaseReceiveOrderMaterialPriceList;
    }

    public void setPurchaseReceiveOrderMaterialPriceList(List<PurchaseReceiveOrderMaterialPrice> purchaseReceiveOrderMaterialPriceList) {
        this.purchaseReceiveOrderMaterialPriceList = purchaseReceiveOrderMaterialPriceList;
    }
}
