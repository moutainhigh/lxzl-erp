package com.lxzl.erp.common.domain.statement.pojo.dto.replaceRent;

import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderProduct;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;

public class CheckStatementDetailReplaceProductDTO extends CheckStatementDetailReplaceDTO {

    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        ReplaceOrderProduct replaceOrderProduct = super.getReplaceOrderProductById(super.getReplaceItemId());
        setItemName(replaceOrderProduct.getProductName());
        setItemSkuName(replaceOrderProduct.getProductSkuName());
        setIsNew(replaceOrderProduct.getIsNewProduct());
        setPayMode(replaceOrderProduct.getPayMode());
        setItemCount(replaceOrderProduct.getRealReplaceProductCount());
        setItemRentType(replaceOrderProduct.getRentType());
        setDepositCycle(replaceOrderProduct.getDepositCycle());
        setPaymentCycle(replaceOrderProduct.getPaymentCycle());
        setUnitAmount(replaceOrderProduct.getProductUnitAmount());
        setOrderItemActualId(replaceOrderProduct.getNewOrderProductId());
        return this;
    }
    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.PRODUCT_REPLACE;
    }

}
