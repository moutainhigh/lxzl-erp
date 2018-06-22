package com.lxzl.erp.core.service.order.impl.support;

import com.lxzl.erp.common.constant.OrderRentType;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.se.common.util.date.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: huanglong
 * @date: 2018/6/19/019 14:18
 * @e_mail: huanglong5945@163.com
 * @Description:
 */
@Component
public class OrderSupport {

    public Date generateExpectReturnTime(OrderDO orderDO) {
        Date expectReturnTime = null;
        if (CollectionUtil.isNotEmpty(orderDO.getOrderProductDOList())) {
            for (OrderProductDO orderProductDO : orderDO.getOrderProductDOList()) {
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(orderDO.getRentStartTime(), orderProductDO.getRentType(), orderProductDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(orderDO.getOrderMaterialDOList())) {
            for (OrderMaterialDO orderMaterialDO : orderDO.getOrderMaterialDOList()) {
                Date thisExpectReturnTime = calculationOrderExpectReturnTime(orderDO.getRentStartTime(), orderMaterialDO.getRentType(), orderMaterialDO.getRentTimeLength());
                if (thisExpectReturnTime != null) {
                    expectReturnTime = expectReturnTime == null || expectReturnTime.getTime() < thisExpectReturnTime.getTime() ? thisExpectReturnTime : expectReturnTime;
                }
            }
        }
        return expectReturnTime;
    }

    /**
     * 计算订单预计归还时间
     */
    public Date calculationOrderExpectReturnTime(Date rentStartTime, Integer rentType, Integer rentTimeLength) {
        if (OrderRentType.RENT_TYPE_DAY.equals(rentType)) {
            return DateUtil.dateInterval(rentStartTime, rentTimeLength - 1);
        } else if (OrderRentType.RENT_TYPE_MONTH.equals(rentType)) {
            return DateUtil.dateInterval(DateUtil.monthInterval(rentStartTime, rentTimeLength), -1);
        }
        return null;
    }
}
