package com.lxzl.erp.common.domain.statement.pojo.dto.rent;

import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.order.pojo.OrderMaterial;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderMaterial;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailReletMaterialDTO extends CheckStatementDetailReletDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        ReletOrderMaterial reletOrderMaterial = super.getReletOrderMaterialById(super.getReletOrderItemReferId());
        OrderMaterial orderMaterial = super.getOrderMaterialById(super.getOrderItemReferId());
        setItemName(reletOrderMaterial.getMaterialName());
        setItemSkuName(reletOrderMaterial.getMaterialName());
        setIsNew(reletOrderMaterial.getIsNewMaterial());
        setPayMode(reletOrderMaterial.getPayMode());
        setItemCount(reletOrderMaterial.getRentingMaterialCount());
        setUnitAmount(reletOrderMaterial.getMaterialUnitAmount());

        setItemRentType(orderMaterial.getRentType());
        setDepositCycle(orderMaterial.getDepositCycle());
        setPaymentCycle(orderMaterial.getPaymentCycle());
        setOrderItemActualId(orderMaterial.getOrderMaterialId());
        return this;
    }

    @Override
    public String getCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {
        ReletOrderMaterial reletOrderMaterial = super.getReletOrderMaterialById(super.getReletOrderItemReferId());
        String cacheKey = reletOrderMaterial.getReletOrderId() + "_" + this.getReletOrderItemReferId() + "_" + this.getOrderItemType() ;
        if(getStatementExpectPayTime() != null){
            cacheKey = cacheKey + "_" + DateFormatUtils.format(getStatementExpectPayTime(), "yyyyMMdd");
        }
        return cacheKey;
    }

    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.MATERIAL_RELET;
    }

}
