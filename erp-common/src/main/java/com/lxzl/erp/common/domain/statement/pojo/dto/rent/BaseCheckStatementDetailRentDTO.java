package com.lxzl.erp.common.domain.statement.pojo.dto.rent;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.constant.OrderType;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author daiqi
 * @create 2018-07-09 9:56
 */
public class BaseCheckStatementDetailRentDTO extends BaseCheckStatementDetailDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        Order order = getOrderById(getOrderId());
        super.setOrderOriginalId(getOrderId());
        if(CommonConstant.COMMON_CONSTANT_YES.equals(order.getIsOriginalOrder())){
            super.setOrderNo(order.getOrderNo());
        }else {
            super.setOrderNo(order.getOriginalOrderNo());
        }
        super.setOrderRentStartTime(order.getRentStartTime());
        super.setOrderExpectReturnTime(order.getExpectReturnTime());
        super.setOrderItemActualId(getOrderItemReferId());
        return this;
    }

    @Override
    public boolean isAddTheMonth(CheckStatementStatisticsDTO statementStatisticsDTO) {
        boolean statementStartTimeFlag = (this.getStatementStartTime().getTime() > statementStatisticsDTO.getMonthEndTime());
        boolean statementEndTimeFlag = (this.getStatementEndTime().getTime() < statementStatisticsDTO.getMonthStartTime());
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

    @Override
    public boolean isMergeOrder(CheckStatementStatisticsDTO statementStatisticsDTO) {
        Integer orderType = this.getOrderType();
        if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(super.getPayMode())) {
            return false;
        }
        if (OrderType.ORDER_TYPE_ORDER.equals(orderType) || OrderType.ORDER_TYPE_RELET.equals(orderType)) {
            return true;
        }
        return false;
    }

}
