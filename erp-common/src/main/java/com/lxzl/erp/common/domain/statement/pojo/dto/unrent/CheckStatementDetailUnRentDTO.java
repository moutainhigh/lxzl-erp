package com.lxzl.erp.common.domain.statement.pojo.dto.unrent;

import com.lxzl.erp.common.constant.OrderPayMode;
import com.lxzl.erp.common.constant.OrderType;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementStatisticsDTO;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * @author daiqi
 * @create 2018-07-09 9:32
 */
public class CheckStatementDetailUnRentDTO extends BaseCheckStatementDetailUnRentDTO {
    @Override
    protected BaseCheckStatementDetailDTO loadData() {
        super.loadData();
        super.setOrderType(OrderType.ORDER_TYPE_RETURN);
        return this;
    }

    @Override
    protected String doGetNoThisMonthCacheKey(CheckStatementStatisticsDTO statementStatisticsDTO) {
        String cacheKey = this.getOrderOriginalId() + "_" + super.getOrderItemActualId() + "_" + getOrderOriginalItemType(statementStatisticsDTO);
        if (OrderPayMode.PAY_MODE_PAY_AFTER.equals(super.getPayMode())) {
            cacheKey = cacheKey + "_" + DateFormatUtils.format(super.getStatementExpectPayTime(), "yyyyMMdd");
        }
        return cacheKey;
    }

}
