package com.lxzl.erp.common.domain.statement.pojo.dto.rent;

import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;

/**
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailRentMaterialDTO extends CheckStatementDetailRentDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        OrderMaterial orderMaterial = super.getOrderMaterialById(super.getOrderItemReferId());
        setItemName(orderMaterial.getMaterialName());
        setItemSkuName(orderMaterial.getMaterialName());
        setIsNew(orderMaterial.getIsNewMaterial());
        setPayMode(orderMaterial.getPayMode());
        setItemCount(orderMaterial.getMaterialCount());
        setItemRentType(orderMaterial.getRentType());
        setDepositCycle(orderMaterial.getDepositCycle());
        setPaymentCycle(orderMaterial.getPaymentCycle());
        setUnitAmount(orderMaterial.getMaterialUnitAmount());
        setOrderItemActualId(orderMaterial.getOrderMaterialId());
        return this;
    }

    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.MATERIAL_RENT;
    }
}
