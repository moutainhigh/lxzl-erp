package com.lxzl.erp.common.domain.statement.pojo.dto.replaceRent;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrderProduct;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrderProduct;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public class CheckStatementDetailUnReplaceProductDTO extends CheckStatementDetailReplaceDTO {

    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        ReplaceOrder replaceOrder = super.getReplaceOrderId(super.getSourceId());
        ReplaceOrderProduct replaceOrderProduct = super.getReplaceOrderProductById(super.getReplaceItemId());
        setOrderItemActualId(replaceOrderProduct.getOldOrderProductId());
        setOrderNo(replaceOrderProduct.getReplaceOrderNo());
        setItemName(replaceOrderProduct.getOldProductName());
        setItemSkuName(replaceOrderProduct.getProductSkuName());
        setIsNew(replaceOrderProduct.getIsNewProduct());
        setPayMode(replaceOrderProduct.getPayMode());
        setUnitAmount(replaceOrderProduct.getOldProductUnitAmount());
        setItemRentType(replaceOrderProduct.getRentType());
        setItemCount(CommonConstant.COMMON_ZERO - replaceOrderProduct.getRealReplaceProductCount());
        setReturnTime(replaceOrder.getRealReplaceTime());
        setReturnReferId(super.getReturnReferId());
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
            ReletOrderProduct reletOrderProduct = super.getReletOrderProductById(super.getReletOrderItemReferId());
            cacheKey = reletOrderProduct.getReletOrderId() + "_" + this.getReletOrderItemReferId() + "_" + this.getOrderOriginalItemType(statementStatisticsDTO);
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

//    @Override
//    public boolean isAddTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
//        return checkIsAddTheMonth(statementStatisticsDTO,this.getReturnTime(),this.getStatementStartTime());
//    }
}
