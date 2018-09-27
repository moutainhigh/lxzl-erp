package com.lxzl.erp.common.domain.statement.pojo.dto.replaceRent;

import com.lxzl.erp.common.constant.OrderItemType;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.constant.OrderType;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author kai
 * @create 2018-09-25 10:40
 */
public class BaseCheckStatementDetailReplaceDTO extends BaseCheckStatementDetailDTO {

    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        ReplaceOrder replaceOrder = getReplaceOrderId(getSourceId());
        super.setOrderOriginalId(super.getOrderId());
        super.setOrderNo(replaceOrder.getOrderNo());
        super.setOrderRentStartTime(replaceOrder.getOrderRentStartTime());
        super.setOrderExpectReturnTime(replaceOrder.getOrderExpectReturnTime());
        super.setOrderItemActualId(super.getOrderItemReferId());
        super.setReturnOrderNo(replaceOrder.getReplaceOrderNo());
        super.setReplaceTime(replaceOrder.getRealReplaceTime());
        super.setReturnReasonType(replaceOrder.getReplaceReasonType());
        return this;
    }


    @Override
    public boolean isAddTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
//        String replaceMonth = DateFormatUtils.format(this.getReplaceTime(), "yyyy-MM");
//        if (statementStatisticsDTO.getMonth().equals(replaceMonth)) {
//            return true;
//        }
        boolean statementEndTimeFlag = (this.getStatementEndTime().getTime() < statementStatisticsDTO.getMonthStartTime());
        boolean statementStartTimeFlag = (this.getStatementStartTime().getTime() > statementStatisticsDTO.getMonthEndTime());
        if (!(statementStartTimeFlag || statementEndTimeFlag)) {
            return true;
        }
        String month = DateFormatUtils.format(this.getStatementExpectPayTime(), "yyyy-MM");
        if (statementStatisticsDTO.getMonth().equals(month)) {
            return true;
        }
        return false;
    }

    @Override
    public void mergeToTarget(BaseCheckStatementDetailDTO targetDetail, CheckStatementStatisticsDTO statementStatisticsDTO) {
        // 合并数量
//        Long statementExpectPayTime = this.getStatementExpectPayTime().getTime();
//        if (statementStatisticsDTO.getMonthEndTime() >= statementExpectPayTime) {
            if (this.getStatementStartTime().getTime() == targetDetail.getStatementStartTime().getTime() && this.getStatementEndTime().getTime() == targetDetail.getStatementEndTime().getTime()) {
                targetDetail.setItemCount(this.getItemCount() + targetDetail.getItemCount());
                // 合并金额
                super.mergeAmountToTarget(targetDetail);
            }
//        }
        // 合并数量
//        Long statementExpectPayTime = this.getStatementExpectPayTime().getTime();
//        if (targetDetail.getStatementExpectPayTime().getTime() == statementExpectPayTime) {
//            targetDetail.setItemCount(this.getItemCount() + targetDetail.getItemCount());
//            // 合并金额
//            super.mergeAmountToTarget(targetDetail);
//        }
    }

    protected String doGetNoThisMonthCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {
        String cacheKey = this.getOrderOriginalId() + "_" + this.getOrderItemActualId() + "_" + this.getOrderItemType();
        if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(super.getPayMode())) {
            cacheKey = cacheKey + "_" + DateFormatUtils.format(super.getStatementExpectPayTime(), "yyyyMMdd");
        }
        return cacheKey;
    }

    @Override
    public boolean isMergeReturnOrder(CheckStatementStatisticsDTO statementStatisticsDTO) {
        if (this.getReturnTime() == null) {
            Integer orderType = this.getOrderType();
            Integer orderItemType = this.getOrderItemType();

            if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(super.getPayMode())) {
                return false;
            }
            if (OrderType.ORDER_TYPE_REPLACE.equals(orderType) && (OrderItemType.ORDER_ITEM_TYPE_PRODUCT.equals(orderItemType) || OrderItemType.ORDER_ITEM_TYPE_MATERIAL.equals(orderItemType))) {
                return true;
            }
        }
        String detailMonth = DateFormatUtils.format(this.getReturnTime(), "yyyy-MM");
        if (detailMonth.equals(statementStatisticsDTO.getMonth())) {
            return false;
        }
        return true;
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
        if (super.getReturnTime() != null) {
            String replaceTimeStr = DateFormatUtils.format(super.getReturnTime(), "yyyy-MM");
            if (!statementStatisticsDTO.getMonth().equals(replaceTimeStr)) {
                return false;
            }
        }
        return super.isShowTheMonth(statementStatisticsDTO);
    }
}
