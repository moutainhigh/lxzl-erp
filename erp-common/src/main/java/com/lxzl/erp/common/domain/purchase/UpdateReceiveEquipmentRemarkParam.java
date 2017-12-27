package com.lxzl.erp.common.domain.purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.validGroup.purchaseOrder.UpdateReceiveRemarkGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateReceiveEquipmentRemarkParam {

    @NotNull(message = ErrorCode.PURCHASE_RECEIVE_ORDER_NO_NOT_NULL, groups = {UpdateReceiveRemarkGroup.class})
    private String purchaseReceiveOrderNo;
    @Valid
    private ProductEquipment productEquipment;

    public String getPurchaseReceiveOrderNo() {
        return purchaseReceiveOrderNo;
    }

    public void setPurchaseReceiveOrderNo(String purchaseReceiveOrderNo) {
        this.purchaseReceiveOrderNo = purchaseReceiveOrderNo;
    }

    public ProductEquipment getProductEquipment() {
        return productEquipment;
    }

    public void setProductEquipment(ProductEquipment productEquipment) {
        this.productEquipment = productEquipment;
    }
}
