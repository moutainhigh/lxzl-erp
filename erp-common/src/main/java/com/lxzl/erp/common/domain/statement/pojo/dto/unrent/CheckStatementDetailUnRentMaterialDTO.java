package com.lxzl.erp.common.domain.statement.pojo.dto.unrent;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailUnRentMaterialDTO extends CheckStatementDetailUnRentDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
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
        setItemName(orderMaterial.getMaterialName());
        setItemSkuName(orderMaterial.getMaterialName());
        setIsNew(orderMaterial.getIsNewMaterial());
        setPayMode(orderMaterial.getPayMode());
        setUnitAmount(orderMaterial.getMaterialUnitAmount());
        setItemRentType(orderMaterial.getRentType());

        setItemCount(CommonConstant.COMMON_ZERO - k3ReturnOrderDetail.getRealProductCount());
        return this;
    }

    @Override
    public boolean isAddTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
        String returnTimeStr = DateFormatUtils.format(this.getReturnTime(), "yyyy-MM");
        if (statementStatisticsDTO.getMonth().equals(returnTimeStr)) {
            if (this.getStatementExpectPayTime() == null) {
                return true;
            }
            return false;
        }
        return super.isAddTheMonth(statementStatisticsDTO);

    }

    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.MATERIAL_RETURN;
    }
}
