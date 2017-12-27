package com.lxzl.erp.common.domain.purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseReceiveOrderMaterial;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.purchaseOrder.UpdateReceiveRemarkGroup;
import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateReceiveMaterialRemarkParam {

    @NotBlank(message = ErrorCode.PURCHASE_RECEIVE_ORDER_NO_NOT_NULL,groups = {IdGroup.class})
    private String purchaseReceiveOrderNo;
    @Valid
    @CollectionNotNull(message = ErrorCode.RECORD_NOT_EXISTS,groups = {IdGroup.class})
    private List<PurchaseReceiveOrderMaterial> purchaseReceiveOrderMaterialList;

    public String getPurchaseReceiveOrderNo() {
        return purchaseReceiveOrderNo;
    }

    public void setPurchaseReceiveOrderNo(String purchaseReceiveOrderNo) {
        this.purchaseReceiveOrderNo = purchaseReceiveOrderNo;
    }

    public List<PurchaseReceiveOrderMaterial> getPurchaseReceiveOrderMaterialList() {
        return purchaseReceiveOrderMaterialList;
    }

    public void setPurchaseReceiveOrderMaterialList(List<PurchaseReceiveOrderMaterial> purchaseReceiveOrderMaterialList) {
        this.purchaseReceiveOrderMaterialList = purchaseReceiveOrderMaterialList;
    }
}
