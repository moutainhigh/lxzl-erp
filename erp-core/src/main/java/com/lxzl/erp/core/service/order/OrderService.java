package com.lxzl.erp.core.service.order;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.erpInterface.order.InterfaceOrderQueryParam;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.core.service.VerifyReceiver;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderMaterialDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;

import java.util.Date;
import java.util.List;

public interface OrderService extends VerifyReceiver {

    /**
     * 创建订单
     *
     * @param order 订单信息
     * @return 订单编号
     */
    ServiceResult<String, String> createOrder(Order order);

    /**
     * 创建订单（包括组合商品）使用新接口，避免影响
     *
     * @param order 订单信息
     * @return 订单编号
     */
    ServiceResult<String, String> createOrderNew(Order order);

    /**
     * 编辑订单接口
     *
     * @param order 订单信息
     * @return 订单编号
     */
    ServiceResult<String, String> updateOrder(Order order);

    /**
     * 编辑订单接口（包括组合商品）使用新接口，避免影响
     *
     * @param order 订单信息
     * @return 订单编号
     */
    ServiceResult<String, String> updateOrderNew(Order order);

    /**
     * 提交订单
     *
     * @return 订单编号
     */
    ServiceResult<String, String> commitOrder(OrderCommitParam orderCommitParam);


    /**
     * 支付订单
     *
     * @param orderNo 订单编号
     * @return 支付结果
     */
    ServiceResult<String, String> payOrder(String orderNo);

    /**
     * 根据订单编号查询单个订单（包含组合商品）
     *
     * @param orderNo 订单编号
     * @return 订单信息
     */
    ServiceResult<String, Order> queryOrderByNoNew(String orderNo);

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
    ServiceResult<String, String> cancelOrder(String orderNo,Integer cancelOrderReasonType);

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
     * 根据用户ID查询订单
     *
     * @param interfaceOrderQueryParam 参数
     * @return 订单列表
     */
    ServiceResult<String, Page<Order>> queryOrderByUserIdInterface(InterfaceOrderQueryParam interfaceOrderQueryParam);

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

    /**
     * 订单是否需要审批
     *
     * @param orderNo 订单编号
     * @return true是false否
     */
    ServiceResult<String, Boolean> isNeedVerify(String orderNo);

    /**
     * 根据商品ID、物料ID，查询上次租赁的价格
     *
     * @param request 查询请求参数
     * @return 查询上次租赁的结果
     */
    ServiceResult<String, LastRentPriceResponse> queryLastPrice(LastRentPriceRequest request);

    /**
     * 设备归还接口
     *
     * @param orderNo           订单编号
     * @param returnEquipmentNo 退还设备编号
     * @param changeEquipmentNo 更换设备编号
     * @param returnDate        归还时间
     * @return 操作结果
     */
    ServiceResult<String, String> returnEquipment(String orderNo, String returnEquipmentNo, String changeEquipmentNo, Date returnDate);

    /**
     * 物料归还接口
     *
     * @param orderNo           订单编号
     * @param returnNBulkMaterialNo 退还散料编号
     * @param changeBulkMaterialNo 更换散料编号
     * @param returnDate        归还时间
     * @return 操作结果
     */
    ServiceResult<String, String> returnBulkMaterial(String orderNo, String returnNBulkMaterialNo, String changeBulkMaterialNo, Date returnDate);
    /**
    * 强制取消订单
    * @Author : XiaoLuYu
    * @Date : Created in 2018/3/9 18:52
    * @param : orderNo
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
    */
    ServiceResult<String, String> forceCancelOrder(String orderNo,Integer cancelOrderReasonType);

    /**
     * 订单计算首付金额
     *
     * @Author : kai
     * @Date : Created in 2018/3/17 11:25
     * @param order
     * @return
     */
    ServiceResult<String, Order> createOrderFirstPayAmount(Order order);
    /**
     * 处理取消订单的结算单（临时使用该接口）
     *
     * @Author : liuke
     * @Date : Created in 2018/3/28 17:25
     * @param orderNo
     * @return
     */
    ServiceResult<String, String> processStatementOrderByCancel(String orderNo);
    ServiceResult<String, String> addOrderMessage(Order order);

    boolean isCheckRiskManagement(OrderDO orderDO);
    void verifyCustomerRiskInfo(OrderDO orderDO);
    void calculateOrderProductInfo(List<OrderProductDO> orderProductDOList, OrderDO orderDO);
    void calculateOrderMaterialInfo(List<OrderMaterialDO> orderMaterialDOList, OrderDO orderDO);

    ServiceResult<String,Page<Order>> queryVerifyOrder(VerifyOrderQueryParam param);

    /**
     * 将完成的订单加入时间轴（兼容之前老数据订单完成后没加入时间轴）
     * @return
     */
    ServiceResult<String,String> addReturnOrderToTimeAxis();

    ServiceResult<String, Boolean> isNeedSecondVerify(String orderNo);
}
