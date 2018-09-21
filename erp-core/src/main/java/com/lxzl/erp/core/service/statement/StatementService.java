package com.lxzl.erp.core.service.statement;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.callback.WeixinPayCallbackParam;
import com.lxzl.erp.common.domain.export.FinanceStatementOrderPayDetail;
import com.lxzl.erp.common.domain.k3.pojo.OrderStatementDateSplit;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.statement.StatementOrderDetailQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderPayParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.domain.statement.pojo.CheckStatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrder;
import com.lxzl.erp.common.domain.statement.pojo.dto.BaseCheckStatementDetailDTO;
import com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementSummaryDTO;
import com.lxzl.erp.dataaccess.domain.k3.returnOrder.K3ReturnOrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderDO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductDO;
import com.lxzl.erp.dataaccess.domain.reletorder.ReletOrderDO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import com.lxzl.se.core.service.BaseService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-06 14:57
 */
public interface StatementService extends BaseService {
    /**
     * 生成结算单
     *
     * @param startDate 结算开始时间
     * @param endDate   结算结束时间
     * @return 结算单号
     */
    ServiceResult<String, String> createStatementOrder(Date startDate, Date endDate);

    /**
     * 创建新订单的结算单, 在确认收货时生成。
     *
     * @param orderNo 订单号
     * @return 发货前需要交多少钱
     */
    ServiceResult<String, BigDecimal> createOrderStatement(String orderNo);
    ServiceResult<String, BigDecimal> createK3OrderStatement(Order order);

    /**
     * 创建续租单的 结算单
     *
     * @author ZhaoZiXuan
     * @date 2018/4/25 15:57
     * @param   orderDO 续租单的  订单ID
     * @return
     */
    ServiceResult<String, BigDecimal> createReletOrderStatement(ReletOrderDO orderDO);


    /**
     * 重新创建结算单
     * @param orderNo
     * @return
     */
    ServiceResult<String, BigDecimal> reCreateOrderStatement(String orderNo);

    /**
     * 重算结算单（修改结算日）
     * @param orderNo
     * @param statementDate
     * @return
     */
    ServiceResult<String, BigDecimal> reCreateOrderStatement(String orderNo,Integer statementDate);

    /**
     * 分段重新结算
     * @param k3StatementDateChange
     * @return
     */
    ServiceResult<String, BigDecimal> reCreateOrderStatement(OrderStatementDateSplit k3StatementDateChange);
    ServiceResult<String, BigDecimal> reCreateOrderStatementAllowConfirmCustommer(OrderDO orderDO);


    /**
     * 批量重算结算单
     * @param orderNoList
     * @param customerNoList
     * @return
     */
    String batchReCreateOrderStatement(List<String> orderNoList , List<String> customerNoList);

    /**
     * 计算订单首次需要缴纳费用
     *
     * @param orderDO 订单
     * @return 发货前需要交多少钱
     */
    ServiceResult<String, Map<String,BigDecimal>> calculateOrderFirstNeedPayAmount(OrderDO orderDO);

    /**
     * 支付结算单
     *
     * @param statementOrderNo 结算单编号
     * @return 是否支付成功
     */
    ServiceResult<String, Boolean> payStatementOrder(String statementOrderNo);

    /**
     * 批量支付结算单
     *
     * @param
     * @return
     */
    ServiceResult<String, List<String>> batchPayStatementOrder(List<StatementOrderPayParam> param);

    /**
     * 微信支付结算单
     *
     * @param statementOrderNo 结算单编号
     * @return 支付参数
     */
    ServiceResult<String, String> weixinPayStatementOrder(String statementOrderNo, String openId, String ip);

    /**
     * 微信支付回调
     *
     * @param param 回调参数
     * @return 支付参数
     */
    ServiceResult<String, String> weixinPayCallback(WeixinPayCallbackParam param);


    /**
     * 查询结算单
     *
     * @param statementOrderQueryParam 查询结算单参数
     * @return 结算单结果
     */
    ServiceResult<String, Page<StatementOrder>> queryStatementOrder(StatementOrderQueryParam statementOrderQueryParam);

    /**
     * 查询结算单详情
     *
     * @param statementOrderNo 结算单编号
     * @return 结算单详情
     */
    ServiceResult<String, StatementOrder> queryStatementOrderDetail(String statementOrderNo);


    /**
     * 查询结算单详情
     *
     * @param orderNo 结算单No
     * @return 结算单详情
     */
    ServiceResult<String, StatementOrder> queryStatementOrderDetailByOrderId(String orderNo);


    /**
     * 创建退货单结算单
     *
     * @param returnOrderNo 退货单编号
     * @return 退货金额
     */
    ServiceResult<String, BigDecimal> createReturnOrderStatement(String returnOrderNo);
    ServiceResult<String, BigDecimal> createK3ReturnOrderStatement(String returnOrderNo);


    /**
     * 创建换货单结算单
     *
     * @param changeOrderNo 换货单编号
     * @return 差价
     */
    ServiceResult<String, BigDecimal> createChangeOrderStatement(String changeOrderNo);
    ServiceResult<String, BigDecimal> createK3ChangeOrderStatement(String changeOrderNo);

    /**
     * 处理逾期的结算单
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否处理成功
     */
    ServiceResult<String, Boolean> handleOverdueStatementOrder(Date startTime, Date endTime);

    /**
     * 处理未支付的结算单
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否处理成功
     */
    ServiceResult<String, Boolean> handleNoPaidStatementOrder(Date startTime, Date endTime);

    /**
     * 结算单月对帐分页查询
     *
     * @param statementOrderMonthQueryParam
     * @return
     */
    ServiceResult<String, Page<StatementOrder>> queryStatementOrderCheckParam(StatementOrderMonthQueryParam statementOrderMonthQueryParam);

    /**
     * 结算单月对帐详情
     *
     * @param customerNo
     * @return
     */
    ServiceResult<String, StatementOrder> queryStatementOrderMonthDetail(String customerNo , Date month);
    /**
     * 结算导出详情分页查询
     *
     * @param statementOrderDetailQueryParam
     * @return
     */
    ServiceResult<String, Page<FinanceStatementOrderPayDetail>> queryFinanceStatementOrderPayDetail(StatementOrderDetailQueryParam statementOrderDetailQueryParam);

    /**
     * 重算续租单
     * @param reletOrderNo
     * @return
     */
    ServiceResult<String, BigDecimal> reCreateReletOrderStatement(String reletOrderNo);

    /**
     * 批量重算续租单（仅未支付允许批量重算）
     * @param reletOrderNos
     * @return
     */
    ServiceResult<String, BigDecimal> batchReCreateReletOrderStatement(List<String> reletOrderNos);

    /**
     * 仅重算退货单租金
     *
     * @param returnOrderNo
     * @return
     */
    ServiceResult<String, String> reStatementK3ReturnOrderRentOnly(String returnOrderNo);
    /**
     * 导入对账单查询
     * @Author : XiaoLuYu
     * @Date : Created in 2018/6/21 9:32
     */
    ServiceResult<String, List<CheckStatementOrder>> exportQueryStatementOrderCheckParam(StatementOrderMonthQueryParam statementOrderMonthQueryParam,Integer userId);

    /**
     * 支付结算单项
     * @param mergeStatementItemList
     * @return
     */
    ServiceResult<String, Boolean> payStatementOrderDetail(List<Integer> mergeStatementItemList);

    /**
     * 查询结算需支付金额
     * @param mergeStatementItemList
     * @return
     */
    ServiceResult<String, BigDecimal> queryStatementOrderDetailsNeedPay(List<Integer> mergeStatementItemList);

    /**
     * 批量退还应退的押金
     * @param orderNo
     * @return
     */
    String returnDeposit(String orderNo);

    /**
     * 取消已结算的退货单
     * @param returnOrderNo
     * @return
     */
    ServiceResult<String,String> rollbackSuccessReturnOrder(String returnOrderNo);

    /**
     * 订单退货时修改已成功还未开始的续租单数量
     * @param k3ReturnOrderDO
     */
    public ServiceResult<String, BigDecimal> fixReletOrderItemCount(K3ReturnOrderDO k3ReturnOrderDO);
    void saveStatementOrder(List<StatementOrderDetailDO> addStatementOrderDetailDOList, Date currentTime, Integer loginUserId);
    List<StatementOrderDetailDO> generateOrderProductStatement(Integer rentTimeLength, Integer statementMode, Date currentTime, Integer statementDays, Integer loginUserId, Date rentStartTime, Integer buyerCustomerId, Integer orderId, OrderProductDO orderProductDO, BigDecimal itemAllAmount);

    /**
     * <p>
     * 根据查询获取导出结算单详情列表数据
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     *     customerId : 231323 ： 客户id ： 必须
     *     statementOrderStartTime : 2018-01 ： 查询的开始时间(Date类型) ： 必须
     *     statementOrderEndTime : 2018-06 ： 查询的结束时间(Date类型) ： 必须
     *     orderIds :  ： 订单id列表 ： 必须
     *
     * </pre>
     *
     * @param statementOrderMonthQueryParam
     * @return com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.util.List<com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementDetailDTO>>
     * @author daiqi
     * @date 2018/7/4 11:04
     */
    ServiceResult<String, List<BaseCheckStatementDetailDTO>> listCheckStatementDetailDTOByQuery(StatementOrderMonthQueryParam statementOrderMonthQueryParam);


    /**
     * <p>
     * 汇总指定客户的结算单金额
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     *
     * @param statementOrderMonthQueryParam
     * @return com.lxzl.erp.common.domain.statement.pojo.dto.CheckStatementSummaryDTO
     * @author daiqi
     * @date 2018/7/5 13:35
     */
    CheckStatementSummaryDTO sumStatementDetailAmountByCustomerNo(StatementOrderMonthQueryParam statementOrderMonthQueryParam);

}
