package com.lxzl.erp.core.service.order;

import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.pojo.OrderSplitDetail;

import java.util.List;

/**
 * @Auther: huahongbin
 * @Date: 2018/5/8 15:18
 * @Description:
 */
public interface OrderSplitDetailService {

    ServiceResult<String, Integer> addOrderSplitDetail(OrderSplitDetail orderSplitDetail);

    ServiceResult<String, List<OrderSplitDetail>> queryOrderSplitDetailByOrderItemTypeAndOrderItemReferId(Integer orderItemType, Integer orderItemReferId);
}
