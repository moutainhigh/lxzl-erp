package com.lxzl.erp.common.domain.statement.pojo.dto.replaceRent;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.OrderItemType;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.constant.OrderType;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.replace.pojo.ReplaceOrder;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect;
import com.lxzl.erp.common.util.BigDecimalUtil;
import org.apache.commons.lang.time.DateFormatUtils;

import java.math.BigDecimal;

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
        Order order = getOrderById(replaceOrder.getOrderId());
        if(CommonConstant.COMMON_CONSTANT_YES.equals(order.getIsOriginalOrder())){
            super.setOrderNo(order.getOrderNo());
        }else {
            super.setOrderNo(order.getOriginalOrderNo());
        }
        super.setOrderRentStartTime(replaceOrder.getOrderRentStartTime());
        super.setOrderExpectReturnTime(replaceOrder.getOrderExpectReturnTime());
        super.setOrderItemActualId(super.getOrderItemReferId());
        super.setReturnOrderNo(replaceOrder.getReplaceOrderNo());
        super.setReplaceTime(replaceOrder.getRealReplaceTime());
        super.setReturnReasonType(replaceOrder.getReplaceReasonType());

        //检查是否有冲正单构建编号与原因
        checkStatementDetailCorrect();
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
        if (!(this.getOrderItemType().equals(targetDetail.getOrderItemType()) && this.getOrderItemReferId().equals(targetDetail.getOrderItemReferId()))) {
            return;
        }
        if (!this.getStatementExpectPayTime().equals(targetDetail.getStatementExpectPayTime())) {
            return;
        }
        super.mergeAmountToTarget(targetDetail);
        if (this.getStatementEndTime().getTime() > targetDetail.getStatementEndTime().getTime()) {
            targetDetail.setStatementEndTime(this.getStatementEndTime());
        }
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
//                String payTimeStr = DateFormatUtils.format(super.getStatementExpectPayTime(), "yyyy-MM");
//                if(payTimeStr.equals(statementStatisticsDTO.getMonth()) && OrderPayMode.PAY_MODE_PAY_BEFORE.equals(super.getPayMode())){
                    return true;
//                }
//                return false;
            }
        }
        return super.isShowTheMonth(statementStatisticsDTO);
    }
}
