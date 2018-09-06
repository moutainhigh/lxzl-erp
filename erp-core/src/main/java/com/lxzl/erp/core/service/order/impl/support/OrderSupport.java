package com.lxzl.erp.core.service.order.impl.support;

import com.lxzl.erp.common.constant.*;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
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

    /**
     * 判断是否可续租
     *
     * @author ZhaoZiXuan
     * @date 2018/5/25 10:47
     * @param
     * @return
     */
    public Integer isOrderCanRelet(Order order){

        if (null != order.getIsTurnRentOrder() && !CommonConstant.COMMON_ZERO.equals(order.getIsTurnRentOrder())){
            //转租赁的原测试机订单不能操作
            return  CanReletOrderStatus.CAN_RELET_ORDER_STATUS_NO;
        }
        //检查是否在续租时间范围
        Date currentTime = new Date();
        Integer dayCount = com.lxzl.erp.common.util.DateUtil.daysBetween(currentTime, order.getExpectReturnTime());
        if ((OrderRentType.RENT_TYPE_MONTH.equals(order.getRentType()) && dayCount >= CommonConstant.RELET_TIME_OF_RENT_TYPE_MONTH)
                || (OrderRentType.RENT_TYPE_DAY.equals(order.getRentType()) && dayCount >= CommonConstant.RELET_TIME_OF_RENT_TYPE_DAY)) {  //订单： 月租提前30天 和 天租提前15天 可续租
            return CanReletOrderStatus.CAN_RELET_ORDER_STATUS_NO;
        }

        //订单状态 必须是租赁中 ，续租中，部分退还  才可续租
        if (!OrderStatus.canReletOrderByCurrentStatus(order.getOrderStatus())){
            return CanReletOrderStatus.CAN_RELET_ORDER_STATUS_NO;
        }

        if (CollectionUtil.isNotEmpty(order.getReletOrderList())){
            for (ReletOrder reletOrder : order.getReletOrderList()){
                if (!ReletOrderStatus.canReletOrderByCurrentStatus(reletOrder.getReletOrderStatus())){

                    return CanReletOrderStatus.CAN_RELET_ORDER_STATUS_EXIST_WAIT_HANDLE;
                }
                else {

                    if (currentTime.compareTo(reletOrder.getRentStartTime()) < 0){  //如果当前续租还没开始  不允许再次续租

                        return CanReletOrderStatus.CAN_RELET_ORDER_STATUS_EXIST_SUCCESS_RELET_NOT_BEGIN;
                    }
                }
            }
        }

        return CanReletOrderStatus.CAN_RELET_ORDER_STATUS_YES;
    }
}
