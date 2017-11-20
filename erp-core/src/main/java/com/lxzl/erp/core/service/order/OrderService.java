package com.lxzl.erp.core.service.order;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.se.core.service.BaseService;

public interface OrderService extends BaseService {

    /**
     * 创建订单
     *
     * @param order 订单信息
     * @return 订单编号
     */
    ServiceResult<String, String> createOrder(Order order);

    /**
     * 提交订单
     *
     * @param orderNo 订单编号
     * @return 订单编号
     */
    ServiceResult<String, String> commitOrder(String orderNo,Integer verifyUser);

    /**
     * 根据订单编号查询单个订单
     *
     * @param orderNo 订单编号
     * @return 订单信息
     */
    ServiceResult<String, Order> queryOrderByNo(String orderNo);

    /**
     * 取消订单
     *
     * @param orderNo 订单号码
     * @return 是否成功，订单号码
     */
    ServiceResult<String, Integer> cancelOrder(String orderNo);

    /**
     * 根据参数查询订单
     *
     * @param param 查询订单参数
     * @return 订单列表
     */
    ServiceResult<String, Page<Order>> queryAllOrder(OrderQueryParam param);

    /**
     * 根据用户ID查询订单
     *
     * @param param 参数
     * @return 订单列表
     */
    ServiceResult<String, Page<Order>> queryOrderByUserId(OrderQueryParam param);

    /**
     * 订单发货
     *
     * @param order 订单信息
     * @return 订单ID
     */
    ServiceResult<String, Integer> deliveryOrder(Order order);


    /**
     * 确认订单
     *
     * @param orderNo 订单编号
     * @return 订单ID
     */
    ServiceResult<String, Integer> confirmOrder(String orderNo);

    /**
     * 为订单项设备出库
     *
     * @param orderProduct 订单商品项
     * @return 订单号
     */
    ServiceResult<String, Integer> outOrderProductEquipment(OrderProduct orderProduct);

    /**
     * 为订单项设备回库
     *
     * @param orderProduct 订单商品项
     * @return 订单号
     */
    ServiceResult<String, Integer> returnOrderProductEquipment(OrderProduct orderProduct);

    /**
     * 为订单项变更设备
     *
     * @param orderProduct 订单商品项
     * @return 订单号
     */
    ServiceResult<String, Integer> updateOrderProductEquipment(OrderProduct orderProduct);

    /**
     * 查询订单商品项
     *
     * @param param 查询参数
     * @return 订单商品项列表
     */
    ServiceResult<String, Page<OrderProduct>> queryOrderProductInfo(OrderQueryProductParam param);

}
