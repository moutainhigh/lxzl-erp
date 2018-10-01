package com.lxzl.erp.common.domain.statement.pojo.dto.unrent;

import com.lxzl.erp.common.constant.OrderType;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;

/**
 * 续租退货数据传输类
 *
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailUnRentReletDTO extends BaseCheckStatementDetailUnRentDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        super.setOrderType(OrderType.ORDER_TYPE_RELET_RETURN);
        super.setOrderItemActualId(super.getReletOrderItemReferId());
        return this;
    }
}
