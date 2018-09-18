package com.lxzl.erp.common.domain.statement.pojo.dto.unrent;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderProduct;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.StringUtils;

/**
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailUnRentReletProductDTO extends CheckStatementDetailUnRentReletDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        ReletOrderProduct reletOrderProduct = super.getReletOrderProductById(super.getReletOrderItemReferId());
        K3ReturnOrderDetail k3ReturnOrderDetail = super.getK3ReturnOrderDetailById(super.getOrderItemReferId());
        OrderProduct orderProduct = super.getOrderProductById(Integer.valueOf(k3ReturnOrderDetail.getOrderItemId()));
        if (orderProduct == null) {
            for (OrderProduct orderProduct1 : super.mapContainer.getIdOrderProductMap().values()) {
                if (orderProduct1.getOrderId().equals(super.getOrderOriginalId()) && StringUtils.equals(k3ReturnOrderDetail.getOrderEntry(), orderProduct1.getFEntryID().toString())) {
                    orderProduct = orderProduct1;
                }
            }
        }
        setOrderItemActualId(Integer.valueOf(orderProduct.getOrderProductId()));
        setOrderNo(k3ReturnOrderDetail.getOrderNo());
        setItemName(reletOrderProduct.getProductName());
        setItemSkuName(reletOrderProduct.getProductSkuName());
        setIsNew(reletOrderProduct.getIsNewProduct());
        setPayMode(reletOrderProduct.getPayMode());
        setUnitAmount(reletOrderProduct.getProductUnitAmount());

        setItemRentType(orderProduct.getRentType());
        setItemCount(CommonConstant.COMMON_ZERO - k3ReturnOrderDetail.getRealProductCount());
        return this;
    }

    @Override
    protected String doGetNoThisMonthCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {
        ReletOrderProduct reletOrderProduct = super.getReletOrderProductById(super.getReletOrderItemReferId());
        String cacheKey = reletOrderProduct.getReletOrderId() + "_" + this.getReletOrderItemReferId() + "_" + this.getOrderOriginalItemType(statementStatisticsDTO);
        return cacheKey;
    }

    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.PRODUCT_RELET_RETURN;
    }

}
