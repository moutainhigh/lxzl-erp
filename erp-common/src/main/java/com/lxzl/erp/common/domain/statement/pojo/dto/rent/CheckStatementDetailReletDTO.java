package com.lxzl.erp.common.domain.statement.pojo.dto.rent;

import com.lxzl.erp.common.constant.OrderType;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;

/**
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailReletDTO extends BaseCheckStatementDetailRentDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        super.setOrderType(OrderType.ORDER_TYPE_RELET);
        setOrderItemActualId(super.getReletOrderItemReferId());
        return this;
    }
}
