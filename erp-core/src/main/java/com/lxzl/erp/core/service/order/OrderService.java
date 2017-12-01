package com.lxzl.erp.core.service.order;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.core.service.VerifyReceiver;
import com.lxzl.se.core.service.BaseService;

public interface OrderService extends VerifyReceiver {

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
     * @param verifyUser 审核人
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
    ServiceResult<String, String> cancelOrder(String orderNo);

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
     * 处理订单
     *
     * @param param 处理订单
     * @return 订单ID
     */
    ServiceResult<String, String> processOrder(ProcessOrderParam param);

    /**
     * 订单发货
     *
     * @param order 订单信息
     * @return 订单ID
     */
    ServiceResult<String, String> deliveryOrder(Order order);


    /**
     * 确认订单
     *
     * @param orderNo 订单编号
     * @return 订单ID
     */
    ServiceResult<String, String> confirmOrder(String orderNo);

    /**
     * 查询订单商品项
     *
     * @param param 查询参数
     * @return 订单商品项列表
     */
    ServiceResult<String, Page<OrderProduct>> queryOrderProductInfo(OrderQueryProductParam param);

}
