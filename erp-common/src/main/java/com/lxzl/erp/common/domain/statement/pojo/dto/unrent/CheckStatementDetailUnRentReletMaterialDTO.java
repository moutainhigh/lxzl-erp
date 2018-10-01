package com.lxzl.erp.common.domain.statement.pojo.dto.unrent;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderMaterial;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailUnRentReletMaterialDTO extends CheckStatementDetailUnRentReletDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        ReletOrderMaterial reletOrderMaterial = super.getReletOrderMaterialById(super.getReletOrderItemReferId());
        K3ReturnOrderDetail k3ReturnOrderDetail = super.getK3ReturnOrderDetailById(super.getOrderItemReferId());
        OrderMaterial orderMaterial = super.getOrderMaterialById(Integer.valueOf(k3ReturnOrderDetail.getOrderItemId()));
        if (orderMaterial == null) {
            for (OrderMaterial orderMaterial1 : super.mapContainer.getIdOrderMaterialMap().values()) {
                if (orderMaterial1.getOrderId().equals(super.getOrderOriginalId()) && StringUtils.equals(k3ReturnOrderDetail.getOrderEntry(), orderMaterial1.getFEntryID().toString())) {
                    orderMaterial = orderMaterial1;
                }
            }
        }
        setOrderItemActualId(Integer.valueOf(orderMaterial.getOrderMaterialId()));
        setOrderNo(k3ReturnOrderDetail.getOrderNo());
        setItemName(reletOrderMaterial.getMaterialName());
        setItemSkuName(reletOrderMaterial.getMaterialName());
        setIsNew(reletOrderMaterial.getIsNewMaterial());
        setPayMode(reletOrderMaterial.getPayMode());
        setUnitAmount(reletOrderMaterial.getMaterialUnitAmount());

        setItemRentType(orderMaterial.getRentType());
        setItemCount(CommonConstant.COMMON_ZERO - k3ReturnOrderDetail.getRealProductCount());
        setReturnReferId(super.getReturnReferId());
        return this;
    }

    @Override
    protected String doGetNoThisMonthCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {
        ReletOrderMaterial reletOrderMaterial = super.getReletOrderMaterialById(super.getReletOrderItemReferId());
        String cacheKey = reletOrderMaterial.getReletOrderId() + "_" + this.getReletOrderItemReferId() + "_" + this.getOrderOriginalItemType(statementStatisticsDTO);
        if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(super.getPayMode())) {
            cacheKey = cacheKey + "_" + DateFormatUtils.format(super.getStatementExpectPayTime(), "yyyyMMdd");
        }
        return cacheKey;
    }

    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.MATERIAL_RELET_RETURN;
    }

    @Override
    public boolean isAddTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
        return checkIsAddTheMonth(statementStatisticsDTO,this.getReturnTime(),this.getStatementStartTime());
    }

}
