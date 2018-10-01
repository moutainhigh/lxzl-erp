package com.lxzl.erp.common.domain.statement.pojo.dto.rent;

import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderProduct;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailReletProductDTO extends CheckStatementDetailReletDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        ReletOrderProduct reletOrderProduct = super.getReletOrderProductById(super.getReletOrderItemReferId());
        OrderProduct orderProduct = super.getOrderProductById(super.getOrderItemReferId());
        setItemName(reletOrderProduct.getProductName());
        setItemSkuName(reletOrderProduct.getProductSkuName());
        setIsNew(reletOrderProduct.getIsNewProduct());
        setPayMode(reletOrderProduct.getPayMode());
        setItemCount(reletOrderProduct.getRentingProductCount());
        setUnitAmount(reletOrderProduct.getProductUnitAmount());

        setItemRentType(orderProduct.getRentType());
        setDepositCycle(orderProduct.getDepositCycle());
        setPaymentCycle(orderProduct.getPaymentCycle());
        setOrderItemActualId(orderProduct.getOrderProductId());

        return this;
    }

    @Override
    public String getCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {
        ReletOrderProduct reletOrderProduct = super.getReletOrderProductById(super.getReletOrderItemReferId());
        String cacheKey = reletOrderProduct.getReletOrderId() + "_" + this.getReletOrderItemReferId() + "_" + this.getOrderItemType();
        cacheKey = cacheKey + "_" + DateFormatUtils.format(getStatementExpectPayTime(), "yyyyMMdd");
        return cacheKey;
    }

    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.PRODUCT_RELET;
    }

}
