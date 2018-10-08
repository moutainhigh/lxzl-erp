package com.lxzl.erp.common.domain.statement.pojo.dto.replaceRent;

import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderMaterial;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;

public class CheckStatementDetailReplaceMaterialDTO extends CheckStatementDetailReplaceDTO {

    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        ReplaceOrderMaterial replaceOrderMaterial = super.getReplaceOrderMaterialById(super.getOrderItemReferId());
        setItemName(replaceOrderMaterial.getMaterialName());
        setItemSkuName(replaceOrderMaterial.getMaterialName());
        setIsNew(replaceOrderMaterial.getIsNewMaterial());
        setPayMode(replaceOrderMaterial.getPayMode());
        setItemCount(replaceOrderMaterial.getRealReplaceMaterialCount());
        setItemRentType(replaceOrderMaterial.getRentType());
        setDepositCycle(replaceOrderMaterial.getDepositCycle());
        setPaymentCycle(replaceOrderMaterial.getPaymentCycle());
        setUnitAmount(replaceOrderMaterial.getMaterialUnitAmount());
        setOrderItemActualId(replaceOrderMaterial.getNewOrderMaterialId());
        return this;
    }

    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.MATERIAL_REPLACE;
    }
}
