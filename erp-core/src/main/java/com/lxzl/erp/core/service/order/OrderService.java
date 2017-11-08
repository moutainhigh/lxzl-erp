package com.lxzl.erp.core.service.order;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.se.core.service.BaseService;

public interface OrderService extends BaseService {

    // 创建订单
    ServiceResult<String, String> createOrder(Order order);

    // 根据订单号查询单个订单
    ServiceResult<String, Order> queryOrderByNo(String orderNo);

    /**
     * 取消订单
     * @param orderNo 订单号码
     * @return 是否成功，订单号码
     * */
    ServiceResult<String, Integer> cancelOrder(String orderNo);

    // 根据参数查询订单，后端使用
    ServiceResult<String, Page<Order>> queryAllOrder(OrderQueryParam param);

    // 根据用户ID查询订单
    ServiceResult<String, Page<Order>> queryOrderByUserId(OrderQueryParam param);

    // 订单发货
    ServiceResult<String, Integer> deliveryOrder(Order order);

    // 确认订单
    ServiceResult<String, Integer> confirmOrder(String orderNo);

    // 为订单项设备出库
    ServiceResult<String, Integer> outOrderProductEquipment(OrderProduct orderProduct);

    // 为订单项设备回库
    ServiceResult<String, Integer> returnOrderProductEquipment(OrderProduct orderProduct);

    // 为订单项变更设备
    ServiceResult<String, Integer> updateOrderProductEquipment(OrderProduct orderProduct);

    // 查询订单商品项
    ServiceResult<String, Page<OrderProduct>> queryOrderProductInfo(OrderQueryProductParam param);

}
