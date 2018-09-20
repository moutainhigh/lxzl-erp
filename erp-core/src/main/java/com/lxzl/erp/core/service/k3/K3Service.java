package com.lxzl.erp.core.service.k3;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.k3.K3OrderQueryParam;
import com.lxzl.erp.common.domain.k3.K3SendRecordBatchParam;
import com.lxzl.erp.common.domain.k3.K3SendRecordParam;
import com.lxzl.erp.common.domain.k3.QueryK3StockParam;
import com.lxzl.erp.common.domain.k3.pojo.K3ProductStock;
import com.lxzl.erp.common.domain.k3.pojo.K3SendRecord;
import com.lxzl.erp.common.domain.k3.pojo.order.Order;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrderQueryParam;
import com.lxzl.erp.common.domain.order.OrderConfirmChangeToK3Param;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;

import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2018-02-11 20:54
 */
public interface K3Service {

    /**
     * 根据参数查询订单
     *
     * @param param 查询订单参数
     * @return 订单列表
     */
    ServiceResult<String, Page<Order>> queryAllOrder(K3OrderQueryParam param);

    ServiceResult<String, Order> queryOrder(String orderNo);

    /**
     * K3数据发送记录表分页
     *
     * @param k3SendRecordParam
     * @return
     */
    ServiceResult<String, Page<K3SendRecord>> queryK3SendRecord(K3SendRecordParam k3SendRecordParam);

    ServiceResult<String, Integer> sendAgainK3SendRecord(K3SendRecord k3SendRecord);

    ServiceResult<String, Map<String, String>> batchSendDataToK3(K3SendRecordBatchParam k3SendRecordBatchParam);

    ServiceResult<String, String> transferOrder(K3OrderQueryParam param);

    /**
     * <p>
     * 导入k3历史退货单
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     *     pageNo : 1 : 当前导入的页数 : 是
     *     pageSize : 10 : 每页显示数量 : 是
     * </pre>
     * @author daiqi
     * @date 2018/4/18 16:32
     * @param  k3ReturnOrderQueryParam

     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String, String> queryK3HistoricalRefundList(K3ReturnOrderQueryParam k3ReturnOrderQueryParam , StringBuffer info);

    /**
     * 推送确认收货的信息给K3
     * @param orderConfirmChangeToK3Param
     * @return
     */
    ServiceResult<String,String> confirmOrder(OrderConfirmChangeToK3Param orderConfirmChangeToK3Param);

    /**
     * 推送 订单续租信息 到
     *
     * @author ZhaoZiXuan
     * @date 2018/6/1 9:25
     * @param
     * @return
     */
    public ServiceResult<String, String> sendReletOrderInfoToK3(ReletOrderDO reletOrderDO, OrderDO orderDO);

    /**
     * 查询K3库存
     * @param queryK3StockParam
     * @return
     */
    ServiceResult<String, List<K3ProductStock>> queryK3Stock(QueryK3StockParam queryK3StockParam);

    /**
     * 由测试机订单转为租赁订单，审核通过后，传送订单数据给K3
     * @param order
     * @return
     */
    ServiceResult<String,String> testMachineOrderTurnRentOrder(com.lxzl.erp.common.domain.order.pojo.Order order);



}
