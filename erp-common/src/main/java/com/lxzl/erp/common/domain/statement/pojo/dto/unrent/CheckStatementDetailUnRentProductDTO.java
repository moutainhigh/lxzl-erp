package com.lxzl.erp.common.domain.statement.pojo.dto.unrent;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailUnRentProductDTO extends CheckStatementDetailUnRentDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
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
        setItemName(orderProduct.getProductName());
        setItemSkuName(orderProduct.getProductSkuName());
        setIsNew(orderProduct.getIsNewProduct());
        setPayMode(orderProduct.getPayMode());
        setUnitAmount(orderProduct.getProductUnitAmount());

        setItemRentType(orderProduct.getRentType());
        setItemCount(CommonConstant.COMMON_ZERO - k3ReturnOrderDetail.getRealProductCount());
        setReturnReferId(super.getReturnReferId());
        return this;
    }

    @Override
    public boolean isAddTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
        return checkIsAddTheMonth(statementStatisticsDTO,this.getReturnTime(),this.getStatementStartTime());
    }
    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.PRODUCT_RETURN;
    }

}
