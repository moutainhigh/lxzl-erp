package com.lxzl.erp.common.domain.changeOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateChangeEquipmentPriceParam {

    @NotNull(message = ErrorCode.PURCHASE_RECEIVE_ORDER_NO_NOT_NULL, groups = {ExtendGroup.class})
    private String purchaseReceiveOrderNo;
    @Valid
    private List<ProductEquipment> equipmentList;

    public List<ProductEquipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<ProductEquipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public String getPurchaseReceiveOrderNo() {
        return purchaseReceiveOrderNo;
    }

    public void setPurchaseReceiveOrderNo(String purchaseReceiveOrderNo) {
        this.purchaseReceiveOrderNo = purchaseReceiveOrderNo;
    }
}
