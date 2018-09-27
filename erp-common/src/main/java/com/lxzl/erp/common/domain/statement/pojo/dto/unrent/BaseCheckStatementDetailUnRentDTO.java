package com.lxzl.erp.common.domain.statement.pojo.dto.unrent;

import com.lxzl.erp.common.constant.OrderItemType;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderDetail;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author daiqi
 * @create 2018-07-09 9:56
 */
public class BaseCheckStatementDetailUnRentDTO extends BaseCheckStatementDetailDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        K3ReturnOrder k3ReturnOrder = getK3ReturnOrderById(getOrderId());
        K3ReturnOrderDetail k3ReturnOrderDetail = getK3ReturnOrderDetailById(getOrderItemReferId());
        if (k3ReturnOrderDetail != null) {
            Order order = getOrderByNo(k3ReturnOrderDetail.getOrderNo());
            super.setOrderOriginalId(order.getOrderId());
        }
        super.setOrderItemActualId(super.getOrderItemReferId());
        super.setOrderRentStartTime(k3ReturnOrder.getReturnTime());
        super.setOrderExpectReturnTime(k3ReturnOrder.getReturnTime());
        super.setReturnTime(k3ReturnOrder.getReturnTime());
        super.setReturnOrderNo(k3ReturnOrder.getReturnOrderNo());
        super.setReturnReasonType(k3ReturnOrder.getReturnReasonType());
        return this;
    }

    @Override
    public boolean isAddTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
        boolean returnTimeFlag = super.getReturnTime().getTime() <= statementStatisticsDTO.getMonthEndTime();
        boolean endTimeFlag = super.getStatementEndTime().getTime() >= statementStatisticsDTO.getMonthStartTime();
        if (returnTimeFlag && endTimeFlag) {
            return true;
        }
        if (statementStatisticsDTO.getMonth().equals(DateFormatUtils.format(this.getStatementExpectPayTime(), "yyyy-MM"))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isMergeReturnOrder(CheckStatementStatisticsDTO statementStatisticsDTO) {
        String detailMonth = DateFormatUtils.format(this.getReturnTime(), "yyyy-MM");
        if (detailMonth.equals(statementStatisticsDTO.getMonth())) {
            return false;
        }
        return true;
    }

    @Override
    public void mergeToTarget(BaseCheckStatementDetailDTO targetDetail, CheckStatementStatisticsDTO statementStatisticsDTO) {
        // 合并数量与金额
        if(this.getStatementStartTime().getTime() == targetDetail.getStatementStartTime().getTime() && this.getStatementEndTime().getTime() == targetDetail.getStatementEndTime().getTime()){
            targetDetail.setItemCount(this.getItemCount() + targetDetail.getItemCount());
            super.mergeAmountToTarget(targetDetail);
        }
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
        String cacheKey = this.getOrderOriginalId() + "_" + this.getOrderItemActualId() + "_" + this.getOrderItemType();
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
    public Integer getOrderOriginalItemType(CheckStatementStatisticsDTO statementStatisticsDTO) {
        Integer orderOriginalItemType = this.getOrderItemType();
        String detailMonth = DateFormatUtils.format(this.getReturnTime(), "yyyy-MM");
        if (!detailMonth.equals(statementStatisticsDTO.getMonth())) {
            if (OrderItemType.ORDER_ITEM_TYPE_RETURN_PRODUCT.equals(this.getOrderItemType())) {
                orderOriginalItemType = OrderItemType.ORDER_ITEM_TYPE_PRODUCT;
            } else if (OrderItemType.ORDER_ITEM_TYPE_RETURN_MATERIAL.equals(this.getOrderItemType())) {
                orderOriginalItemType = OrderItemType.ORDER_ITEM_TYPE_MATERIAL;
            }
        }
        return orderOriginalItemType;
    }

    @Override
    public boolean isShowTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
        String returnTimeStr = DateFormatUtils.format(super.getReturnTime(), "yyyy-MM");
        if (!statementStatisticsDTO.getMonth().equals(returnTimeStr)) {
            return false;
        }
        return super.isShowTheMonth(statementStatisticsDTO);
    }
}
