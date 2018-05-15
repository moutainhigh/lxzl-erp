package com.lxzl.erp.core.service.order;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.pojo.OrderSplit;
import com.lxzl.erp.common.domain.order.pojo.OrderSplitDetail;

import java.util.List;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/8 15:18
 * @Description:
 */
public interface OrderSplitDetailService {

    ServiceResult<String, List<Integer>> addOrderSplitDetail(OrderSplit orderSplit);

    ServiceResult<String, List<OrderSplitDetail>> queryOrderSplitDetailByOrderItemTypeAndOrderItemReferId(Integer orderItemType, Integer orderItemReferId);

    ServiceResult<String, Integer> updateOrderSplit(OrderSplit orderSplit);

    ServiceResult<String, Integer> deleteOrderSplit(Integer orderItemType, Integer orderItemReferId);
}
