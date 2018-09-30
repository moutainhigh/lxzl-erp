package com.lxzl.erp.common.domain.statement.pojo.dto.rent;

import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;

/**
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailRentProductDTO extends CheckStatementDetailRentDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        OrderProduct orderProduct = super.getOrderProductById(super.getOrderItemReferId());
        setItemName(orderProduct.getProductName());
        setItemSkuName(orderProduct.getProductSkuName());
        setIsNew(orderProduct.getIsNewProduct());
        setPayMode(orderProduct.getPayMode());
        setItemCount(orderProduct.getProductCount());
        setItemRentType(orderProduct.getRentType());
        setDepositCycle(orderProduct.getDepositCycle());
        setPaymentCycle(orderProduct.getPaymentCycle());
        setUnitAmount(orderProduct.getProductUnitAmount());
        setOrderItemActualId(orderProduct.getOrderProductId());
        return this;
    }
    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.PRODUCT_RENT;
    }

}
