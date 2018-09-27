package com.lxzl.erp.common.domain.statement.pojo.dto.replaceRent;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.constant.SortOrderItemType;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderMaterial;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderMaterial;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.time.DateFormatUtils;

public class CheckStatementDetailUnReplaceMaterialDTO extends CheckStatementDetailReplaceDTO {

    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        ReplaceOrder replaceOrder = super.getReplaceOrderId(super.getSourceId());
        ReplaceOrderMaterial replaceOrderMaterial = super.getReplaceOrderMaterialById(super.getReplaceItemId());
        setOrderItemActualId(replaceOrderMaterial.getOldOrderMaterialId());
        setOrderNo(replaceOrderMaterial.getReplaceOrderNo());
        //todo 没旧物料名称字段
        setItemName(replaceOrderMaterial.getMaterialName());
        setIsNew(replaceOrderMaterial.getIsNewMaterial());
        setPayMode(replaceOrderMaterial.getPayMode());
        //todo 没旧物料金额字段
        setUnitAmount(replaceOrderMaterial.getOldMaterialUnitAmount());

        setItemRentType(replaceOrderMaterial.getRentType());
        setItemCount(CommonConstant.COMMON_ZERO - replaceOrderMaterial.getRealReplaceMaterialCount());
        setReturnReferId(super.getReturnReferId());
        setReturnTime(replaceOrder.getRealReplaceTime());
        return this;
    }

    @Override
    public Integer getSortItemType() {
        return SortOrderItemType.PRODUCT_REPLACE_RETURN;
    }

    @Override
    public final String getCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {
        String detailMonth = DateFormatUtils.format(this.getReturnTime(), "yyyy-MM");
        if (!detailMonth.equals(statementStatisticsDTO.getMonth())) {
            return doGetNoThisMonthCacheKey(statementStatisticsDTO);
        } else {
            return doGetThisMonthCacheKey(statementStatisticsDTO);
        }
    }


    protected String doGetNoThisMonthCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {
        String cacheKey;
        if(this.getReletOrderItemReferId() == null){
            cacheKey = this.getOrderOriginalId() + "_" + this.getOrderItemActualId() + "_" + this.getOrderOriginalItemType(statementStatisticsDTO);
        }else {
            ReletOrderMaterial reletOrderMaterial = super.getReletOrderMaterialById(super.getReletOrderItemReferId());
            cacheKey = reletOrderMaterial.getReletOrderId() + "_" + this.getReletOrderItemReferId() + "_" + this.getOrderOriginalItemType(statementStatisticsDTO);
        }
        if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(super.getPayMode())) {
            cacheKey = cacheKey + "_" + DateFormatUtils.format(super.getStatementExpectPayTime(), "yyyyMMdd");
        }
        return cacheKey;
    }

    protected String doGetThisMonthCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {
        String cacheKey = this.getStatementOrderDetailId() + "_" + this.getOrderItemActualId() + "_" + getOrderOriginalItemType(statementStatisticsDTO);
        return cacheKey;
    }

    @Override
    public boolean isAddTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
        return checkIsAddTheMonth(statementStatisticsDTO,this.getReturnTime(),this.getStatementStartTime());
    }
}
