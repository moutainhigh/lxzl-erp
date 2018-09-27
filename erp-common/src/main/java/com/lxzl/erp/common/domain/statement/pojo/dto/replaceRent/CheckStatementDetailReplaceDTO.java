package com.lxzl.erp.common.domain.statement.pojo.dto.replaceRent;

import com.lxzl.erp.common.constant.OrderType;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;

public class CheckStatementDetailReplaceDTO extends BaseCheckStatementDetailReplaceDTO {

    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        super.setOrderType(OrderType.ORDER_TYPE_REPLACE);
        return this;
    }


}
